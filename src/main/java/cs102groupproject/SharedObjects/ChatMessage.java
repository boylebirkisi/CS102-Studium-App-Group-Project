package cs102groupproject.SharedObjects;

import java.time.LocalDateTime;
/**
 * Class representing a chat message between users.
 */
public class ChatMessage {
    private int senderID;
    private int receiverID;
    private String message;
    private LocalDateTime timestamp;

    public ChatMessage(int senderID, int receiverID, String message)
    {
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public ChatMessage(int senderID, int receiverID, String message, LocalDateTime timestamp)
    {
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.message = message;
        this.timestamp = timestamp;
    }

    public int getSenderID() {return senderID;}
    public int getReceiverID() {return receiverID;}
    public String getMessage() {return message;}
    public LocalDateTime getTimestamp() {return timestamp;}
}
