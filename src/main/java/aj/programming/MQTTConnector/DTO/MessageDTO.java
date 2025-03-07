package aj.programming.MQTTConnector.DTO;

import aj.programming.MQTTConnector.Serializers.MessageDTODeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = MessageDTODeserializer.class)
public class MessageDTO {
    private final String message;
    private final String clientId;
    private final String messageId;
    private final int messageNumber;
    private final long timestamp;


    public MessageDTO(String message, String clientId, String messageId, int messageNumber, long timestamp) {
        this.message = message;
        this.clientId = clientId;
        this.messageNumber = messageNumber;
        this.messageId = messageId;
        this.timestamp = timestamp;
    }

    public String getClientId() {
        return clientId;
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
