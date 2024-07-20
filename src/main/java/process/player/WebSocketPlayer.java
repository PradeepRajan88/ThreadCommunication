package process.player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Each instance of this class represents a player that can communicate with another instance of this class
 * that is running in a separate process (different instance of JVM).
 * Creates a server socket, bound to the specified port, and the required I/O streams for two-way communication.
 */
public class WebSocketPlayer {

    /**
     * stop condition is the number of messages that each player should send as well as receive before stopping
     */
    protected final int stopCondition;
    /**
     * port number used by the socket/ServerSocket
     */
    protected final int port;
    /**
     * name that will be printed to console
     */
    protected final String name;
    /**
     * holds the number of messaged received at any point. update everytime a message is received
     */
    protected int receivedMessagesCount;
    /**
     * holds the number of messaged sent at any point. update everytime a message is sent
     */
    protected int sentMessagesCount;
    /**
     * if true, player will log to the console each message received or sent, as well as stopping
     */
    protected final boolean verboseLogging;
    /**
     * socket instance
     */
    protected Socket socket;

    /**
     * PrintStream instance to send data
     */
    PrintStream ps;

    /**
     * BufferedReader instance to read data
     */
    BufferedReader br;

    /**
     * Class constructor
     *
     * @param name name of this player that appears in logs.
     * @param stopCondition number of messages to send and receive before play stops.
     * @param verboseLogging flag whether the players should log their actions to the console.
     * @param port port that websocket should use.
     */
    public WebSocketPlayer(String name, int stopCondition, boolean verboseLogging, int port) {
        this.name = name;
        this.stopCondition = stopCondition;
        this.verboseLogging = verboseLogging;
        this.port = port;
    }


    /**
     * Executes continuously until stop condition is met.
     */
    public void play() {
        try {
            initSocketIO();
            initMessaging();
            while (receivedMessagesCount < stopCondition) {
                String message = br.readLine();
                receivedMessagesCount++;
                log("read message: " + message, "\t");
                if (sentMessagesCount < stopCondition) {
                    message = message + " " + receivedMessagesCount;
                    ps.println(message);
                    sentMessagesCount++;
                    log("sent message: " + message, null);
                } // else Do nothing. Initiator does not have to send a reply to the last message they receive.

            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                // make sure we close the Closeables even if there is an exception
                closeSocketIO();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        log("stopped", null);
    }

    /**
     * initiates messaging. Meant for server instance.
     */
    protected void initMessaging() {
        // do nothing since this is not server
    }

    /**
     * instantiate the Socket, PrintStream and BufferedReader
     *
     * @throws IOException exception.
     */
    protected void initSocketIO() throws IOException, InterruptedException {

        final int retryDelay = 200; // Delay between retries in milliseconds
        final int maxWaitTime = 2000; // Maximum total wait time in milliseconds

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        CountDownLatch latch = new CountDownLatch(1);
        long startTime = System.currentTimeMillis();

        scheduler.scheduleAtFixedRate(() -> {
            if (System.currentTimeMillis() - startTime >= maxWaitTime) {
                System.err.println("Failed to connect to server after " + maxWaitTime + " ms");
                latch.countDown(); // Ensure the latch is decremented to unblock main thread
                scheduler.shutdown(); // Stop retrying
                return;
            }

            try {
                socket = new Socket("localhost", port);
                System.out.println("Connected to server.");
                latch.countDown(); // Signal the main thread to continue
                scheduler.shutdown(); // Stop retrying
            } catch (ConnectException conEx) {
                System.out.println("Failed to connect to server. Retrying after " + retryDelay + " ms");
            } catch (IOException e) {
                System.err.println("Unexpected I/O error: " + e.getMessage());
                latch.countDown(); // Ensure the latch is decremented to unblock main thread
                scheduler.shutdown(); // Stop retrying on unexpected errors
            }
        }, 0, retryDelay, TimeUnit.MILLISECONDS);

        latch.await(); // Wait until a connection is established or max wait time is reached

        if (socket == null || !socket.isConnected()) {
            throw new IOException("Unable to connect to the server after " + maxWaitTime + " ms");
        }
        ps = new PrintStream(socket.getOutputStream());
        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    /**
     * close the Socket, PrintStream and BufferedReader
     *
     * @throws IOException exception.
     */
    protected void closeSocketIO() throws IOException {

        if (ps != null) {
            ps.close();
        }
        if (br != null) {
            br.close();
        }
        if (socket != null) {
            socket.close();
        }
    }

    public int getReceivedMessagesCount() {
        return receivedMessagesCount;
    }

    public int getSentMessagesCount() {
        return sentMessagesCount;
    }

    /**
     * Prints statistics (counts) to the console
     */
    public void printStats() {
        System.out.println(name + " sent " + sentMessagesCount + " messages and received " + receivedMessagesCount + " messages");
    }

    /**
     * Prints the supplied string with prefix and name to the console if verboseLogging flag is true.
     */
    public void log(String string, String prefix) {
        if (verboseLogging) {
            System.out.println((prefix == null ? "" : prefix) + name + ": " + string);
        }
    }
}
