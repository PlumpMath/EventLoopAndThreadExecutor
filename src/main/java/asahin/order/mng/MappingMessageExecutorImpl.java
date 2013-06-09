package asahin.order.mng;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MappingMessageExecutorImpl implements MessageExecutor {

    final List<ExecutorService> services = new ArrayList<ExecutorService>();
    final ConcurrentMap<String, ExecutorService> messageThreadMap = new ConcurrentHashMap<String, ExecutorService>();

    public MappingMessageExecutorImpl(int workerThreadSize) {
        for (int i = 0; i < workerThreadSize; i++) {
            services.add(Executors.newSingleThreadExecutor());
        }
    }

    @Override
    public void processMessage(Message message) {
        String correlationId = message.getCorrelationId();
        if (null == correlationId) {
            throw new IllegalArgumentException("Message correlationId is null");
        }
        ExecutorService newService = getExecutorService();
        ExecutorService service = messageThreadMap.putIfAbsent(correlationId, newService);

        if (null == service) {
            service=newService;
        }
        service.execute(new RunnableTask(message));
    }

    private ExecutorService getExecutorService() {
        ExecutorService service;//pick a random executor
        Random random = new Random();
        int indx = random.nextInt(services.size());
        service = services.get(indx);
        return service;
    }

    static class RunnableTask implements Runnable {
        private final Message message;

        RunnableTask(Message message) {
            this.message = message;
        }

        @Override
        public void run() {
            System.out.println("processing " + message + " in thread " + Thread.currentThread());
        }
    }
}
