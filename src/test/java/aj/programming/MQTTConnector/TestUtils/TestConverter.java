package aj.programming.MQTTConnector.TestUtils;

import java.util.HashMap;
import java.util.Map;

public class TestConverter {
    public static Map<String, String> convertMap(Map<String, ?> flatMap) {
        Map<String, String> newMap = new HashMap<>();
        flatMap.forEach((key, value) -> {
            newMap.put(key, String.valueOf(value));
        });
        return newMap;
    }
}
