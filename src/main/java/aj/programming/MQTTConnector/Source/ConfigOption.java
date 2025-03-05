package aj.programming.MQTTConnector.Source;

import org.apache.kafka.common.config.ConfigDef;

import java.util.NoSuchElementException;

public final class ConfigOption {
    private final String name;
    private final String doc;
    private final ConfigDef.Type type;
    private final ConfigDef.Importance importance;
    private Object value;
    private final boolean isRequired;

    public ConfigOption(ConfigData configData) {
        this.type = configData.getType();
        this.doc = configData.getDoc();
        this.name = configData.getName();
        this.isRequired = configData.getIsRequired();
        this.importance = configData.getImportance();
        if (!configData.getIsRequired()) {
            this.value = "";
        }

    }

    public ConfigOption(ConfigData configData, Object value) {
        this(configData);
        this.setValue(configData, value);
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        if (value == null) {
            throw new NoSuchElementException("Value is null");
        }
        return value;
    }

    public String getDoc() {
        return doc;
    }

    public ConfigDef.Type getType() {
        return type;
    }

    public ConfigDef.Importance getImportance() {
        return importance;
    }

    private void setValue(ConfigData configData, Object value) {
        if (configData.getIsRequired() && (value == null || value.equals(""))) {
            throw new NoSuchElementException("Cannot find required config value: " + configData.getName());
        } else if (!configData.getIsRequired() && value == null) {
            this.value = "";
        } else if (configData.getType().equals(ConfigDef.Type.BOOLEAN)) {
            this.value = this.parseBoolValue(value);
        } else if (configData.getType().equals(ConfigDef.Type.INT)) {
            this.value = this.parseIntValue(value);
        } else if (configData.getType().equals(ConfigDef.Type.STRING)) {
            this.value = this.parseStringValue(value);
        }
    }


    private boolean parseBoolValue(Object value) {
        return value.toString().equalsIgnoreCase("true");
    }

    private int parseIntValue(Object value) {
        try {
            return Integer.parseInt(value.toString());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid config value type, int expected");
        }
    }

    private String parseStringValue(Object value) {
        try {
            return (String) value;
        } catch (ClassCastException classCastException) {
            throw new IllegalArgumentException("Invalid config value type, string expected");
        }
    }
}
