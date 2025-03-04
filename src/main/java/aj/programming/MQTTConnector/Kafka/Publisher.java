package aj.programming.MQTTConnector.Kafka;

import aj.programming.MQTTConnector.Buffers.SourceMessageBuffer;
import aj.programming.MQTTConnector.Source.MQTTConfigNames;
import aj.programming.MQTTConnector.Source.MQTTSourceConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class Publisher extends Thread {
    private volatile boolean running;
    private final MQTTSourceConfig config;
    private final Logger logger;
    private final Producer<String, String> producer;
    private final SourceMessageBuffer buffer;
    public Publisher(MQTTSourceConfig mqttSourceConfig, SourceMessageBuffer buffer) {
        this.logger = LoggerFactory.getLogger(Publisher.class);
        this.running = true;
        this.config = mqttSourceConfig;
        this.buffer =buffer;
        Properties properties = new Properties();
        properties.put("bootstrap.servers", mqttSourceConfig.getString(MQTTConfigNames.KAFKA_SERVER));
        properties.put("key.serializer", mqttSourceConfig.getString(MQTTConfigNames.KAFKA_KEY_SERIALIZER));
        properties.put("value.serializer", mqttSourceConfig.getString(MQTTConfigNames.KAFKA_VALUE_SERIALIZER));
        this.producer = new KafkaProducer<>(properties);
    }

    @Override
    public void run() {
        while (this.running){
            try {
                this.logger.info("Loop started");
                String message = this.buffer.poll();
                if (message != null) {
                    String topic = this.config.getString(MQTTConfigNames.KAFKA_TOPIC);
                    this.logger.info("Buffer not empty, processing message: {} on kafka topic: {}", message, topic);
                    ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, message);
                    this.producer.send(producerRecord);
                    this.logger.info("Kafka message: {} on topic: {} was send", message, topic);
                    sleep(1);
                } else {
                    synchronized (this) {
                        this.wait();
                    }
                }
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }
        }
    }
}
