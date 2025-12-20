package cs102groupproject;

import java.time.LocalDate;

public class ChatMessage {
    protected String senderID;
    protected String message;
    protected LocalDate timestamp;

    public ChatMessage(String senderID, String message)
    {
        this.senderID = senderID;
        this.message = message;
        this.timestamp = LocalDate.now();
    }

    public String getSenderID() {return senderID;}
    public String getMessage() {return message;}
    public LocalDate getTimestamp() {return timestamp;}
}
