package process.player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ConnectException;
import java.net.Socket;

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
    protected String name;
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
    protected boolean verboseLogging;
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
     * @param name
     * @param stopCondition
     * @param verboseLogging
     * @param port
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
        } catch (IOException e) {
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
     * @throws IOException
     */
    protected void initSocketIO() throws IOException {
        int retryCount = 0;
        while (retryCount < 30) {
            try {
                socket = new Socket("localhost", port);
                break;
            } catch (ConnectException conEx) {
                System.out.println("Failed to connect to server. Retrying...");
                retryCount++;
            }
        }
        ps = new PrintStream(socket.getOutputStream());
        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    /**
     * close the Socket, PrintStream and BufferedReader
     *
     * @throws IOException
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
