package aj.programming.MQTTConnector.Source;

import aj.programming.MQTTConnector.Buffers.MessageBuffer;
import aj.programming.MQTTConnector.Config.ConfigNames;
import aj.programming.MQTTConnector.Config.MQTTConfig;
import aj.programming.MQTTConnector.Helpers.MqttConnectOptionsHelper;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MQTTSourceClient extends MqttClient {
    private final Logger logger = LoggerFactory.getLogger(MQTTSourceClient.class);
    private final MessageBuffer messageBuffer;

    public MQTTSourceClient(MQTTConfig mqttConfig, MessageBuffer messageBuffer) throws MqttException {
        super(
                mqttConfig.getString(ConfigNames.BROKER),
                mqttConfig.getString(ConfigNames.MQTT_CLIENT_ID)
        );
        this.logger.info("Initializing MQTTSourceClient");
        this.messageBuffer = messageBuffer;
        this.initialize(mqttConfig);
        logger.info("MQTTSourceClient initialized");

    }

    @Override
    public void subscribe(String topicFilters, int qos) throws MqttException {
        this.logger.info("Started subscription");
        super.subscribe(topicFilters, qos, (topic, message) -> {
            this.logger.info("Received message: {} from topic: {}", message, topic);
            String clientId = this.getClientId();
            this.messageBuffer.addMessage(String.valueOf(message), clientId);
        });
    }

    private void initialize(MQTTConfig mqttConfig) throws MqttException {
        this.logger.info("Adding MQTT options");
        MqttConnectOptions mqttConnectOptions = MqttConnectOptionsHelper.getOptions(mqttConfig);
        this.logger.info("Initialized MQTTClient");
        this.connect(mqttConnectOptions);
        this.logger.info("Connected MQTTClient");
        this.subscribe(mqttConfig.getString(
                        ConfigNames.MQTT_TOPIC),
                mqttConfig.getInt(ConfigNames.MQTT_QOS)
        );
    }
}
