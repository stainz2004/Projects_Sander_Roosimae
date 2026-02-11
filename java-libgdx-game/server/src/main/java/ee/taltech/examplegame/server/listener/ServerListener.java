package ee.taltech.examplegame.server.listener;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import ee.taltech.examplegame.server.lobby.Lobby;
import message.GameJoinMessage;
import message.LobbyDataMessage;
import message.LobbyJoinMessage;
import message.LobbyLeaveMessage;

import java.util.ArrayList;
import java.util.List;


/**
 * This class listens for all connections and messages that are
 * sent to the server by the clients.
 * <p>
 * It contains 3 methods that can be overridden to add custom logic
 */
public class ServerListener extends Listener {
    private Server server;
    private List<Lobby> lobbies = new ArrayList<>();

    public ServerListener(Server server) {
        this.server = server;
        lobbies.add(new Lobby(server , 2));  // 2 mängijaga lobby
        lobbies.add(new Lobby(server, 2));
        lobbies.add(new Lobby(server, 2));
    }

    /**
     * When a client connects to the server, this method is called.
     * Include any logic that should be executed when a client connects to the server.
     * ex - add the client to the game, etc.
     *
     * @param connection The connection object that is created when a client connects to the server.
     */
    @Override
    public void connected(Connection connection) {
        Log.info("Client connected: " + connection.getRemoteAddressTCP().getAddress().getHostAddress());
        Log.info(String.valueOf(connection.getID()));
        super.connected(connection);
    }

    /**
     * When a client disconnects from the server, this method is called.
     * Include any logic that should be executed when a client disconnects from the server.
     * ex - clean up resources, remove the client from the game, etc.
     *
     * @param connection The connection object of the client that disconnected.
     */
    @Override
    public void disconnected(Connection connection) {
        Log.info("Client disconnected");

        if (lobbies.get(0) != null) {
            lobbies.get(0).removeConnection(connection);
        }
        super.disconnected(connection);
    }

    /**
     * When a message is received from a client, this method is called.
     * ex - client sends a JoinGame message, the server will add the client to the game.
     *
     * @param connection The connection object of the client that sent the message.
     * @param object     The object that is sent by the client.
     */
    @Override
    public void received(Connection connection, Object object) {
        Log.debug("Received message from client (" + connection.getRemoteAddressTCP().getAddress().getHostAddress() + "): " + object.toString());

        // when a GameJoinMessage is received, the server will add the connection to the game instance
        // if there is no active instance, a new one is created
        if (object instanceof LobbyJoinMessage) {
            lobbies.get(((LobbyJoinMessage) object).getId()).addConnection(connection);
            Log.info(String.valueOf(lobbies.getFirst().getCurrentPlayerCount()));
            Log.info("Keegi liitus");
            sendLobbyDataToClients();
        }

        if (object instanceof GameJoinMessage) {
            sendLobbyDataToClients();
        }

        if (object instanceof LobbyLeaveMessage) {
            lobbies.forEach(x -> x.removeConnection(connection));
        }

        super.received(connection, object);
    }

    public void sendLobbyDataToClients() {
        LobbyDataMessage lobbyDataMessage = new LobbyDataMessage();

        for (Lobby l : lobbies) {
            lobbyDataMessage.lobbies.put(l.getLobbyId(), "Lobby " + l.getLobbyId());
            lobbyDataMessage.maxPlayers.put(l.getLobbyId(), l.getLobbySize());
            lobbyDataMessage.playerCount.put(l.getLobbyId(), l.getCurrentPlayerCount());

        }
        server.sendToAllUDP(lobbyDataMessage);
    }
}
