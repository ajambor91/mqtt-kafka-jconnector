package aj.programming.MQTTConnector.Source;

import aj.programming.MQTTConnector.Config.MQTTConfig;
import aj.programming.MQTTConnector.Config.Version;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.source.SourceConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class MQTTSourceConnector extends SourceConnector {
    private final Logger logger = LoggerFactory.getLogger(MQTTSourceConnector.class);
    private Map<String, String> configProps;

    @Override
    public void start(Map<String, String> map) {
        this.logger.info("Started MQTTSourceConnector with values = {}", map);
        this.configProps = Collections.unmodifiableMap(map);
    }

    @Override
    public Class<? extends Task> taskClass() {
        this.logger.info("Get task");
        return MQTTSourceTask.class;
    }

    @Override
    public List<Map<String, String>> taskConfigs(int i) {
        List<Map<String, String>> taskConfigs = new ArrayList<>(1);
        taskConfigs.add(new HashMap<>(this.configProps));
        this.logger.info("Received task");
        return taskConfigs;
    }

    @Override
    public void stop() {
        this.logger.info("Stopped MQTTSourceConnector");

    }

    @Override
    public ConfigDef config() {
        return MQTTConfig.getConfig();
    }

    @Override
    public String version() {
        String appVersion = Version.getAppVersion();
        if (appVersion == null || appVersion.isEmpty()) {
            appVersion = "Unknown application version";
            logger.warn("Cannot read application version");
        }

        return appVersion;
    }
}
