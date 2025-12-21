package cs102groupproject.Server;

import org.glassfish.tyrus.server.Server;

import cs102groupproject.Server.WebSocketServer;

/**
 * Starts the WebSocket server.
 */
public class ServerMain {

    public static void main(String[] args) {

        Server server = new Server(
                "localhost",
                8080,
                "/",
                null,
                WebSocketServer.class
        );

        try {
            server.start();
            System.out.println("Server running at ws://localhost:8080/ws");
            Thread.currentThread().join();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            server.stop();
        }
    }
}
