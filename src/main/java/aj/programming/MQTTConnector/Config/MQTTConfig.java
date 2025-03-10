package aj.programming.MQTTConnector.Config;

import org.apache.kafka.common.config.AbstractConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class MQTTConfig extends AbstractConfig {
    private static final Logger logger = LoggerFactory.getLogger(MQTTConfig.class);
    private static final String configName = "config.properties";
    private static final ConfigDefEx configDef = new ConfigDefEx();

    static {
        Properties properties = new Properties();
        try (InputStream inputStream = MQTTConfig.class.getClassLoader().getResourceAsStream(MQTTConfig.configName)) {
            properties.load(inputStream);
            logger.info("Loaded all config properties");
            for (ConfigData configData : ConfigNames.configSet) {
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

    public MQTTConfig(Map<?, ?> originals) {
        super(getConfig(), originals, false);
    }

    public static ConfigDefEx getConfig() {
        return MQTTConfig.configDef;
    }

    @Override
    public String getString(String configName) {
        return String.valueOf(this.get(configName));
    }

    @Override
    public Integer getInt(String configName) {
        try {

            return Integer.parseInt(String.valueOf(this.get(configName)));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Cannot convert config value for " + configName + " to integer: " + this.originals().get(configName), e);
        }
    }

    @Override
    public Boolean getBoolean(String configName) {
        Object value = this.get(configName);
        if (value == null) {
            throw new IllegalArgumentException("Config value for " + configName + " is null");
        }
        String stringValue = value.toString();
        if (stringValue.equalsIgnoreCase("true")) {
            return true;
        } else if (stringValue.equalsIgnoreCase("false")) {
            return false;
        }
        throw new IllegalArgumentException("Cannot convert given value to boolean: " + stringValue);
    }
}
