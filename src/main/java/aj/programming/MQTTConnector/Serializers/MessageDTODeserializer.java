package aj.programming.MQTTConnector.Serializers;

import aj.programming.MQTTConnector.DTO.MessageDTO;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class MessageDTODeserializer extends JsonDeserializer<MessageDTO> {
    @Override
    public MessageDTO deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        JsonNode root = mapper.readTree(jsonParser);
        JsonNode messageNumberNode = root.get("messageNumber");
        JsonNode clientIdNode = root.get("clientId");
        JsonNode messageNode = root.get("message");
        JsonNode messageIdNode = root.get("messageId");
        JsonNode timestampNode = root.get("timestamp");
        if (timestampNode == null) {
            throw new NullPointerException("timestamp in message cannot be null");
        }

        if (messageIdNode == null || messageIdNode.asText().isEmpty()) {
            throw new NullPointerException("messageId in message cannot be null");
        }
        if (messageNode == null || messageNode.asText().isEmpty()) {
            throw new NullPointerException("message in message cannot be null");
        }
        if (messageNumberNode == null || messageNumberNode.asText().isEmpty()) {
            throw new NullPointerException("messageNumber in message cannot be null");
        }

        if (clientIdNode == null || clientIdNode.asText().isEmpty()) {
            throw new NullPointerException("clientId in message cannot be null");
        }
        String message = messageNode.asText();
        String clientId = clientIdNode.asText();
        String messageId = messageIdNode.asText();
        long timestamp = timestampNode.asLong();
        int messageNumber = messageNumberNode.asInt();
        return new MessageDTO(message, clientId, messageId, messageNumber, timestamp);


    }
}
