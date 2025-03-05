package aj.programming.MQTTConnector.Source;

import aj.programming.MQTTConnector.Buffers.SourceMessageBuffer;
import aj.programming.MQTTConnector.DTO.MessageDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MQTTSourceTask extends SourceTask {
    private final Logger logger = LoggerFactory.getLogger(MQTTSourceTask.class);
    private final ObjectMapper objectMapper;
    private MQTTSourceConfig config;
    private MQTTSourceClient mqttClient;
    private SourceMessageBuffer buffer;

    public MQTTSourceTask() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public String version() {
        return "";
    }

    @Override
    public void start(Map<String, String> map) {
        this.buffer = new SourceMessageBuffer();
        this.logger.info("New task started");
        this.config = new MQTTSourceConfig(map);
        try {
            this.logger.info("Creating MQTTSourceClient");
            this.mqttClient = new MQTTSourceClient(this.config, this.buffer, this.config.getString(MQTTConfigNames.BROKER), this.config.getString(MQTTConfigNames.MQTT_CLIENT_ID));

        } catch (MqttException e) {
            this.logger.error("Cannot start MQTTSourceCLient");

            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SourceRecord> poll() throws InterruptedException {
        logger.info("Polling messages");
        List<SourceRecord> records = new ArrayList<>();
        MessageDTO message = buffer.poll();

        String uniqueId = config.getString(MQTTConfigNames.UNIQUE_ID);
        String mqttTopic = config.getString(MQTTConfigNames.MQTT_TOPIC);
        String kafkaTopic = config.getString(MQTTConfigNames.KAFKA_TOPIC);
        String parsedMessage = null;
        try {
             parsedMessage = this.objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            logger.error("Cannot parse message to string", e);
            throw new RuntimeException(e);
        }
        Map<String, Object> kafkaOffset = new HashMap<>(Map.of(
                "id", message.getMessageId(),
                "timestamp", message.getTimestamp()
        ));
        Map<String, Object> partition = new HashMap<>(Map.of(
                "mqttTopic", mqttTopic
        ));


        logger.info("Get messages from buffer");
        if (message != null) {
            SourceRecord record = new SourceRecord(
                    partition,
                    kafkaOffset,
                    kafkaTopic,
                    null,
                    uniqueId,
                    null,
                    parsedMessage
            );
            records.add(record);
            logger.info("Messages added to record, sending for topic: {}", kafkaTopic);
        }
        return records;
    }

    @Override
    public void stop() {
        this.logger.info("Task stopped");

    }

}
