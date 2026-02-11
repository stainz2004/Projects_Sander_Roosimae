package ee.taltech.examplegame.server.listener;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import ee.taltech.examplegame.server.lobby.Lobby;
import message.CharacterSelectionMessage;
import message.PlayerReadyMessage;

public class PlayerReadyMessageListener extends Listener {

    private Lobby lobby;


    public PlayerReadyMessageListener(Lobby lobby) {
        this.lobby = lobby;
    }

    @Override
    public void received(Connection connection, Object object) {
        super.received(connection, object);
        if (object instanceof PlayerReadyMessage msg) {
            lobby.addReadyConnection(connection);
            if (lobby.getPlayersReady().size() >= 2) {
                lobby.startGame();
            }
        }
    }
}
