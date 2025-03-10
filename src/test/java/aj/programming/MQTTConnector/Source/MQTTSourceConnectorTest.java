package aj.programming.MQTTConnector.Source;

import aj.programming.MQTTConnector.Config.Version;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class MQTTSourceConnectorTest {

    private MQTTSourceConnector connector;
    private Map<String, String> configProps;
    private final Logger logger = LoggerFactory.getLogger(MQTTSourceConnectorTest.class);

    @BeforeEach
    void setup() {
        connector = new MQTTSourceConnector();
        configProps = new HashMap<>();
        configProps.put("mqtt.broker.url", "tcp://localhost:1883");
        configProps.put("mqtt.topic", "test/topic");
    }

    @Test
    @DisplayName("Should start MQTT source connector task")
    void testStart() {
        connector.start(configProps);
    }

    @Test
    @DisplayName("Should return mqtt source task")
    void testTaskClass() {
        Class<? extends Task> taskClass = connector.taskClass();
        assertThat(taskClass).isEqualTo(MQTTSourceTask.class);
    }

    @Test
    @DisplayName("Return MQTT source connector task config")
    void testTaskConfigs() {
        connector.start(configProps);
        List<Map<String, String>> taskConfigs = connector.taskConfigs(1);
        assertThat(taskConfigs).hasSize(1);
        assertThat(taskConfigs.get(0)).isEqualTo(configProps);
    }

    @Test
    @DisplayName("Should stop MQTT source connector")
    void testStop() {
        connector.start(configProps);
        connector.stop();
    }

    @Test
    @DisplayName("Should return current config ")
    void testConfig() {
        ConfigDef configDef = connector.config();
        assertThat(configDef).isNotNull();
    }

    @Test
    @DisplayName("Should return current application version")
    void testVersion() {
        String version = connector.version();
        assertThat(version).isEqualTo(Version.getAppVersion());
    }
}