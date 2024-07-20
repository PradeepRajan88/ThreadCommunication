package process;

import common.ArgsReader;
import common.Stats;
import process.player.WebSocketServer;

/**
 * This class contains the main method to be run the WebSocketServer instance (Initiator)
 * <p>
 * Important Note -1. Server must run before Client o avoid 'Connection refused' exception
 * Important Note -2. the port number supplied to the server and client must be the same for communication to work
 * Important Note -3. the stopCondition supplied to the server and client must be the same for both to stop gracefully.
 */
public class ProcessCommunicationServer {

    /**
     * Run this followed by ProcessCommunicationClient#main to execute communication between two player instances each running in a separate process
     *
     * @param args if args are not supplied, defaults will be used.
     *             args[0] -> stopCondition (int, number of messages that each player should send as well as receive)
     *             args[1] -> verboseLogging (boolean, whether the players should log their actions.)
     *             args[2] -> initialMessage (String, the very first message that the Server will send)
     *             args[3] -> port (int, port number to use)
     */
    public static void main(String[] args) {

        int stopCondition = ArgsReader.readStopCondition(args, 0);
        boolean verboseLogging = ArgsReader.readVerboseLogging(args, 1);
        int port = ArgsReader.readPort(args, 2);
        String initialMessage = ArgsReader.readInitialMessage(args, 3);

        play(stopCondition, verboseLogging, port, initialMessage);
    }

    public static Stats play(int stopCondition, boolean verboseLogging, int port, String initialMessage) {

        System.out.println("running WebSocketInitiator with stopCondition: " + stopCondition + ", verboseLogging: " + verboseLogging + ", port: " + port + ", initialMessage: " + initialMessage);
        WebSocketServer initiator = new WebSocketServer("WebSocketInitiator", stopCondition, verboseLogging, port, initialMessage);
        initiator.play();
        initiator.printStats();

        Stats stats = new Stats();
        stats.setInitiatorSentMessagesCount(initiator.getSentMessagesCount());
        stats.setInitiatorReceivedMessagesCount(initiator.getReceivedMessagesCount());
        return stats;
    }
}
