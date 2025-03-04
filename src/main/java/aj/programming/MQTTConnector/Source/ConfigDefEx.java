package aj.programming.MQTTConnector.Source;

import org.apache.kafka.common.config.ConfigDef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.NoSuchElementException;

public class ConfigDefEx extends ConfigDef {
    private final Logger logger = LoggerFactory.getLogger(ConfigDefEx.class);

    public void define(ConfigOption configOption) {
        try {
            logger.info("Adding new option with value to config, option name = {}, option value = {}", configOption.getName(), configOption.getValue());
            this.define(
                    configOption.getName(),
                    configOption.getType(),
                    configOption.getValue(),
                    configOption.getImportance(),
                    configOption.getDoc()
            );
            logger.info("Option {} : {} added", configOption.getName(), configOption.getValue());

        } catch (NoSuchElementException e) {
            logger.info("Adding new option  to config, option name = {}", configOption.getName());
            this.define(
                    configOption.getName(),
                    configOption.getType(),
                    configOption.getImportance(),
                    configOption.getDoc()
            );
            this.logger.info("Option {} added", configOption.getName());
        }

    }
}
