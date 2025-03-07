package aj.programming.MQTTConnector.Sink;

import aj.programming.MQTTConnector.Buffers.MessageBuffer;
import aj.programming.MQTTConnector.Config.MQTTConfig;
import aj.programming.MQTTConnector.DTO.MessageDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class MQTTSinkTask extends SinkTask {
    private final Logger logger = LoggerFactory.getLogger(MQTTSinkTask.class);
    private final ObjectMapper objectMapper;
    private MQTTConfig mqttConfig;
    private MQTTSinkClient mqttSinkClient;
    private MessageBuffer sinkMessageBuffer;

    public MQTTSinkTask() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public String version() {
        return "";
    }

    @Override
    public void start(Map<String, String> props) {
        this.sinkMessageBuffer = new MessageBuffer();
        this.mqttConfig = new MQTTConfig(props);
        try {
            this.mqttSinkClient = new MQTTSinkClient(mqttConfig, sinkMessageBuffer);
        } catch (MqttException e) {
            this.logger.error("Cannot connect to MQTT broker", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void put(Collection<SinkRecord> records) {
        Iterator<SinkRecord> sinkRecordIterator = records.iterator();
        while (sinkRecordIterator.hasNext()) {
            SinkRecord record = sinkRecordIterator.next();
            logger.info("Received message from topic: {}, timestamp: {}, message: {}", record.topic(), record.timestamp(), record.value().toString());
            String recordValue = record.value().toString();
            try {
                MessageDTO messageDTO = this.objectMapper.readValue(recordValue, MessageDTO.class);
                logger.info("Addeing incoming KafkaMessage to buffer, messageId={}, messageNumber={}", messageDTO.getMessageId(), messageDTO.getMessageNumber());
                this.sinkMessageBuffer.addMessage(messageDTO);
            } catch (JsonProcessingException e) {
                logger.error("Error converting incoming Kafka Message, to MessageDTO", e);
            }

        }
    }

    @Override
    public void stop() {
        this.mqttSinkClient.stop();
        logger.info("MQTTSinkClient was stopped");
    }
}
