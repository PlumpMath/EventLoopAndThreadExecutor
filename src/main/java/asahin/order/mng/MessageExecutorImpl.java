package asahin.order.mng;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessageExecutorImpl implements MessageExecutor {

    final ExecutorService service;

    public MessageExecutorImpl(int workerThreadSize) {
        service = Executors.newFixedThreadPool(workerThreadSize);
    }


    @Override
    public void processMessage(Message message) {
        final Message messageCopy = message;
        this.service.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("processing " + messageCopy + " in thread " + Thread.currentThread());
            }
        });
    }


}
