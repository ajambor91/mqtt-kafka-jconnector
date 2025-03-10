package aj.programming.MQTTConnector.Sink;

import aj.programming.MQTTConnector.TestUtils.TestConverter;
import aj.programming.MQTTConnector.TestUtils.TestFactory;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.spy;

public class MQTTSinkConnectorTest {


    private MQTTSinkConnector mqttSinkConnector;

    private Map<String, String> properties;

    @BeforeEach
    public void setup() {
        this.mqttSinkConnector = spy(new MQTTSinkConnector());
        this.properties = TestConverter.convertMap(TestFactory.getConfig());
    }

    @Test
    @DisplayName("Should start sink connector")
    public void testStart() {
        this.mqttSinkConnector.start(this.properties);
    }

    @Test
    @DisplayName("Should return sink task")
    public void testTaskClass() {
        Class<? extends Task> mqttSinkTask = this.mqttSinkConnector.taskClass();
        assertThat(mqttSinkTask).isEqualTo(MQTTSinkTask.class);
    }

    @Test
    @DisplayName("Shoold get task config")
    public void testTaskConfigs() throws NoSuchFieldException, IllegalAccessException {
        Field propsField = MQTTSinkConnector.class.getDeclaredField("configProps");
        propsField.setAccessible(true);
        propsField.set(this.mqttSinkConnector, this.properties);
        List<Map<String, String>> tasks = this.mqttSinkConnector.taskConfigs(1);
        assertThat(tasks.size()).isGreaterThan(0);
        Map<String, String> taskProps = tasks.getFirst();
        assertThat(taskProps).isEqualTo(this.properties);
    }

    @Test
    @DisplayName("Should stop sink connector")
    public void testStopSinkConnector() {
        this.mqttSinkConnector.stop();
    }

    @Test
    @DisplayName("Should return sink connector config")
    public void testGetSinkConnectorConfig() {
        ConfigDef configDef = this.mqttSinkConnector.config();
        assertThat(configDef).isNotNull();

    }

    @Test
    @DisplayName("Should return current app version")
    public void testVersion() {
        String version = this.mqttSinkConnector.version();
        assertThat(version).isEqualTo(TestFactory.getCurrentAppVersion());
    }

}
