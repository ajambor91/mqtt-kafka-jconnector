package aj.programming.MQTTConnector.Source;

import org.apache.kafka.common.config.ConfigDef;
import org.junit.jupiter.api.BeforeEach;
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
    private Logger logger = LoggerFactory.getLogger(MQTTSourceConnectorTest.class);

    @BeforeEach
    void setup() {
        connector = new MQTTSourceConnector();
        configProps = new HashMap<>();
        configProps.put("mqtt.broker.url", "tcp://localhost:1883");
        configProps.put("mqtt.topic", "test/topic");
    }

    @Test
    void testStart() {
        connector.start(configProps);
    }

    @Test
    void testTaskClass() {
        Class<? extends org.apache.kafka.connect.connector.Task> taskClass = connector.taskClass();
        assertThat(taskClass).isEqualTo(MQTTSourceTask.class);
    }

    @Test
    void testTaskConfigs() {
        connector.start(configProps);
        List<Map<String, String>> taskConfigs = connector.taskConfigs(1);
        assertThat(taskConfigs).hasSize(1);
        assertThat(taskConfigs.get(0)).isEqualTo(configProps);
    }

    @Test
    void testStop() {
        connector.start(configProps);
        connector.stop();}

    @Test
    void testConfig() {
        ConfigDef configDef = connector.config();
        assertThat(configDef).isNotNull();
    }

    @Test
    void testVersion() {
        String version = connector.version();
        assertThat(version).isEqualTo("");
    }
}