package asahin.order.mng;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.concurrent.CountDownLatch;

public class ReactorTest {

    MessageQueue messageQueue = Mockito.mock(MessageQueue.class);
    MessageExecutor messageExecutor = new MessageExecutorImpl(3);
    MappingMessageExecutorImpl mappingMessageExecutor = new MappingMessageExecutorImpl(3);
    HashedMessageExecutorImpl hashedMessageExecutor = new HashedMessageExecutorImpl(3);
    private Message message1 = new Message("M1");
    private Message message2 = new Message("M2");
    private Message message3 = new Message("M3");
    private Message message4 = new Message("M4");
    private Message message5 = new Message("M5");


    @Before
    public void setUp() throws Exception {
        Mockito.when(messageQueue.listen()).thenReturn(message1).thenReturn(message2).thenReturn(message3)
                .thenReturn(message4).thenReturn(message5).thenAnswer(new BlockingListenerAnswer());
    }


    @Test
    public void testExecute() throws InterruptedException {
        Reactor reactor = new Reactor(messageQueue, messageExecutor);
        reactor.execute();
        Thread.sleep(1000);
        reactor.stop();
    }

    @Test
    public void testOrderedExecute() throws InterruptedException {
        message1.setCorrelationId("Trader1");
        message2.setCorrelationId("Trader2");
        message3.setCorrelationId("Trader3");
        message4.setCorrelationId("Trader1");
        message5.setCorrelationId("Trader2");
        Reactor reactor = new Reactor(messageQueue, mappingMessageExecutor);

        reactor.execute();
        Thread.sleep(1000);
        reactor.stop();

    }

    @Test
    public void testOrderedExecuteWithHashedMessageExecutor() throws InterruptedException {
        message1.setCorrelationId("Trader1");
        message2.setCorrelationId("Trader2");
        message3.setCorrelationId("Trader3");
        message4.setCorrelationId("Trader1");
        message5.setCorrelationId("Trader2");
        Reactor reactor = new Reactor(messageQueue, hashedMessageExecutor);

        reactor.execute();
        Thread.sleep(1000);
        reactor.stop();

    }

    //simulate a blocking listening operation
    static class BlockingListenerAnswer implements Answer<Message> {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        @Override
        public Message answer(InvocationOnMock invocationOnMock) throws Throwable {
            countDownLatch.await();
            return null;
        }
    }

}
