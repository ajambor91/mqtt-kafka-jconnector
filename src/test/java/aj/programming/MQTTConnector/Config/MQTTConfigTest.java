package aj.programming.MQTTConnector.Config;

import aj.programming.MQTTConnector.TestUtils.TestConverter;
import aj.programming.MQTTConnector.TestUtils.TestFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class MQTTConfigTest {
    private MQTTConfig mqttConfig;
    private Map<String, String> props;

    @BeforeEach
    public void setup() {
        this.props = TestConverter.convertMap(TestFactory.getConfig());
        this.mqttConfig = new MQTTConfig(this.props);
    }

    @Test
    @DisplayName("Should return int")
    public void testGetInt() {
        int result = this.mqttConfig.getInt(ConfigNames.MQTT_QOS);
        assertThat(result).isInstanceOfAny(Integer.class);
    }

    @Test
    @DisplayName("Should return boolen")
    public void testGetBoolean() {
        boolean result = this.mqttConfig.getBoolean(ConfigNames.VALUE_CONVERTER_SCHEMAS_ENABLE);
        assertThat(result).isInstanceOfAny(Boolean.class);
    }

    @Test
    @DisplayName("Should return string")
    public void testGetString() {
        String result = this.mqttConfig.getString(ConfigNames.MQTT_TOPIC);
        assertThat(result).isInstanceOfAny(String.class);
    }
}
