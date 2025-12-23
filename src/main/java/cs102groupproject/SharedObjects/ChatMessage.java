package cs102groupproject.SharedObjects;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    public ChatMessage(int senderID, int receiverID, String message, long timestamp)
    {
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.message = message;
        this.timestamp = LocalDateTime.ofEpochSecond(timestamp, 0, java.time.ZoneOffset.UTC);
    }

    public int getSenderID() {return senderID;}
    public int getReceiverID() {return receiverID;}
    public String getMessage() {return message;}
    public LocalDateTime getTimestamp() {return timestamp;}
}
