package asahin.order.mng;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Reactor {
    private final MessageQueue messageQueue;
    private final MessageExecutor messageExecutor;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public Reactor(MessageQueue messageQueue, MessageExecutor messageExecutor) {
        this.messageQueue = messageQueue;
        this.messageExecutor = messageExecutor;
    }

    public void execute() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Message message = messageQueue.listen();
                    if (null != message) {
                        messageExecutor.processMessage(message);
                    }
                }
            }
        });

    }

    public void stop() {
        // stop execution forcefully for demo purpose.
        executor.shutdownNow();
    }

}
