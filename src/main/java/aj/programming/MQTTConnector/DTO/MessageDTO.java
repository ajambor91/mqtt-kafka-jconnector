package aj.programming.MQTTConnector.DTO;

public class MessageDTO {
    private final String message;
    private final String messageId;
    private final int messageNumber;
    private final long timestamp;

    public MessageDTO(String message, String messageId, int messageNumber, long timestamp) {
        this.message = message;
        this.messageNumber = messageNumber;
        this.messageId = messageId;
        this.timestamp = timestamp;
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageId() {
        return messageId;
    }

    public long getTimestamp() {
        return timestamp;
    }


}
