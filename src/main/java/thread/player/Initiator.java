package thread.player;

import java.util.concurrent.BlockingQueue;

/**
 * Each instance of this class represents an Initiator player.
 * Extends the behaviour of Player class with the ability to initiate messaging with the given initial message.
 *
 * @see Player
 */
public class Initiator extends Player {

    /**
     * the very first message
     */
    private final String initialMessage;

    public Initiator(String name, BlockingQueue<String> inbox, BlockingQueue<String> outbox,
                     int stopCondition, boolean verboseLogging, String initialMessage) {
        super(name, inbox, outbox, stopCondition, verboseLogging);
        this.initialMessage = initialMessage;
    }

    @Override
    public void run() {

        try {
            // initiate messaging before going into the while loop
            sendMessage(initialMessage);
            super.run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
