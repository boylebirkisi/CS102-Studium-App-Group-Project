package cs102groupproject.SharedObjects;

import com.fatboyindustrial.gsonjavatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Represents a message exchanged between client and server per instance.
 */
public class ProtocolMessage extends TransferObject{
    private static final Gson gson = Converters.registerAll(new GsonBuilder()).create();

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

    /**
     * Returns payload object into original class type, which was lost during creation of json. (Bununla birlikte mapofa gerek yok)
     * @param <T>
     * @param type
     * @return
     */
    public <T> T getPayloadAs(Class<T> type) {
        if (payload == null) return null;
        
        // If the payload is already the right type (unlikely with JSON), return it
        if (type.isInstance(payload)) {
            return type.cast(payload);
        }
        
        // Otherwise, convert the LinkedTreeMap back to JSON and parse as the desired Class
        String json = gson.toJson(payload);
        return gson.fromJson(json, type);
    }
}
