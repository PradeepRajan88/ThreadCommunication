package common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *  This class holds all the constants and defaults used in the project. Can be used to read from the property file
 *  or from the default properties.
 */
public class Configuration {

    public static final String KEY_INITIAL_MESSAGE = "message.initial";
    public static final String KEY_STOP_CONDITION = "stop.condition";
    public static final String KEY_VERBOSE = "log.verbose";
    public static final String KEY_PORT_NUMBER = "websocket.port";

    public static final String KEY_STOP_CONDITION_MAX = "stop.condition.max";
    public static final String KEY_INITIAL_MESSAGE_MAX = "message.initial.max";

    public static final String DEFAULT_INITIAL_MESSAGE = "Hi";
    public static final String DEFAULT_STOP_CONDITION = "20";
    public static final String DEFAULT_STOP_CONDITION_MAX = "1000";
    public static final String DEFAULT_INITIAL_MESSAGE_MAX = "1000";
    public static final String DEFAULT_VERBOSE = "true";
    public static final String DEFAULT_PORT_NUMBER = "81";

    private static final String CONFIG_FILE_PATH = "config.properties";

    private static final Properties prop = new Properties(getDefaults());

    /**
     * Returns the value of the specified key from the property file, or failing that, from the defaults.
     * Returns null if property is not found. This method reads directly from the property file every time, so that
     * properties can be changed at runtime.
     *
     * @param key key
     * @return value as String
     */
    public static String read(String key) {
        try (InputStream input = Configuration.class.getClassLoader().getResourceAsStream(CONFIG_FILE_PATH)) {
            if (input == null) {
                System.err.println("config.properties not found.");
            } else {
                prop.load(input);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return prop.getProperty(key);
    }

    /**
     * supplies default properties that will be used if the property file is not available or if the specified key is not present in the property file.
     * Can also be directly used to bypass property file.
     * @return default properties
     */
    public static Properties getDefaults() {
        Properties defaultProp = new Properties();
        defaultProp.setProperty(KEY_INITIAL_MESSAGE, DEFAULT_INITIAL_MESSAGE);
        defaultProp.setProperty(KEY_STOP_CONDITION, DEFAULT_STOP_CONDITION);
        defaultProp.setProperty(KEY_STOP_CONDITION_MAX, DEFAULT_STOP_CONDITION_MAX);
        defaultProp.setProperty(KEY_INITIAL_MESSAGE_MAX, DEFAULT_INITIAL_MESSAGE_MAX);
        defaultProp.setProperty(KEY_VERBOSE, DEFAULT_VERBOSE);
        defaultProp.setProperty(KEY_PORT_NUMBER, DEFAULT_PORT_NUMBER);
        return defaultProp;
    }
}
