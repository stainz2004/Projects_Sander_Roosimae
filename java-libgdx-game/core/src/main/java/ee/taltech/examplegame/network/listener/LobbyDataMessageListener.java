package ee.taltech.examplegame.network.listener;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import ee.taltech.examplegame.game.LobbyDataManager;
import message.LobbyDataMessage;

public class LobbyDataMessageListener extends Listener {
    private final LobbyDataManager lobbyDataManager;

    public LobbyDataMessageListener(LobbyDataManager lobbyDataManager) {
        this.lobbyDataManager = lobbyDataManager;
    }
    @Override
    public void received(Connection connection, Object object) {
        super.received(connection, object);


        if (object instanceof LobbyDataMessage lobbyDataMessage) {
            System.out.println("received lobby data message");
            lobbyDataManager.setLatestLobbyDataMessage(lobbyDataMessage);
        }
    }
}
