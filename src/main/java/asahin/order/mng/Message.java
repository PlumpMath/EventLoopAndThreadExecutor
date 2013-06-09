package asahin.order.mng;

public class Message {
    String messageId;
    private String correlationId;

    public Message(String messageId) {
        this.messageId = messageId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    @Override
    public String toString() {
        return "Message:=" + messageId + " Correlation:"+correlationId;
    }
}
