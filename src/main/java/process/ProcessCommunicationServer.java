package process;

import common.Configuration;
import process.player.WebSocketServer;

/**
 *  This class contains the main method to be run the WebSocketServer instance (Initiator)
 *
 *  Important Note -1. Server must run before Client o avoid 'Connection refused' exception
 *  Important Note -2. the port number supplied to the server and client must be the same for communication to work
 *  Important Note -3. the stopCondition supplied to the server and client must be the same for both to stop gracefully.
 *
 */
public class ProcessCommunicationServer {

    /**
     * Run this followed by ProcessCommunicationClient#main to execute communication between two player instances each running in a separate process
     *
     * @param args if args are not supplied, defaults will be used.
     *   args[0] -> stopCondition (int, number of messages that each player should send as well as receive)
     *   args[1] -> verbose (boolean, whether the players should log their actions to the console)
     *   args[2] -> port (int, port number to use)
     *   args[3] -> initialMessage (String, the very first message that the Server will send)
     */
    public static void main(String args[]) {

        int stopCondition = Integer.parseInt(args.length > 0 ? args[0] : Configuration.read(Configuration.KEY_STOP_CONDITION));
        boolean verbose = Boolean.parseBoolean(args.length > 1 ? args[1] : Configuration.read(Configuration.KEY_VERBOSE));
        int port = Integer.parseInt(args.length > 2 ? args[2] : Configuration.read(Configuration.KEY_PORT_NUMBER));
        String initialMessage = args.length > 3 ? args[3] : Configuration.read(Configuration.KEY_INITIAL_MESSAGE);
        System.out.println("running ServerPlayer with stopCondition: " + stopCondition + ", verbose: " + verbose + ", port: " + port+ ", initialMessage: " + initialMessage);

        validateAndPlay(stopCondition, verbose, port, initialMessage);
    }

    public static int[] validateAndPlay(int stopCondition, boolean verbose, int port, String initialMessage) {
        // validation to limit the number and thereby size of messages
        int maxStopCondition = Integer.parseInt(Configuration.read(Configuration.KEY_STOP_CONDITION_MAX));
        if (stopCondition > maxStopCondition || stopCondition < 1) {
            throw new IllegalArgumentException("Invalid stop condition. Allowed range is 1 to " + maxStopCondition);
        }
        // null-check initialMessage
        if (initialMessage == null) {
            throw new IllegalArgumentException("Initial message cannot be null");
        }
        // validation to limit the size of messages
        int maxInitialMessageLength = Integer.parseInt(Configuration.read(Configuration.KEY_INITIAL_MESSAGE_MAX));
        if (initialMessage.length() > maxInitialMessageLength) {
            throw new IllegalArgumentException("Maximum allowed length of initial message is " + maxInitialMessageLength);
        }

        WebSocketServer serverPlayer = new WebSocketServer("WebSocketServer", stopCondition, verbose, port, initialMessage);
        serverPlayer.play();
        serverPlayer.printStats();

        int[] stats = new int[2];
        stats[0] = serverPlayer.getSentMessagesCount();
        stats[1] = serverPlayer.getReceivedMessagesCount();
        return stats;
    }
}
