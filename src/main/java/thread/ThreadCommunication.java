package thread;

import common.Configuration;
import thread.player.Initiator;
import thread.player.Player;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

public class ThreadCommunication {

    /**
     * Run this to execute communication between two player instances each running in a separate thread.
     *
     * @param args if args are not supplied, defaults will be used.
     *   args[0] -> stopCondition (int, number of messages that each player should send as well as receive)
     *   args[1] -> initialMessage (String, the very first message that Initiator will send)
     *   args[2] -> verbose (boolean, whether the players should log their actions to the console)
     */
    public static void main(String args[]) {

        int stopCondition = Integer.parseInt(args.length > 0 ? args[0] : Configuration.read(Configuration.KEY_STOP_CONDITION));
        String initialMessage = args.length > 1 ? args[1] : Configuration.read(Configuration.KEY_INITIAL_MESSAGE);
        boolean verbose = Boolean.parseBoolean(args.length > 2 ? args[2] : Configuration.read(Configuration.KEY_VERBOSE));

        System.out.println("running ThreadCommunication with stopCondition: " + stopCondition + ", initialMessage: "
                + initialMessage + ", verbose: " + verbose);

        validateAndPlay(stopCondition, initialMessage, verbose);
    }

    /**
     * @param stopCondition number of messages that each player should send as well as receive
     * @param initialMessage the very first message that Initiator will send
     * @param verbose whether the players should log their actions to the console
     * @return statistics (number of messages received and sent) as a fixed size int array.
     */
    public static int[] validateAndPlay(final int stopCondition, String initialMessage, boolean verbose) {

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

        // Using SynchronousQueue implementation of BlockingQueue
        BlockingQueue<String> mailBox1 = new SynchronousQueue<>();
        BlockingQueue<String> mailBox2 = new SynchronousQueue<>();

        Initiator initiator = new Initiator("InitiatorThread", mailBox1, mailBox2, stopCondition, verbose, initialMessage);

        Player responder = new Player("ResponderThread", mailBox2, mailBox1, stopCondition, verbose);

        Thread t1 = new Thread(initiator);
        Thread t2 = new Thread(responder);
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
        responder.printStats();

        int[] stats = new int[4];
        stats[0] = initiator.getSentMessagesCount();
        stats[1] = initiator.getReceivedMessagesCount();
        stats[2] = initiator.getSentMessagesCount();
        stats[3] = initiator.getReceivedMessagesCount();
        return stats;
    }


}
