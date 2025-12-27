package cs102groupproject.Server;

import cs102groupproject.SharedObjects.ChatMessage;
/**
*handles operations related to private messages and manages
*interactions with the database
*/
public class ChatService {

    private final DBManager db;
    
    public ChatService(DBManager db) {
        this.db = db;
    }

    /**
     * saves the message of the user to database
       @param Chat message
     */
    public void saveMessage(ChatMessage cMsg){
        db.insertChatMessage(cMsg.getSenderID(), cMsg.getReceiverID(), cMsg.getMessage(), cMsg.getTimestamp());
    }
}
