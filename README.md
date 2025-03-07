# MQTTKafkaJConnector

MQTTKafkaJConnector allows you to stream data from MQTT brokers to Apache Kafka. It's designed to be robust, efficient, and easy to use, providing seamless integration between MQTT and Kafka.

## Features
* **Reliable MQTT to Kafka streaming:** Efficiently transfers messages from MQTT topics to Kafka topics.
* **Configurable MQTT connection:** Supports various MQTT connection options.
* **Configurable Kafka producer:** Allows customization of Kafka producer settings for optimal performance.
* **Message buffering:** Implements a message buffer to handle temporary network issues and improve throughput.
* **Easy configuration:** Simple and straightforward configuration options.
* **Error handling:** Robust error handling and logging for easy debugging and monitoring.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

* Apache Kafka and Kafka Connect
* MQTT Broker (e.g., Mosquitto, HiveMQ)
* Java 22 or higher
* Maven
### Installation

1.  Clone the repository:

    ```bash
    git clone https://github.com/ajambor91/mqtt-kafka-jconnector
    ```
2. Build project, go to main project directory and run:

    ```bash
    mvn clean package assembly:single```

The default build process runs unit tests. These tests require a running MQTT broker, such as Mosquitto.  
Ensure the broker is running on the default port (1883) for the tests to pass.3. 
If you don't want run any MQTT broker, you can paste below command to skip tests from building:

```bash
    mvn clean package assembly:single -DskipTests
```
3. Copy MQTTConnector.jar from the target directory into your plugins path in Kafka Connect.
#### Source Connector

Send request to http://your-kafka-connects:8083/connectors with payload: 

``` bash
        {
            "name": "your-name",
            "config": {
                "connector.class": "aj.programming.MQTTConnector.Source.MQTTSourceConnector",
                "mqtt.clientId": "your-id",
                "mqtt.topic": "your-topic",
                "kafka.topic": "your-kafka-topic",
                 "uniqueId": "id"
            }
        }
```
This is the basic required configuration. With these settings, the MQTT broker defaults to tcp://localhost:1883, and the Kafka broker to localhost:9092.
Also here is minimal recommended payload:
``` bash
        {
            "name": "your-name",
            "config": {
                "connector.class": "aj.programming.MQTTConnector.Source.MQTTSourceConnector",
                "mqtt.clientId": "your-id",
                "mqtt.topic": "your-topic",
                "mqtt.broker": "tcp://localhost:1883",
                "kafka.server": "http://localhost:9092",
                "kafka.topic": "your-kafka-topic",
                "uniqueId": "id"
            }
        }
```
And here is the complete list of configurable options:
``` bash
        {
            "name": "your-name",
            "config": {
                "connector.class": "aj.programming.MQTTConnector.Source.MQTTSourceConnector",
                "mqtt.clientId": "your-id",
                "mqtt.topic": "your-topic",
                "mqtt.keepAliveInterval": 10,
                "mqtt.broker": "tcp://localhost:1883",
                "mqtt.connectionTimeout": 10,
                "mqtt.cleanSession": false,
                "mqtt.automaticReconnect": true,
                "mqtt.qos": 2,
                "mqtt.userName": "your-username",
                "mqtt.password": "your-password",
                "kafka.server": "http://localhost:9092",
                "kafka.topic": "your-kafka-topic",
                "uniqueId": "id"
            }
        }
```
#### Sink Connector

Send request 

```bash
        {
            "name": "your-name",
            "config": {
                "connector.class": "aj.programming.MQTTConnector.Sink.MQTTSinkConnector",
                "mqtt.clientId": "your-id",
                "mqtt.topic": "your-topic",
                "kafka.topic": "your-kafka-topic",
                 "uniqueId": "id",
                 "topics": "kafka=listen-topic"
            }
        }
```
Similar as SourceConnecter is the basic required configuration. With these settings, the MQTT broker defaults to tcp://localhost:1883, and the Kafka broker to localhost:9092, but you have to add "topics" field to set Kafka listen topic.



You can also edit the config.properties file in the resources directory. This file contains the default configuration, but remember that incoming configuration will take precedence.  
Please note: Currently, the connector reliably receives messages from the MQTT broker. However, there may be issues with sending these messages to Kafka. I am actively working to resolve this problem and will provide an update as soon as possible    
Built with Kafka Connect Api v. 3.9.0 and Eclipse Paho MQTTv3 v. 1.2.5 
## Testing
Now are two the most important classes tested: MQTTSourceConnector, and MQTTSourceTask. First extends SourceConnector and second extends SourceTask, both from Kafka Connects Api.  
To run test paste below command in main package directory

```bash
    mvn test
```
Make note, now for testing you have to run MQTT broker on your local machine with default port 1883. 
## Future Enhancements

1. **Add unit and integration tests**
2. **Add TLS encryption**
3. **Add documentation**

## Contributing ##
Feel free to open issues or submit pull requests. All contributions are welcome!

## License

This project is licensed under the [MIT License](LICENSE).  
Feel free to use, modify, and distribute it as long as the terms of the license are followed.