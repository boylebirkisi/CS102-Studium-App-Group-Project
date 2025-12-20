package cs102groupproject.SharedObjects;
import com.google.api.client.json.gson.GsonFactory;
public class ProtocolMessage {

    private ActionType type;
    private Object content;

    public ProtocolMessage(ActionType type, Object content) {
        this.type = type;
        this.content = content;
    }

    public String toJson() {
        GsonFactory factory = new GsonFactory();
        try {
            return factory.toString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        ProtocolMessage msg = new ProtocolMessage(ActionType.LOGIN, "User login request");
        System.out.println("Message Type: " + msg.type);
        System.out.println("Message Content: " + msg.content);

        System.out.println("JSON Representation: " + msg.toJson());

    }
}
