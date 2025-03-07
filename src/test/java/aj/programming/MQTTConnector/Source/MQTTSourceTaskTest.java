package aj.programming.MQTTConnector.Source;

import aj.programming.MQTTConnector.Buffers.MessageBuffer;
import aj.programming.MQTTConnector.Config.MQTTConfig;
import aj.programming.MQTTConnector.TestUtils.TestConverter;
import aj.programming.MQTTConnector.TestUtils.TestFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;
import java.util.Map;

import static org.mockito.Mockito.spy;

public class MQTTSourceTaskTest {

    private MQTTSourceTask mqttSourceTask;
    private Map<String, Object> config;



    @BeforeEach
    public void setup() {
        this.mqttSourceTask = new MQTTSourceTask();
        this.config = TestFactory.getConfig();
    }



    @Test
    public void testStart() throws NoSuchFieldException, IllegalAccessException {
        MQTTSourceTask mqttSourceTaskSpy = spy(this.mqttSourceTask);
        mqttSourceTaskSpy.start(TestConverter.convertMap(this.config));
        Field mqttClientField = MQTTSourceTask.class.getDeclaredField("mqttClient");
        mqttClientField.setAccessible(true);
        MQTTSourceClient mqttSourceClient = (MQTTSourceClient) mqttClientField.get(mqttSourceTaskSpy);

        Field configField = MQTTSourceTask.class.getDeclaredField("config");
        configField.setAccessible(true);
        MQTTConfig mqttConfig = (MQTTConfig)  configField.get(mqttSourceTaskSpy);
        Field bufferField = MQTTSourceTask.class.getDeclaredField("buffer");
        bufferField.setAccessible(true);
        MessageBuffer buffer = (MessageBuffer) bufferField.get(mqttSourceTaskSpy);
        assertThat(mqttSourceClient).isNotNull();
        assertThat(mqttConfig).isNotNull();
        assertThat(buffer).isNotNull();

    }

}
