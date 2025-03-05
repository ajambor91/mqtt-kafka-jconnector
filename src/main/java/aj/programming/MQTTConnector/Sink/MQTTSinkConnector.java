package aj.programming.MQTTConnector.Sink;

import aj.programming.MQTTConnector.Config.MQTTConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.sink.SinkConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class MQTTSinkConnector extends SinkConnector {
    private Logger logger = LoggerFactory.getLogger(MQTTSinkConnector.class);
    private Map<String, String> configProps;

    @Override
    public void start(Map<String, String> props) {
        this.logger.info("Started MQTTSourceConnector with values = {}", props);
        this.configProps = Collections.unmodifiableMap(props);
    }

    @Override
    public Class<? extends Task> taskClass() {
        this.logger.info("Get task");

        return MQTTSinkTask.class;
    }

    @Override
    public List<Map<String, String>> taskConfigs(int maxTasks) {
        List<Map<String, String>> taskConfigs = new ArrayList<>(1);
        taskConfigs.add(new HashMap<>(this.configProps));
        this.logger.info("Received task");
        return taskConfigs;
    }

    @Override
    public void stop() {
        this.logger.info("Stopped MQTTSinkConnector");
    }

    @Override
    public ConfigDef config() {
        return MQTTConfig.getConfig();
    }

    @Override
    public String version() {
        return "";
    }
}
