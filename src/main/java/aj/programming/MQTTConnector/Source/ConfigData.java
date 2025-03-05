package aj.programming.MQTTConnector.Source;

import org.apache.kafka.common.config.ConfigDef;

public class ConfigData {
    private final String name;
    private final String doc;
    private final ConfigDef.Type type;
    private final ConfigDef.Importance importance;
    private final boolean isRequired;

    public ConfigData(String name, String doc, ConfigDef.Type type, ConfigDef.Importance importance, boolean isRequired) {
        this.name = name;
        this.doc = doc;
        this.type = type;
        this.importance = importance;
        this.isRequired = isRequired;
    }

    public ConfigData(String name, String doc, ConfigDef.Type type, ConfigDef.Importance importance) {
        this(name, doc, type, importance, false);
    }

    public ConfigData(String name, String doc) {
        this(name, doc, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH);
    }

    public ConfigData(String name, String doc, boolean isRequired) {
        this(name, doc, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, isRequired);
    }

    public ConfigData(String name, String doc, ConfigDef.Importance importance, boolean isRequired) {
        this(name, doc, ConfigDef.Type.STRING, importance, isRequired);
    }

    public ConfigData(String name, String doc, ConfigDef.Importance importance) {
        this(name, doc, ConfigDef.Type.STRING, importance);
    }

    public boolean getIsRequired() {
        return this.isRequired;
    }


    public String getName() {
        return name;
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
}
