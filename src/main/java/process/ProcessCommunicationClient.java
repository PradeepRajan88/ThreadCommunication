package process;

import common.Configuration;
import process.player.WebSocketPlayer;

/**
 *  This class contains the main method to be run the WebSocketPlayer instance
 *
 *  Important Note -1. Server must run before Client o avoid 'Connection refused' exception
 *  Important Note -2. the port number supplied to the server and client must be the same for communication to work
 *  Important Note -3. the stopCondition supplied to the server and client must be the same for both to stop gracefully.
 *
 */
public class ProcessCommunicationClient {

    /**
     * Run this only after ProcessCommunicationServer#main to execute communication between two player instances each running in a separate process
     *
     * @param args if args are not supplied, defaults will be used.
     *   args[0] -> stopCondition (int, number of messages that each player should send as well as receive)
     *   args[1] -> verbose (boolean, whether the players should log their actions to the console)
     *   args[2] -> port (int, port number to use)
     */
    public static void main(String args[]) {

        int stopCondition = Integer.parseInt(args.length > 0 ? args[0] : Configuration.read(Configuration.KEY_STOP_CONDITION));
        boolean verbose = Boolean.parseBoolean(args.length > 1 ? args[1] : Configuration.read(Configuration.KEY_VERBOSE));
        int port = Integer.parseInt(args.length > 2 ? args[2] : Configuration.read(Configuration.KEY_PORT_NUMBER));
        System.out.println("running ClientPlayer with stopCondition: " + stopCondition + ", verbose: " + verbose + ", port: " + port);

        validateAndPlay(stopCondition, verbose, port);
    }

    public static int[] validateAndPlay(final int stopCondition, boolean verbose, int port) {
        // validation to limit the number and thereby size of messages
        int maxStopCondition = Integer.parseInt(Configuration.read(Configuration.KEY_STOP_CONDITION_MAX));
        if (stopCondition > maxStopCondition || stopCondition < 1) {
            throw new IllegalArgumentException("Invalid stop condition. Allowed range is 1 to " + maxStopCondition);
        }

        WebSocketPlayer clientPlayer = new WebSocketPlayer("WebSocketClient", stopCondition, verbose, port);
        clientPlayer.play();
        clientPlayer.printStats();

        int[] stats = new int[2];
        stats[0] = clientPlayer.getSentMessagesCount();
        stats[1] = clientPlayer.getReceivedMessagesCount();
        return stats;
    }
}
