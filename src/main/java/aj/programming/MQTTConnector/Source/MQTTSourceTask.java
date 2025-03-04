package aj.programming.MQTTConnector.Source;

import aj.programming.MQTTConnector.Buffers.SourceMessageBuffer;
import aj.programming.MQTTConnector.Kafka.Publisher;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MQTTSourceTask extends SourceTask  {
    private MQTTSourceConfig config;
    private Publisher publisher;
    private MQTTSourceClient mqttClient;
    private SourceMessageBuffer buffer;
    private final Logger logger = LoggerFactory.getLogger(MQTTSourceTask.class);

    @Override
    public String version() {
        return "";
    }

    @Override
    public void start(Map<String, String> map) {
        this.buffer = new SourceMessageBuffer();
        this.logger.info("New task started");
        this.config = new MQTTSourceConfig(map);
        try {
            this.logger.info("Creating MQTTSourceClient");
            this.publisher = new Publisher(this.config, this.buffer);
            this.publisher.start();
            this.mqttClient = new MQTTSourceClient(this.config,this.publisher, this.buffer, this.config.getString(MQTTConfigNames.BROKER), this.config.getString(MQTTConfigNames.CLIENTID));

        } catch (MqttException e) {
            this.logger.error("Cannot start MQTTSourceCLient");

            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SourceRecord> poll() throws InterruptedException {

        return new ArrayList<>();
    }

    @Override
    public void stop() {
        this.logger.info("Task stopped");

    }

}
