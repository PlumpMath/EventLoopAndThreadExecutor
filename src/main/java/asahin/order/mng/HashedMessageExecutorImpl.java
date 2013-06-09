package asahin.order.mng;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HashedMessageExecutorImpl implements MessageExecutor {

    final List<ExecutorService> services = new ArrayList<ExecutorService>();

    public HashedMessageExecutorImpl(int workerThreadSize) {
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
        int hashCode = correlationId.hashCode();
        // take a modulus of the calculated hash
        int index = hashCode % services.size();
        // use remainder as the pointer to the thread executor
        ExecutorService service = services.get(index);
        service.execute(new RunnableTask(message));
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
