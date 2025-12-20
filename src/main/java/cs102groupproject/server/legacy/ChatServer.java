package cs102groupproject.Server.legacy;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.glassfish.tyrus.server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@ServerEndpoint("/chat")
public class ChatServer {

    // Thread-safe set to store all active WebSocket sessions
    private static Set<Session> peers = Collections.synchronizedSet(new HashSet<>());
    
    // Executor for keeping the server running
    private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    // --- WebSocket Event Handlers ---

    @OnOpen
    public void onOpen(Session session) {
        peers.add(session);
        System.out.println("Client connected: " + session.getId());
        broadcast("SERVER: User " + session.getId() + " joined the chat.");
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("Message from " + session.getId() + ": " + message);
        broadcast("User " + session.getId() + ": " + message);
    }

    @OnClose
    public void onClose(Session session) {
        peers.remove(session);
        System.out.println("Client disconnected: " + session.getId());
        broadcast("SERVER: User " + session.getId() + " left the chat.");
    }
    
    @OnError
    public void onError(Session session, Throwable error) {
        System.err.println("Error on session " + session.getId() + ": " + error.getMessage());
    }

    // --- Broadcast Logic ---

    private static void broadcast(String message) {
        for (Session peer : peers) {
            if (peer.isOpen()) {
                try {
                    // getAsyncRemote() is often preferred for non-blocking I/O
                    peer.getAsyncRemote().sendText(message);
                } catch (Exception e) {
                    System.err.println("Failed to send message to client " + peer.getId());
                }
            }
        }
    }

    // --- Server Startup ---
    public static void main(String[] args) {
        Server server = new Server("localhost", 8080, "/websocket", null, ChatServer.class);
        
        try {
            server.start();
            System.out.println("✅ WebSocket Server started!");
            System.out.println("📡 Listening on: ws://localhost:8080/websocket/chat");
            System.out.println("\nPress Enter to stop the server...");
            
            // Wait for Enter
            new Scanner(System.in).nextLine();
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            server.stop();
            System.out.println("Server stopped.");
        }
    }
}