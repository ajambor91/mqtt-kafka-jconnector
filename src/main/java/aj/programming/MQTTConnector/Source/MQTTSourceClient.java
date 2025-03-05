package aj.programming.MQTTConnector.Source;

import aj.programming.MQTTConnector.Buffers.SourceMessageBuffer;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MQTTSourceClient extends MqttClient {
    private final Logger logger = LoggerFactory.getLogger(MQTTSourceClient.class);
    private final SourceMessageBuffer messageBuffer;

    public MQTTSourceClient(MQTTSourceConfig mqttSourceConfig, SourceMessageBuffer messageBuffer, String serverURI, String clientId) throws MqttException {
        super(serverURI, clientId);
        this.logger.info("Created MQTTSourceClient");
        this.messageBuffer = messageBuffer;
        this.initialize(mqttSourceConfig);
    }

    @Override
    public void subscribe(String topicFilters, int qos) throws MqttException {
        this.logger.info("Started subscription");
        super.subscribe(topicFilters, qos, (topic, message) -> {
            this.logger.info("Received message: {} from topic: {}", message, topic);
            this.messageBuffer.addMessage(String.valueOf(message));
        });
    }

    private void initialize(MQTTSourceConfig mqttSourceConfig) throws MqttException {
        this.logger.info("Adding MQTT options");
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        this.addBasicMQTTOptions(mqttConnectOptions, mqttSourceConfig);
        this.addCredentialsIfExists(mqttConnectOptions, mqttSourceConfig);
        this.logger.info("Initialized MQTTClient");
        this.connect(mqttConnectOptions);
        this.logger.info("Connected MQTTClient");
        this.subscribe(mqttSourceConfig.getString(
                        MQTTConfigNames.MQTT_TOPIC),
                mqttSourceConfig.getInt(MQTTConfigNames.MQTT_QOS)
        );
    }

    private void addBasicMQTTOptions(MqttConnectOptions mqttConnectOptions, MQTTSourceConfig mqttSourceConfig) {
        mqttConnectOptions.setConnectionTimeout(mqttSourceConfig.getInt(MQTTConfigNames.MQTT_CONNECTIONTIMEOUT));
        mqttConnectOptions.setCleanSession(mqttSourceConfig.getBoolean(MQTTConfigNames.MQTT_CLEANSESSION));
        mqttConnectOptions.setKeepAliveInterval(mqttSourceConfig.getInt(MQTTConfigNames.MQTT_KEEPALIVEINTERVAL));
        mqttConnectOptions.setAutomaticReconnect(mqttSourceConfig.getBoolean(MQTTConfigNames.MQTT_ARC));
    }

    private void addCredentialsIfExists(MqttConnectOptions mqttConnectOptions, MQTTSourceConfig mqttSourceConfig) {

        if (!mqttSourceConfig.getString(MQTTConfigNames.MQTT_USERNAME).isEmpty()
                && !mqttSourceConfig.getString(MQTTConfigNames.MQTT_PASSWORD).isEmpty()) {
            this.logger.info("MQTT with passwordt");
            mqttConnectOptions.setUserName(mqttSourceConfig.getString(MQTTConfigNames.MQTT_USERNAME));
            mqttConnectOptions.setPassword(mqttSourceConfig.getString(MQTTConfigNames.MQTT_PASSWORD).toCharArray());
        }
    }
}
