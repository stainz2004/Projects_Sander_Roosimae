package message;

import java.util.HashMap;
import java.util.Map;

public class LobbyDataMessage {
    // lobby id -> lobby nimi
    public Map<Integer, String> lobbies = new HashMap<>();
    // lobby id -> mängijate arv
    public Map<Integer, Integer> playerCount = new HashMap<>();
    // lobby id -> maksimaalne mängijate arv
    public Map<Integer, Integer> maxPlayers = new HashMap<>();
}
