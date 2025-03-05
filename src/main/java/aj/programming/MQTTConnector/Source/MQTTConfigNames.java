package aj.programming.MQTTConnector.Source;

import org.apache.kafka.common.config.ConfigDef;

import java.util.HashSet;
import java.util.Set;

public class MQTTConfigNames {
    public static final String BROKER = "mqtt.broker";
    public static final String BROKER_DOC = "The URL of the MQTT broker (e.g., tcp://localhost:1883).";


    public static final String MQTT_TOPIC = "mqtt.topic";
    public static final String MQTT_TOPIC_DOC = "The MQTT topic to subscribe to and receive messages from.";

    public static final String MQTT_ARC = "mqtt.automaticReconnect";
    public static final String MQTT_ARC_DOC = "Enables automatic reconnection to the MQTT broker.";

    public static final String MQTT_KEEPALIVEINTERVAL = "mqtt.keepAliveInterval";
    public static final String MQTT_KEEPALIVEINTERVAL_DOC = "The keep-alive interval in seconds for the MQTT connection.";

    public static final String MQTT_CLEANSESSION = "mqtt.cleanSession";
    public static final String MQTT_CLEANSESSION_DOC = "Enables a clean session for the MQTT connection.";

    public static final String MQTT_CONNECTIONTIMEOUT = "mqtt.connectionTimeout";
    public static final String MQTT_CONNECTIONTIMEOUT_DOC = "The connection timeout in seconds for the MQTT broker connection.";

    public static final String KAFKA_TOPIC = "kafka.topic";
    public static final String KAFKA_TOPIC_DOC = "The Kafka topic to send messages to.";
    public static final String MQTT_QOS = "mqtt.qos";
    public static final String MQTT_QOS_DOC = "The Quality of Service (QoS) level for the MQTT connection (0, 1, or 2).";

    public static final String MQTT_USERNAME = "mqtt.userName";
    public static final String MQTT_USERNAME_DOC = "The username for authenticating with the MQTT broker.";

    public static final String MQTT_PASSWORD = "mqtt.password";
    public static final String MQTT_PASSWORD_DOC = "The password for authenticating with the MQTT broker.";

    public static final String MQTT_CLIENT_ID = "mqtt.clientId";
    public static final String MQTT_CLIENT_ID_DOC = "An alternative client ID for the MQTT connection.";

    public static final String KAFKA_SERVER = "kafka.server";
    public static final String KAFKA_SERVER_DOC = "Bootstrap kafka server";
    public static final String UNIQUE_ID = "uniqueId";
    public static final String UNIQUE_ID_DOC = "Unique connector ID";
    public static final String VALUE_CONVERTER_SCHEMAS_ENABLE = "value.converter.schemas.enable";
    public static final String VALUE_CONVERTER_SCHEMAS_ENABLE_DOC = "Enables schemas in the value converter.";

    public static final String VALUE_CONVERTER = "value.converter";
    public static final String VALUE_CONVERTER_DOC = "The value converter for Kafka Connect messages.";
    public static final String KAFKA_KEY_SERIALIZER = "kafka.key.serializer";
    public static final String KAFKA_KEY_SERIALIZER_DOC = "Class of kafka serializer";
    public static final String KAFKA_VALUE_SERIALIZER = "kafka.value.serializer";
    public static final String KAFKA_VALUE_SERIALIZER_DOC = "Class of kafka value serializer";
    public static final String KEY_CONVERTER_SCHEMAS_ENABLE = "key.converter.schemas.enable";
    public static final String KEY_CONVERTER_SCHEMAS_ENABLE_DOC = "Enables schemas in the key converter.";

    public static final String KEY_CONVERTER = "key.converter";
    public static final String KEY_CONVERTER_DOC = "The key converter for Kafka Connect messages. Defaults to org.apache.kafka.connect.storage.StringConverter.";

    public static final String CONNECTOR_CLASS = "connector.class";
    public static final String CONNECTOR_CLASS_DOC = "The Kafka Connect connector class name.";
    public static Set<ConfigData> configSet = new HashSet<>();

    static {
        configSet.add(new ConfigData(BROKER, BROKER_DOC, true));
        configSet.add(new ConfigData(MQTT_TOPIC, MQTT_TOPIC_DOC, true));
        configSet.add(new ConfigData(UNIQUE_ID, UNIQUE_ID_DOC, true));
        configSet.add(new ConfigData(MQTT_ARC, MQTT_ARC_DOC, ConfigDef.Type.BOOLEAN, ConfigDef.Importance.LOW));
        configSet.add(new ConfigData(MQTT_KEEPALIVEINTERVAL, MQTT_KEEPALIVEINTERVAL_DOC, ConfigDef.Type.INT, ConfigDef.Importance.LOW));
        configSet.add(new ConfigData(MQTT_CLEANSESSION, MQTT_CLEANSESSION_DOC, ConfigDef.Type.BOOLEAN, ConfigDef.Importance.LOW));
        configSet.add(new ConfigData(MQTT_CONNECTIONTIMEOUT, MQTT_CONNECTIONTIMEOUT_DOC, ConfigDef.Type.INT, ConfigDef.Importance.MEDIUM));
        configSet.add(new ConfigData(KAFKA_TOPIC, KAFKA_TOPIC_DOC, true));
        configSet.add(new ConfigData(MQTT_QOS, MQTT_QOS_DOC, ConfigDef.Type.INT, ConfigDef.Importance.MEDIUM));
        configSet.add(new ConfigData(MQTT_USERNAME, MQTT_USERNAME_DOC, ConfigDef.Importance.MEDIUM));
        configSet.add(new ConfigData(MQTT_PASSWORD, MQTT_PASSWORD_DOC, ConfigDef.Importance.MEDIUM));
        configSet.add(new ConfigData(MQTT_CLIENT_ID, MQTT_CLIENT_ID_DOC, true));
        configSet.add(new ConfigData(VALUE_CONVERTER_SCHEMAS_ENABLE, VALUE_CONVERTER_SCHEMAS_ENABLE_DOC, true));
        configSet.add(new ConfigData(VALUE_CONVERTER, VALUE_CONVERTER_DOC, true));
        configSet.add(new ConfigData(KEY_CONVERTER_SCHEMAS_ENABLE, KEY_CONVERTER_SCHEMAS_ENABLE_DOC, true));
        configSet.add(new ConfigData(KEY_CONVERTER, KEY_CONVERTER_DOC, true));
        configSet.add(new ConfigData(KAFKA_SERVER, KAFKA_SERVER_DOC, true));
        configSet.add(new ConfigData(CONNECTOR_CLASS, CONNECTOR_CLASS_DOC, true));
        configSet.add(new ConfigData(KAFKA_KEY_SERIALIZER, KAFKA_KEY_SERIALIZER_DOC, true));
        configSet.add(new ConfigData(KAFKA_VALUE_SERIALIZER, KAFKA_VALUE_SERIALIZER_DOC, true));
    }
}
