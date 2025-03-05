package aj.programming.MQTTConnector.Sink;

import aj.programming.MQTTConnector.Buffers.SinkMessageBuffer;
import aj.programming.MQTTConnector.DTO.MessageDTO;
import aj.programming.MQTTConnector.Helpers.MqttConnectOptionsHelper;
import aj.programming.MQTTConnector.Config.ConfigNames;
import aj.programming.MQTTConnector.Config.MQTTConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

public class MQTTSinkClient extends MqttClient {
    private Logger logger = LoggerFactory.getLogger(MQTTSinkClient.class);
    private SinkMessageBuffer sinkMessageBuffer;
    private final ObjectMapper objectMapper;
    private MQTTConfig config;
    private MQTTPublisher mqttPublisher;
    private Thread mqttPublisherThread;
    private class MQTTPublisher implements Runnable {
        private volatile boolean running;

        @Override
        public void run() {
            while (this.running) {
                try {
                    MessageDTO messageDTO = sinkMessageBuffer.poll();
                    if (messageDTO != null) {
                        String topic = config.getString(ConfigNames.MQTT_TOPIC);
                        int qos = config.getInt(ConfigNames.MQTT_QOS);
                        MqttMessage mqttMessage = new MqttMessage();
                        mqttMessage.setId(messageDTO.getMessageNumber());
                        mqttMessage.setQos(qos);
                        String msgValue = objectMapper.writeValueAsString(messageDTO);
                        mqttMessage.setPayload(msgValue.getBytes(StandardCharsets.UTF_8));
                        publish(topic, mqttMessage);
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

    public MQTTSinkClient(MQTTConfig mqttConfig, SinkMessageBuffer sinkMessageBuffer) throws MqttException {
        super(
                mqttConfig.getString(ConfigNames.BROKER),
                mqttConfig.getString(ConfigNames.MQTT_CLIENT_ID)
        );
        this.objectMapper = new ObjectMapper();
        this.sinkMessageBuffer = sinkMessageBuffer;
        this.connect(mqttConfig);
        this.mqttPublisher = new MQTTPublisher();
        this.mqttPublisherThread = new Thread(mqttPublisher);
        mqttPublisherThread.start();
    }

    public void connect(MQTTConfig mqttConfig) throws MqttException {
        MqttConnectOptions mqttConnectOptions = MqttConnectOptionsHelper.getOptions(mqttConfig);
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
}
