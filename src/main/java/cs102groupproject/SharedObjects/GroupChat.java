package cs102groupproject.SharedObjects;

import java.util.ArrayList;

public class GroupChat {
    protected ArrayList<ChatMessage> messages; 

    public GroupChat()
    {
        messages = new ArrayList<>();
    }

    public ArrayList<ChatMessage> getMessages() {return messages;}
    public void addMessage(ChatMessage message) {messages.add(message);}
}
