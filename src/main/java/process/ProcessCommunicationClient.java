package process;

import common.ArgsReader;
import common.Stats;
import process.player.WebSocketPlayer;

/**
 * This class contains the main method to be run the WebSocketPlayer instance
 * <p>
 * Important Note -1. Server must run before Client o avoid 'Connection refused' exception
 * Important Note -2. the port number supplied to the server and client must be the same for communication to work
 * Important Note -3. the stopCondition supplied to the server and client must be the same for both to stop gracefully.
 */
public class ProcessCommunicationClient {

    /**
     * Run this only after ProcessCommunicationServer#main to execute communication between two player instances each running in a separate process
     *
     * @param args if args are not supplied, defaults will be used.
     *             args[0] -> stopCondition (int, number of messages that each player should send as well as receive)
     *             args[1] -> verboseLogging (boolean, whether the players should log their actions to the console)
     *             args[2] -> port (int, port number to use)
     */
    public static void main(String[] args) {

        int stopCondition = ArgsReader.readStopCondition(args, 0);
        boolean verboseLogging = ArgsReader.readVerboseLogging(args, 1);
        int port = ArgsReader.readPort(args, 2);
        System.out.println("running ClientPlayer with stopCondition: " + stopCondition + ", verboseLogging: " + verboseLogging + ", port: " + port);

        play(stopCondition, verboseLogging, port);
    }

    public static Stats play(final int stopCondition, boolean verboseLogging, int port) {

        WebSocketPlayer player = new WebSocketPlayer("WebSocketClient", stopCondition, verboseLogging, port);
        player.play();
        player.printStats();

        Stats stats = new Stats();
        stats.setPlayerSentMessagesCount(player.getSentMessagesCount());
        stats.setPlayerReceivedMessagesCount(player.getReceivedMessagesCount());
        return stats;
    }
}
