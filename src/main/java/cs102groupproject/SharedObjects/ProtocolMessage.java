package cs102groupproject.SharedObjects;

import com.google.gson.Gson;

/**
 * Represents a message exchanged between client and server.
 */
public class ProtocolMessage {

    private static final Gson gson = new Gson();

    private ActionType action;
    private Object payload;

    public ProtocolMessage(ActionType action, Object payload) {
        this.action = action;
        this.payload = payload;
    }

    public ActionType getAction() {
        return action;
    }

    public Object getPayload() {
        return payload;
    }

    public String toJson() {
        return gson.toJson(this);
    }

    public static ProtocolMessage fromJson(String json) {
        return gson.fromJson(json, ProtocolMessage.class);
    }
}
