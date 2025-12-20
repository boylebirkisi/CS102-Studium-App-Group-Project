package cs102groupproject.SharedObjects;

import java.util.ArrayList;

public class GroupSession extends Session {
    private ArrayList<User> participants;
    private GroupChat chat;

    public GroupSession(User owner, ArrayList<User> participants, String name, String type, int no, String id, int length, int breakLength)
    {
        super(owner, name, type, no, id, length, breakLength);
        this.participants = participants;
        chat = new GroupChat();
    }

    public ArrayList<User> getParticipants() {return participants;}
    public void removeParticipant(User user) {participants.remove(user);}
    public void addParticipant(User user) {participants.add(user);}
    public GroupChat getChat() {return chat;}

    public void addGroupChatMessage(ChatMessage message)
    {
        chat.addMessage(message);
    }
}
