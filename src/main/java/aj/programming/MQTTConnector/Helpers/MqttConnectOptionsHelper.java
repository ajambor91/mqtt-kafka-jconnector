package aj.programming.MQTTConnector.Helpers;

import aj.programming.MQTTConnector.Config.ConfigNames;
import aj.programming.MQTTConnector.Config.MQTTConfig;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

public class MqttConnectOptionsHelper {

    public static MqttConnectOptions getOptions(MQTTConfig mqttConfig) {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        MqttConnectOptionsHelper.addBasicMQTTOptions(mqttConnectOptions, mqttConfig);
        MqttConnectOptionsHelper.addCredentialsIfExists(mqttConnectOptions, mqttConfig);
        return mqttConnectOptions;
    }

    private static void addBasicMQTTOptions(MqttConnectOptions mqttConnectOptions, MQTTConfig mqttConfig) {
        mqttConnectOptions.setConnectionTimeout(mqttConfig.getInt(ConfigNames.MQTT_CONNECTIONTIMEOUT));
        mqttConnectOptions.setCleanSession(mqttConfig.getBoolean(ConfigNames.MQTT_CLEANSESSION));
        mqttConnectOptions.setKeepAliveInterval(mqttConfig.getInt(ConfigNames.MQTT_KEEPALIVEINTERVAL));
        mqttConnectOptions.setAutomaticReconnect(mqttConfig.getBoolean(ConfigNames.MQTT_ARC));
    }

    private static void addCredentialsIfExists(MqttConnectOptions mqttConnectOptions, MQTTConfig mqttConfig) {

        if (!mqttConfig.getString(ConfigNames.MQTT_USERNAME).isEmpty()
                && !mqttConfig.getString(ConfigNames.MQTT_PASSWORD).isEmpty()) {
            mqttConnectOptions.setUserName(mqttConfig.getString(ConfigNames.MQTT_USERNAME));
            mqttConnectOptions.setPassword(mqttConfig.getString(ConfigNames.MQTT_PASSWORD).toCharArray());
        }
    }
}
