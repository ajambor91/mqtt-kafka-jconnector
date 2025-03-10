package aj.programming.MQTTConnector.Sink;

import aj.programming.MQTTConnector.Buffers.MessageBuffer;
import aj.programming.MQTTConnector.Config.MQTTConfig;
import aj.programming.MQTTConnector.Helpers.MqttConnectOptionsHelper;
import aj.programming.MQTTConnector.TestUtils.TestConverter;
import aj.programming.MQTTConnector.TestUtils.TestFactory;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.lang.reflect.Field;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

public class MQTTSinkClientTest {

    private MQTTSinkClient mqttSinkClient;
    private MQTTConfig mqttConfig;
    private MessageBuffer messageBuffer;

    @BeforeEach
    public void setup() throws MqttException {
        this.messageBuffer = mock(MessageBuffer.class);
        this.mqttConfig = new MQTTConfig(TestConverter.convertMap(TestFactory.getConfig()));
        MQTTSinkClient sinkClient = new MQTTSinkClient(this.mqttConfig, this.messageBuffer);
        this.mqttSinkClient = spy(sinkClient);
    }

    @Test
    @DisplayName("Should connect to MQTT broker")
    public void testConnect() throws MqttException {
        MqttConnectOptions mqttConnectOptions = MqttConnectOptionsHelper.getOptions(this.mqttConfig);
        ArgumentCaptor<MqttConnectOptions> captor = ArgumentCaptor.forClass(MqttConnectOptions.class);
        this.mqttSinkClient.connect();
        verify(this.mqttSinkClient).connect(captor.capture());
        MqttConnectOptions capturedOptions = captor.getValue();
        assertThat(capturedOptions).isNotNull();
        assertThat(capturedOptions.getConnectionTimeout()).isEqualTo(mqttConnectOptions.getConnectionTimeout());
        assertThat(capturedOptions.getDebug()).isEqualTo(mqttConnectOptions.getDebug());
        assertThat(capturedOptions.getPassword()).isEqualTo(mqttConnectOptions.getPassword());
        assertThat(capturedOptions.getUserName()).isEqualTo(mqttConnectOptions.getUserName());
        assertThat(capturedOptions.getSocketFactory()).isEqualTo(mqttConnectOptions.getSocketFactory());
        assertThat(capturedOptions.isCleanSession()).isEqualTo(mqttConnectOptions.isCleanSession());
        assertThat(capturedOptions.getMqttVersion()).isEqualTo(mqttConnectOptions.getMqttVersion());
    }

    @Test
    @DisplayName("Should stop publisher")
    public void testStop() throws NoSuchFieldException, IllegalAccessException, MqttException {
        Field publisherField = MQTTSinkClient.class.getDeclaredField("mqttPublisher");
        publisherField.setAccessible(true);
        MQTTPublisherRunnable publisher = (MQTTPublisherRunnable) spy(publisherField.get(this.mqttSinkClient));
        publisherField.set(this.mqttSinkClient, publisher);
        Field mqttPublisherThreadField = MQTTSinkClient.class.getDeclaredField("mqttPublisherThread");
        mqttPublisherThreadField.setAccessible(true);
        Thread thread = mock(Thread.class);
        mqttPublisherThreadField.set(this.mqttSinkClient, thread);
        this.mqttSinkClient.stop();
        verify(publisher).stop();
        verify(thread).interrupt();
        verify(this.mqttSinkClient).disconnect();

    }

    @Test
    @DisplayName("Should don't stop publisher, when publisher is null")
    public void testStopNotStopWHenPublisherNull() throws NoSuchFieldException, IllegalAccessException, MqttException {
        Field publisherField = MQTTSinkClient.class.getDeclaredField("mqttPublisher");
        publisherField.setAccessible(true);
        publisherField.set(this.mqttSinkClient, null);
        Field mqttPublisherThreadField = MQTTSinkClient.class.getDeclaredField("mqttPublisherThread");
        mqttPublisherThreadField.setAccessible(true);
        Thread thread = mock(Thread.class);
        mqttPublisherThreadField.set(this.mqttSinkClient, thread);
        this.mqttSinkClient.stop();
        verify(thread, never()).interrupt();
        verify(this.mqttSinkClient).disconnect();
    }


}
