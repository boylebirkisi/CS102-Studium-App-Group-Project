package cs102groupproject.SharedObjects;

import java.util.ArrayList;
/**
 * Class representing a group chat containing multiple chat messages.
 */
public class GroupChat {
    private ArrayList<ChatMessage> messages; 

    public GroupChat()
    {
        messages = new ArrayList<>();
    }

    public ArrayList<ChatMessage> getMessages() {return messages;}
    public void addMessage(ChatMessage message) {messages.add(message);}
}
