package aj.programming.MQTTConnector.Source;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class MQTTSourceConfig extends AbstractConfig {
    private static final Logger logger = LoggerFactory.getLogger(MQTTSourceConfig.class);
    private static final String configName = "config.properties";
    private static final ConfigDefEx configDef = new ConfigDefEx();

    static {
        Properties properties = new Properties();
        try (InputStream inputStream = MQTTSourceConfig.class.getClassLoader().getResourceAsStream(MQTTSourceConfig.configName)) {
            properties.load(inputStream);
            logger.info("Loaded all config properties");
            for (ConfigData configData : MQTTConfigNames.configSet) {
                Object propertyValue = properties.get(configData.getName());
                ConfigOption configOption = null;
                if (propertyValue != null) {
                    configOption = new ConfigOption(configData, propertyValue);
                } else {
                    configOption = new ConfigOption(configData);
                }
                logger.info("Processing {} : {} property", configData.getName(), propertyValue);
                configDef.define(configOption);
            }
        } catch (IOException e) {
            logger.error("Cannot read configuration file");
            throw new RuntimeException(e);
        }
    }

    public MQTTSourceConfig(Map<?, ?> originals) {
        super(getConfig(), originals,false);
    }


    public static ConfigDef getConfig() {
        return MQTTSourceConfig.configDef;
    }
}
