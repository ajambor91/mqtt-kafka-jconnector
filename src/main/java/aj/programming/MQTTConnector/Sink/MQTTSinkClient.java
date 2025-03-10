package aj.programming.MQTTConnector.Sink;

import aj.programming.MQTTConnector.Buffers.MessageBuffer;
import aj.programming.MQTTConnector.Config.ConfigNames;
import aj.programming.MQTTConnector.Config.MQTTConfig;
import aj.programming.MQTTConnector.DTO.MessageDTO;
import aj.programming.MQTTConnector.Helpers.MqttConnectOptionsHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

public class MQTTSinkClient extends MqttClient {
    private final ObjectMapper objectMapper;
    private final Logger logger = LoggerFactory.getLogger(MQTTSinkClient.class);
    private final MessageBuffer sinkMessageBuffer;
    private final MQTTConfig config;
    private final MQTTPublisherRunnable mqttPublisher;
    private final Thread mqttPublisherThread;

    public MQTTSinkClient(MQTTConfig mqttConfig, MessageBuffer sinkMessageBuffer) throws MqttException {
        super(
                mqttConfig.getString(ConfigNames.BROKER),
                mqttConfig.getString(ConfigNames.MQTT_CLIENT_ID)
        );
        logger.info("Initializing MQTTSinkClient");
        this.config = mqttConfig;
        this.objectMapper = new ObjectMapper();
        this.sinkMessageBuffer = sinkMessageBuffer;
        this.mqttPublisher = new MQTTPublisher();
        this.mqttPublisherThread = new Thread(mqttPublisher);
        logger.info("MQTTSinkClient initialized");
    }

    public void connect() throws MqttException {
        MqttConnectOptions mqttConnectOptions = MqttConnectOptionsHelper.getOptions(config);
        this.connect(mqttConnectOptions);
    }

    public void stop() {
        if (mqttPublisher != null) {
            mqttPublisher.stop();
        }
        if (mqttPublisher != null) {
            mqttPublisherThread.interrupt();
        }
        try {
            this.disconnect();
        } catch (MqttException e) {
            logger.error("Error disconnecting", e);
        }

    }

    private class MQTTPublisher implements MQTTPublisherRunnable {
        private final String clientId;
        private volatile boolean running;

        private MQTTPublisher() {
            logger.info("Initializing MQTTPublisher");
            this.clientId = config.getString(ConfigNames.MQTT_CLIENT_ID);
            this.running = true;
        }

        @Override
        public void run() {
            logger.info("MQTTSinkClient Thread running: {}", this.running);
            while (this.running) {
                try {
                    MessageDTO messageDTO = sinkMessageBuffer.poll();
                    logger.info("Sink client, get message from buffer, messageId: {}, messageNumber: {}", messageDTO.getMessageId(), messageDTO.getMessageNumber());
                    if (messageDTO != null && messageDTO.getClientId().equals(this.clientId)) {
                        logger.info("Creating MQTTMessage, messageId: {}, messageNumber: {}", messageDTO.getMessageId(), messageDTO.getMessageNumber());
                        String topic = config.getString(ConfigNames.MQTT_TOPIC);
                        int qos = config.getInt(ConfigNames.MQTT_QOS);
                        MqttMessage mqttMessage = new MqttMessage();
                        mqttMessage.setId(messageDTO.getMessageNumber());
                        mqttMessage.setQos(qos);
                        String msgValue = objectMapper.writeValueAsString(messageDTO);
                        logger.info("MQTT message was parsed: {}", msgValue);
                        mqttMessage.setPayload(msgValue.getBytes(StandardCharsets.UTF_8));
                        publish(topic, mqttMessage);
                        logger.info("MQTTMessage was published: {}", msgValue);
                    }

                } catch (InterruptedException e) {
                    logger.error("Error when polling buffer", e);
                } catch (JsonProcessingException e) {
                    logger.error("Error parsing MessageDTO to string", e);

                } catch (MqttException e) {
                    logger.error("Error publishing MQTTMessage", e);
                }
            }
        }

        public void stop() {
            this.running = false;
        }
    }
}
