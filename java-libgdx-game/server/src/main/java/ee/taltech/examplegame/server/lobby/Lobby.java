package ee.taltech.examplegame.server.lobby;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import ee.taltech.examplegame.server.game.GameInstance;
import ee.taltech.examplegame.server.listener.CharacterSelectionMessageListener;
import ee.taltech.examplegame.server.listener.CharacterSelectionMessageSender;
import ee.taltech.examplegame.server.listener.PlayerReadyMessageListener;
import lombok.Getter;
import lombok.Setter;
import message.*;

import java.util.*;
import java.util.HashSet;
import java.util.Set;

public class Lobby {
    private final Set<Connection> connections;
    private Map<Connection, Integer> selectedCharacters = new HashMap<>();
    @Getter
    private List<Connection> playersReady = new ArrayList<>();
    private final Server kryoServer;

    public void addCharacterSelection(Connection connection, int characterId) {
        if (selectedCharacters.containsValue(characterId)) {
            System.out.print("tegelane juba valitud");
            connection.sendTCP("Valitud tegelane (" + characterId + ") on juba hõivatud. Palun vali teine tegelane.");
            return;
        }

        selectedCharacters.put(connection, characterId);
        connection.sendTCP("Teie tegelane (" + characterId + ") on edukalt valitud.");

        List<Integer> chosenIds = new ArrayList<>(selectedCharacters.values());
        SelectedCharacterMessage msg = new SelectedCharacterMessage(chosenIds);
        sendDataToConnections(msg);
    }

    public void startGame() {
        if (selectedCharacters.size() == 2) {
            game = new GameInstance(this, selectedCharacters);
            game.start();
            sendDataToConnections(new SwitchToGameScreenMessage());
        }
    }

    public Map<Connection, Integer> getSelectedCharacters() {
        return selectedCharacters;
    }


    public int getLobbySize() {
        return lobbySize;
    }

    private final int lobbySize;
    private static int id = 0;
    private final int lobbyId;
    private GameInstance game;


    public Set<Connection> getConnections() {
        return connections;
    }

    public Lobby(Server kryoServer, int lobbySize) {
        this.kryoServer = kryoServer;
        this.connections = new HashSet<>();
        this.lobbyId = id++;
        this.lobbySize = lobbySize;
    }

    public void addConnection(Connection connection) {
        if (connections.size() >= lobbySize) {
            connection.sendTCP("Lobby is full");
            return;
        }
        connection.addListener(new CharacterSelectionMessageListener(this));
        connections.add(connection);
        connection.addListener(new PlayerReadyMessageListener(this));

        if (connections.size() == 2) {
            sendDataToConnections(new SwitchToLobbyScreenMessage());
        }
    }
    public void sendToAllUDP(Object message) {
        kryoServer.sendToAllUDP(message);
    }

    public void removeConnection(Connection connection) {
        if (connections.size() == 2) {
            connections.remove(connection);
            sendDataToConnections(new SwitchToLobbySelectionScreenMessage());
            connections.clear();
        } else {
            connections.remove(connection);
        }
        selectedCharacters.clear();
        List<Integer> chosenIds = new ArrayList<>(selectedCharacters.values());
        SelectedCharacterMessage msg = new SelectedCharacterMessage(chosenIds);
        sendDataToConnections(msg);

        if (game != null) {
            game.removeConnection(connection);
        }
    }

    public void sendDataToConnections(Object msg) {
        for (Connection c : connections) {
            c.sendTCP(msg);
        }
    }

    public void disposeGame() {
        game = null;
    }

    public int getLobbyId() {
        return lobbyId;
    }


    public int getCurrentPlayerCount() {
        return connections.size();
    }

    public void addReadyConnection(Connection conn) {
        if (!playersReady.contains(conn)) {
            playersReady.add(conn);
        }
    }

}
