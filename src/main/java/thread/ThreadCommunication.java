package thread;

import common.ArgsReader;
import common.Stats;
import thread.player.Initiator;
import thread.player.Player;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

public class ThreadCommunication {

    /**
     * Run this to execute communication between two player instances each running in a separate thread.
     *
     * @param args if args are not supplied, defaults will be used.
     *             args[0] -> stopCondition (int, number of messages that each player should send as well as receive)
     *             args[1] -> verboseLogging (boolean, whether the players should log their actions to the console)
     *             args[2] -> initialMessage (String, the very first message that Initiator will send)
     */
    public static void main(String[] args) {

        int stopCondition = ArgsReader.readStopCondition(args, 0);
        boolean verboseLogging = ArgsReader.readVerboseLogging(args, 1);
        String initialMessage = ArgsReader.readInitialMessage(args, 2);

        System.out.println("running ThreadCommunication with stopCondition: " + stopCondition + ", initialMessage: "
                + initialMessage + ", verboseLogging: " + verboseLogging);

        play(stopCondition, initialMessage, verboseLogging);
    }

    /**
     * @param stopCondition  number of messages that each player should send as well as receive
     * @param initialMessage the very first message that Initiator will send
     * @param verboseLogging whether the players should log their actions to the console
     * @return statistics (number of messages received and sent) as a fixed size int array.
     */
    public static Stats play(final int stopCondition, String initialMessage, boolean verboseLogging) {

        // Using SynchronousQueue implementation of BlockingQueue
        BlockingQueue<String> mailBox1 = new SynchronousQueue<>();
        BlockingQueue<String> mailBox2 = new SynchronousQueue<>();

        Player initiator = new Initiator("InitiatorThread", mailBox1, mailBox2, stopCondition, verboseLogging, initialMessage);

        Player player = new Player("ResponderThread", mailBox2, mailBox1, stopCondition, verboseLogging);

        Thread t1 = new Thread(initiator);
        Thread t2 = new Thread(player);
        t1.start();
        t2.start();

        // Before reading the player stats in the main thread, we must make sure that the player threads have
        // terminated by using thread.join().
        // All actions in a thread happen-before any other thread successfully returns from a join() on that thread.
        try {
            t2.join();
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        initiator.printStats();
        player.printStats();

        Stats stats = new Stats();
        stats.setInitiatorSentMessagesCount(initiator.getSentMessagesCount());
        stats.setInitiatorReceivedMessagesCount(initiator.getReceivedMessagesCount());
        stats.setPlayerSentMessagesCount(player.getSentMessagesCount());
        stats.setPlayerReceivedMessagesCount(player.getReceivedMessagesCount());
        return stats;
    }


}
