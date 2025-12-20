package cs102groupproject.SharedObjects;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import cs102groupproject.Server.websocket.ClientConnection;

public class GroupSession {

    private final String id;
    private final Set<String> clientIds = new HashSet<>();
    private final Set<ClientConnection> participants = ConcurrentHashMap.newKeySet();

    public void addParticipant(ClientConnection client) {
        participants.add(client);
    }

    public GroupSession() {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void addClient(String clientId) {
        clientIds.add(clientId);
    }

    public Set<String> getClientIds() {
        return clientIds;
    }
}
