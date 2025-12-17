package cs102groupproject;

import jakarta.websocket.*;
import org.glassfish.tyrus.client.ClientManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

@ClientEndpoint
public class ChatClient {

    private Session session;
    private final String clientName;

    public ChatClient(String name) {
        this.clientName = name;
    }

    // --- WebSocket Event Handlers ---

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        System.out.println("\n[SYSTEM] Successfully connected to the server.");
        System.out.println("Type your message and press Enter (or 'exit' to quit).");
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("\n" + message); // Print the incoming message
        System.out.print("> "); // Prompt for the next message
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("[SYSTEM] Connection closed.");
    }
    
    @OnError
    public void onError(Session session, Throwable error) {
        System.err.println("[SYSTEM] Error: " + error.getMessage());
    }

    // --- Client Logic ---

    public void start() {
        ClientManager client = ClientManager.createClient();
        try {
            // Connect to the server endpoint
            session = client.connectToServer(this, new URI("ws://localhost:8080/websocket/chat"));
            
            // Console input loop
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String line;
            System.out.print("> ");
            while ((line = reader.readLine()) != null) {
                if (line.equalsIgnoreCase("exit")) {
                    break;
                }
                // Send the message
                session.getBasicRemote().sendText(line);
                System.out.print("> ");
            }
        } catch (Exception e) {
            System.err.println("Could not connect to server: " + e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                try {
                    session.close();
                } catch (Exception e) {
                    // Ignore close error
                }
            }
        }
    }

    public static void main(String[] args) {
        // You can run two instances of this client to chat with each other
        String name = (args.length > 0) ? args[0] : "ClientA";
        new ChatClient(name).start();
    }
}