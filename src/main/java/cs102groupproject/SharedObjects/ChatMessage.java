package cs102groupproject.SharedObjects;

import java.time.LocalDate;

public class ChatMessage {
    private int senderID;
    private String message;
    private LocalDate timestamp;

    public ChatMessage(int senderID, String message)
    {
        this.senderID = senderID;
        this.message = message;
        this.timestamp = LocalDate.now();
    }

    public int getSenderID() {return senderID;}
    public String getMessage() {return message;}
    public LocalDate getTimestamp() {return timestamp;}
}
