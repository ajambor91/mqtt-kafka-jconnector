package aj.programming.MQTTConnector.Source;

import aj.programming.MQTTConnector.Buffers.MessageBuffer;
import aj.programming.MQTTConnector.Config.MQTTConfig;
import aj.programming.MQTTConnector.TestUtils.TestConverter;
import aj.programming.MQTTConnector.TestUtils.TestFactory;
import org.apache.kafka.connect.source.SourceRecord;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class MQTTSourceTaskTest {
    private MQTTSourceTask mqttSourceTask;
    private Map<String, Object> config;
    private MQTTSourceTask mqttSourceTaskSpy;


    @BeforeEach
    public void setup() {
        this.mqttSourceTask = new MQTTSourceTask();
        this.mqttSourceTaskSpy = spy(this.mqttSourceTask);
        this.config = TestFactory.getConfig();
    }


    @Test
    @DisplayName("Should run source task")
    public void testStart() throws NoSuchFieldException, IllegalAccessException {
        mqttSourceTaskSpy.start(TestConverter.convertMap(this.config));
        Field mqttClientField = MQTTSourceTask.class.getDeclaredField("mqttClient");
        mqttClientField.setAccessible(true);
        MQTTSourceClient mqttSourceClient = (MQTTSourceClient) mqttClientField.get(mqttSourceTaskSpy);
        Field configField = MQTTSourceTask.class.getDeclaredField("config");
        configField.setAccessible(true);
        MQTTConfig mqttConfig = (MQTTConfig) configField.get(mqttSourceTaskSpy);
        Field bufferField = MQTTSourceTask.class.getDeclaredField("buffer");
        bufferField.setAccessible(true);
        MessageBuffer buffer = (MessageBuffer) bufferField.get(mqttSourceTaskSpy);
        assertThat(mqttSourceClient).isNotNull();
        assertThat(mqttConfig).isNotNull();
        assertThat(buffer).isNotNull();

    }

    @Test
    @DisplayName("Should poll message from MQTT broker, and pass it to kafka")
    public void testPoll() throws NoSuchFieldException, IllegalAccessException, InterruptedException {
        MessageBuffer messageBuffer = mock(MessageBuffer.class);
        MQTTConfig mqttConfig = new MQTTConfig(TestConverter.convertMap(TestFactory.getConfig()));
        when(messageBuffer.poll()).thenReturn(TestFactory.createTestMessageDTO());
        Field buffer = MQTTSourceTask.class.getDeclaredField("buffer");
        buffer.setAccessible(true);
        buffer.set(mqttSourceTaskSpy, messageBuffer);
        Field mqttConfigField = MQTTSourceTask.class.getDeclaredField("config");
        mqttConfigField.setAccessible(true);
        mqttConfigField.set(mqttSourceTaskSpy, mqttConfig);
        List<SourceRecord> sourceRecord = mqttSourceTaskSpy.poll();
        assertThat(sourceRecord).isNotNull();
        assertThat(sourceRecord.size()).isGreaterThan(0);
        SourceRecord record = sourceRecord.getFirst();
        String topic = (String) record.sourcePartition().get("mqttTopic");
        String kafkaTopic = record.topic();
        String parsedMessage = (String) record.value();
        assertThat("test-mqtt-topic").isEqualTo(topic);
        assertThat("testTopic").isEqualTo(kafkaTopic);
        assertThat(parsedMessage).isNotNull().isNotBlank();
    }

    @Test
    @DisplayName("Should return current app version")
    public void testVersion() {
        String version = this.mqttSourceTask.version();
        AssertionsForClassTypes.assertThat(version).isEqualTo(TestFactory.getCurrentAppVersion());
    }
}
