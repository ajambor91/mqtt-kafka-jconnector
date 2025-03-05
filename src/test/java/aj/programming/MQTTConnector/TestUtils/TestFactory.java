package aj.programming.MQTTConnector.TestUtils;

import java.util.HashMap;
import java.util.Map;

public class TestFactory {

    public static Map<String, Object> getConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("mqtt.broker", "tcp://localhost:1883");
        config.put("mqtt.clientId", "testClientId");
        config.put("mqtt.automaticReconnect", true);
        config.put("mqtt.keepAliveInterval", 0);
        config.put("mqtt.cleanSession", false);
        config.put("mqtt.connectionTimeout", 5);
        config.put("kafka.topic", "testTopic");
        config.put("mqtt.topic", "test-mqtt-topic");
        config.put("mqtt.qos", 0);
        config.put("kafka.server", "localhost:9092");
        config.put("value.converter.schemas.enable", false);
        config.put("value.converter", "org.apache.kafka.connect.storage.StringConverter");
        config.put("key.converter.schemas.enable", false);
        config.put("key.converter", "org.apache.kafka.connect.storage.StringConverter");
        config.put("kafka.key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("kafka.value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("connector.class", "aj.programming.MQTTConnector.Source.MQTTSourceConnector");
        config.put("mqtt.userName", "");
        config.put("mqtt.password", "");
        config.put("uniqueId", "TEST_ID");
        return config;
    }

    public static Map<String, Object> getConfigWithCredntials() {
        Map<String, Object> configWithCredentials = TestFactory.getConfig();
        configWithCredentials.putAll(Map.of(
                "mqtt.userName", "testUsername",
                "mqtt.password", "testPass"
        ));
        return configWithCredentials;
    }
}
