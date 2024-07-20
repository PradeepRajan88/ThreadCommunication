package process.player;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;

/**
 * Each instance of this class represents an Initiator (Server) player.
 * Extends the behaviour of WebSocketPlayer class with the ability to initiate messaging with the given initial message,
 * as well as to creates a server socket, bound to the specified port.
 *
 * @see WebSocketServer
 */
public class WebSocketServer extends WebSocketPlayer {

    /**
     * the very first message
     */
    private final String initialMessage;

    /**
     * ServerSocket instance
     */
    private ServerSocket serverSocket;

    /**
     * Class constructor
     *
     * @param name name of this player that appears in logs.
     * @param stopCondition play stop condition
     * @param verboseLogging flag whether the players should log their actions to the console.
     * @param port port that websocket should use.
     * @param initialMessage initial message that initiator sends to start play.
     */
    public WebSocketServer(String name, int stopCondition, boolean verboseLogging, int port, String initialMessage) {
        super(name, stopCondition, verboseLogging, port);
        this.initialMessage = initialMessage;
    }

    /**
     * initiate messaging.
     */
    @Override
    protected void initMessaging() {
        String message = initialMessage + " " + receivedMessagesCount;
        ps.println(message);
        sentMessagesCount++;
        log("sent message: " + message, null);
    }

    /**
     * instantiate the ServerSocket, Socket, PrintStream and BufferedReader
     *
     * @throws IOException exception.
     */
    @Override
    protected void initSocketIO() throws IOException {
        serverSocket = new ServerSocket(port);
        socket = serverSocket.accept();
        log("connection established", null);
        ps = new PrintStream(socket.getOutputStream());
        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    /**
     * close the ServerSocket, Socket, PrintStream and BufferedReader
     *
     * @throws IOException exception.
     */
    @Override
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
        if (serverSocket != null) {
            serverSocket.close();
        }
    }

}
