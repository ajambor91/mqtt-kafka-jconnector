package aj.programming.MQTTConnector.Source;

import aj.programming.MQTTConnector.Buffers.MessageBuffer;
import aj.programming.MQTTConnector.Config.ConfigNames;
import aj.programming.MQTTConnector.Config.MQTTConfig;
import aj.programming.MQTTConnector.TestUtils.TestConverter;
import aj.programming.MQTTConnector.TestUtils.TestFactory;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class MQTTSourceClientTest {

    private MQTTConfig mqttConfig;
    private MessageBuffer messageBuffer;
    private MQTTSourceClient mqttSourceClient;
    private MqttConnectOptions mqttConnectOptions;

    @BeforeEach
    public void setup() throws MqttException {

        this.messageBuffer = new MessageBuffer();
        this.mqttConfig = new MQTTConfig(TestConverter.convertMap(TestFactory.getConfig()));
        this.mqttSourceClient = new MQTTSourceClient(mqttConfig, messageBuffer);
    }

    @Test
    @DisplayName("Should connect and subscribe to MQTT connect")
    public void testConnectAndSubscribeToMQTTBroker() throws MqttException {
        MQTTSourceClient mqttSourceClientSpy = spy(this.mqttSourceClient);
        mqttSourceClientSpy.initialize();
        verify(mqttSourceClientSpy).connect(any(MqttConnectOptions.class));
        verify(mqttSourceClientSpy).subscribe(mqttConfig.getString(
                ConfigNames.MQTT_TOPIC), mqttConfig.getInt(ConfigNames.MQTT_QOS));
    }


}