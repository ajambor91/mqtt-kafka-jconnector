package aj.programming.MQTTConnector.Sink;

import aj.programming.MQTTConnector.Buffers.MessageBuffer;
import aj.programming.MQTTConnector.Config.MQTTConfig;
import aj.programming.MQTTConnector.DTO.MessageDTO;
import aj.programming.MQTTConnector.TestUtils.TestConverter;
import aj.programming.MQTTConnector.TestUtils.TestFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.connect.sink.SinkRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

public class MQTTSinkTaskTest {

    private MQTTSinkTask mqttSinkTask;
    private MQTTConfig mqttConfig;
    private Map<String, String> properties;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        this.properties = TestConverter.convertMap(TestFactory.getConfig());
        this.mqttConfig = new MQTTConfig(properties);
        this.mqttSinkTask = spy(MQTTSinkTask.class);
        this.objectMapper = mock(ObjectMapper.class);
        Field field = MQTTSinkTask.class.getDeclaredField("objectMapper");
        field.setAccessible(true);
        field.set(this.mqttSinkTask, this.objectMapper);
    }

    @Test
    @DisplayName("Should start sink task")
    public void testStart() {
        this.mqttSinkTask.start(properties);
    }

    @Test
    @DisplayName("Should sink message from Kafka and add to buffer")
    public void testPut() throws IOException, NoSuchFieldException, IllegalAccessException {
        MessageDTO testMessage = TestFactory.createTestMessageDTO();
        MessageBuffer messageBufferMock = mockMessageBuffer();
        Collection<SinkRecord> records = createSinkRecords();
        when(this.objectMapper.readValue(anyString(), (Class<Object>) any())).thenReturn(testMessage);
        this.mqttSinkTask.put(records);
        ArgumentCaptor<MessageDTO> argumentCaptor = ArgumentCaptor.forClass(MessageDTO.class);
        verify(messageBufferMock).addMessage(argumentCaptor.capture());
        MessageDTO messageCaptured = argumentCaptor.getValue();
        assertThat(messageCaptured.getMessageId()).isEqualTo(testMessage.getMessageId());

    }

    @Test
    @DisplayName("Should do nothing when queue is empty")
    public void testPutWhenQueueIsEmpty() throws NoSuchFieldException, IllegalAccessException {
        Field messageBufferField = MQTTSinkTask.class.getDeclaredField("sinkMessageBuffer");
        messageBufferField.setAccessible(true);
        MessageBuffer messageBufferMock = mock(MessageBuffer.class);
        messageBufferField.set(this.mqttSinkTask, messageBufferMock);
        Collection<SinkRecord> records = new HashSet<>();
        this.mqttSinkTask.put(records);
        verify(messageBufferMock, never()).addMessage(any(MessageDTO.class));

    }

    @Test
    @DisplayName("Should doesn't break app when json is invalid, and log error")
    public void testShouldNotBreakAppOnInvalidMessage() throws NoSuchFieldException, IllegalAccessException, JsonProcessingException {
        MessageDTO testMessage = TestFactory.createTestMessageDTO();
        Logger logger = mock(Logger.class);
        Field loggerField = MQTTSinkTask.class.getDeclaredField("logger");
        loggerField.setAccessible(true);
        loggerField.set(this.mqttSinkTask, logger);
        Collection<SinkRecord> records = createSinkRecords();
        when(this.objectMapper.readValue(anyString(), (Class<Object>) any())).thenThrow(JsonProcessingException.class);
        this.mqttSinkTask.put(records);
        verify(logger).info(anyString(), isNull(), anyLong(), anyString());
        verify(logger).error(eq("Error converting incoming Kafka Message, to MessageDTO"), isA(JsonProcessingException.class));
    }


    @Test
    @DisplayName("Should stop task")
    public void testStop() throws NoSuchFieldException, IllegalAccessException {
        MQTTSinkClient mqttSinkClient = mock(MQTTSinkClient.class);
        Field clientField = MQTTSinkTask.class.getDeclaredField("mqttSinkClient");
        clientField.setAccessible(true);
        clientField.set(this.mqttSinkTask, mqttSinkClient);
        this.mqttSinkTask.stop();
        verify(mqttSinkClient).stop();
    }

    @Test
    @DisplayName("Should return current app version")
    public void testVersion() {
        String version = this.mqttSinkTask.version();
        assertThat(version).isEqualTo(TestFactory.getCurrentAppVersion());
    }

    private Collection<SinkRecord> createSinkRecords() {
        SinkRecord sinkRecord = mock(SinkRecord.class);
        ConsumerRecord connectRecord = mock(ConsumerRecord.class);
        when(connectRecord.toString()).thenReturn("Message");
        when(sinkRecord.value()).thenReturn(connectRecord);
        Collection<SinkRecord> records = new HashSet<>();
        records.add(sinkRecord);
        return records;
    }

    private MessageBuffer mockMessageBuffer() throws NoSuchFieldException, IllegalAccessException {
        Field messageBufferField = MQTTSinkTask.class.getDeclaredField("sinkMessageBuffer");
        messageBufferField.setAccessible(true);
        MessageBuffer messageBufferMock = mock(MessageBuffer.class);
        messageBufferField.set(this.mqttSinkTask, messageBufferMock);
        return messageBufferMock;
    }
}