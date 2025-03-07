package aj.programming.MQTTConnector.Config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Version {
    private static final String versionNumberName = "app.version";
    private static final Logger logger = LoggerFactory.getLogger(Version.class);
    private static final String versionName = "version.properties";
    private static Properties versionProperties;

    static {
        Version.versionProperties = new Properties();
        try (InputStream inputStream = Version.class.getClassLoader().getResourceAsStream(Version.versionName)) {
            versionProperties.load(inputStream);
            logger.info("Loaded version properties");
            System.out.println("Application version is: " + Version.getAppVersion());

        } catch (IOException e) {
            logger.error("Cannot read version file");
            throw new RuntimeException(e);
        }
    }

    private Version() {
    }

    public static String getAppVersion() {
        return Version.versionProperties.getProperty(Version.versionNumberName);
    }
}
