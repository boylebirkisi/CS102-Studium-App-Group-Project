package cs102groupproject.SharedObjects;

import java.time.LocalDateTime;
import java.util.ArrayList;
/**
 * Class representing a group session with multiple participants and a group chat.
 * @author Ali Mersin
 */
public class GroupSession extends Session {
    private ArrayList<User> participants;
    private GroupChat chat;
    private boolean isPublic;

    public GroupSession() {
        super();
        participants = new ArrayList<>();
        chat = new GroupChat();
    }

    public GroupSession(Session session, ArrayList<User> participants, boolean isPublic)
    {
        super(session.getOwner(), session.getName(), session.getType(), session.getNo(), session.getId(), session.getLength(), session.getBreakLength(), session.getStartDate());
        this.participants = participants;
        chat = new GroupChat();
        this.isPublic = isPublic;
    }

    public GroupSession(User owner, ArrayList<User> participants, String name, String type, int no, int id, int length, int breakLength, LocalDateTime startDate, boolean isPublic)
    {
        super(owner, name, type, no, id, length, breakLength, startDate);
        this.participants = participants;
        chat = new GroupChat();
        this.isPublic = isPublic;
    }

    public GroupSession(User owner, ArrayList<User> participants, String name, String type, int no, int length, int breakLength, LocalDateTime startDate, boolean isPublic)
    {
        super(owner, name, type, no, length, breakLength, startDate);
        this.participants = participants;
        chat = new GroupChat();
        this.isPublic = isPublic;
    }

    // public ArrayList<User> getParticipants() {return participants;}
    // public void removeParticipant(User user) {participants.remove(user);}
    // public void addParticipant(User user) {participants.add(user);}
    // public GroupChat getChat() {return chat;}

    // public void addGroupChatMessage(ChatMessage message)
    // {
    //     chat.addMessage(message);
    // }
}
