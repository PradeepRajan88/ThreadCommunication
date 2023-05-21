package thread.player;

import java.util.concurrent.BlockingQueue;

/**
 * Each instance of this class represents a player that can communicate with another instance of this class while running as a separate thread.
 * Implements Runnable interface to enable concurrent execution.
 */
public class Player implements Runnable {

    /** name that will be printed to console */
    private final String name;

    /** holds the number of messaged received at any point. update everytime a message is received */
    private int receivedMessagesCount;

    /** holds the number of messaged sent at any point. update everytime a message is sent */
    private int sentMessagesCount;

    /** stop condition is the number of messages that each player should send as well as receive before stopping */
    private final int stopCondition;

    /** messages to this player will be put in this queue and can be read by this player */
    private BlockingQueue<String> inbox;

    /** messages from this player will be put in this queue to be read by the other player*/
    private BlockingQueue<String> outbox;

    /** if true, player will log to the console each message received or sent, as well as stopping */
    private boolean verbose;

    /**
     * Executes continuously until stop condition is met.
     */
    @Override
    public void run() {
        try {
            while (receivedMessagesCount < stopCondition) {
                // receivedMessage returned by readMessage() is used as parameter of the subsequent sendMessage()
                // This order ensures that a reply will be sent only after a message is received.
                // Except for the initial message, which is triggered differently.
                String receivedMessage = readMessage();
                if (sentMessagesCount < stopCondition) {
                    sendMessage(receivedMessage);
                } // else Do nothing. Initiator does not have to send a reply to the last message they receive.

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log("stopped", null);
    }

    /**
     * Class constructor
     * @param name
     * @param inbox
     * @param outbox
     * @param stopCondition
     * @param verbose
     */
    public Player(String name, BlockingQueue<String> inbox, BlockingQueue<String> outbox,
                  int stopCondition, boolean verbose) {
        this.name = name;
        this.inbox = inbox;
        this.outbox = outbox;
        this.stopCondition = stopCondition;
        this.verbose = verbose;
    }

    /**
     * Retrieves and removes the message from inbox, waiting if necessary until a message is available. increments
     * receivedMessagesCount and returns the message.
     *
     * @return message
     * @throws InterruptedException
     */
    public synchronized String readMessage() throws InterruptedException {

        String message = inbox.take();
        receivedMessagesCount++;
        log("read message: " + message, "\t");
        return message;
    }

    /**
     * Inserts the specified message into the outbox, waiting if necessary for space to become available,
     * and increments sentMessagesCount. Does not insert if message is null or empty.
     *
     * @param message
     * @throws InterruptedException
     */
    public synchronized void sendMessage(String message) throws InterruptedException {

        if (message != null && !message.isEmpty()) {
            message = message + " " + receivedMessagesCount;
            outbox.put(message);
            sentMessagesCount++;
            log("sent message: " + message, null);
        } else {
            log("has no message to send, skip sending", null);
        }
    }

    /**
     *
     * @return receivedMessagesCount
     */
    public int getReceivedMessagesCount() {
        return receivedMessagesCount;
    }

    /**
     *
     * @return sentMessagesCount
     */
    public int getSentMessagesCount() {
        return sentMessagesCount;
    }

    /**
     *
     * Prints statistics (counts) to the console
     */
    public void printStats() {
        System.out.println(name + " sent " + sentMessagesCount + " messages and received " + receivedMessagesCount + " messages");
    }

    /**
     *
     * Prints the supplied string with prefix and name to the console if verbose flag is true.
     */
    public void log(String string, String prefix) {
        if (verbose) {
            System.out.println((prefix == null ? "" : prefix) + name + ": " + string);
        }
    }

}
