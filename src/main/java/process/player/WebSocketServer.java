package process.player;


import java.io.*;
import java.net.ServerSocket;

/**
 * Each instance of this class represents an Initiator (Server) player.
 * Extends the behaviour of WebSocketPlayer class with the ability to initiate messaging with the given initial message,
 * as well as to creates a server socket, bound to the specified port.
 * @see WebSocketServer
 */
public class WebSocketServer extends WebSocketPlayer {

    /** the very first message */
    private final String initialMessage;

    /** ServerSocket instance */
    private ServerSocket serverSocket;

    /**
     * Class constructor
     *
     * @param name
     * @param stopCondition
     * @param verbose
     * @param port
     * @param initialMessage
     */
    public WebSocketServer(String name, int stopCondition, boolean verbose, int port, String initialMessage) {
        super(name, stopCondition, verbose, port);
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
        log("sent message: " + message,null);
    }

    /**
     *
     * instantiate the ServerSocket, Socket, PrintStream and BufferedReader
     * @throws IOException
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
     *
     * close the ServerSocket, Socket, PrintStream and BufferedReader
     * @throws IOException
     */
    @Override
    protected void closeSocketIO() throws IOException {
        ps.close();
        br.close();
        socket.close();
        serverSocket.close();
    }

}
