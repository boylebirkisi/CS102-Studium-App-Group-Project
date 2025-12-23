package cs102groupproject.Server;

import cs102groupproject.SharedObjects.ChatMessage;

public class ChatService {

    private final DBManager db;
    
    public ChatService(DBManager db) {
        this.db = db;
    }
    
    public void saveMessage(ChatMessage cMsg){
        db.insertChatMessage(cMsg.getSenderID(), cMsg.getReceiverID(), cMsg.getMessage(), cMsg.getTimestamp());
    }
}
