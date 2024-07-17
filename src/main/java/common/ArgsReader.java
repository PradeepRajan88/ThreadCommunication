package common;


public class ArgsReader {


    public static int readStopCondition(String[] args, int index) {
        int stopCondition = Integer.parseInt(args.length > index ? args[index] : Configuration.read(Configuration.KEY_STOP_CONDITION));
        // validation to limit the number and thereby size of messages
        int maxStopCondition = Integer.parseInt(Configuration.read(Configuration.KEY_STOP_CONDITION_MAX));
        if (stopCondition > maxStopCondition || stopCondition < 1) {
            throw new IllegalArgumentException("Invalid stop condition. Allowed range is 1 to " + maxStopCondition);
        }
        return stopCondition;
    }

    public static boolean readVerboseLogging(String[] args, int index) {
        return Boolean.parseBoolean(args.length > index ? args[index] : Configuration.read(Configuration.KEY_VERBOSE_LOGGING));
    }

    public static String readInitialMessage(String[] args, int index) {
        String initialMessage = args.length > index ? args[index] : Configuration.read(Configuration.KEY_INITIAL_MESSAGE);
        if (initialMessage == null) {
            throw new IllegalArgumentException("Initial message cannot be null");
        }
        int maxInitialMessageLength = Integer.parseInt(Configuration.read(Configuration.KEY_INITIAL_MESSAGE_MAX));
        if (initialMessage.length() > maxInitialMessageLength) {
            throw new IllegalArgumentException("Maximum allowed length of initial message is " + maxInitialMessageLength);
        }
        return initialMessage;
    }

    public static int readPort(String[] args, int index) {
        int port = Integer.parseInt(args.length > index ? args[index] : Configuration.read(Configuration.KEY_PORT_NUMBER));
        if (port < 1 || port > 65535) {
            throw new IllegalArgumentException("Invalid port number. Valid range is 1 - 65536");
        }
        return port;
    }
}
