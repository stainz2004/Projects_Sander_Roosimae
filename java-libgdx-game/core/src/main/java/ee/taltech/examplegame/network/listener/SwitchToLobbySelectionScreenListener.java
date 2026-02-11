package ee.taltech.examplegame.network.listener;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import ee.taltech.examplegame.screen.LobbyScreen;
import ee.taltech.examplegame.screen.LobbySelectionScreen;
import message.SwitchToLobbyScreenMessage;
import message.SwitchToLobbySelectionScreenMessage;

public class SwitchToLobbySelectionScreenListener extends Listener {
    private LobbyScreen lobbyScreen;

    public SwitchToLobbySelectionScreenListener(LobbyScreen lobbyScreen) {
        this.lobbyScreen = lobbyScreen;
    }

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof SwitchToLobbySelectionScreenMessage msg) {
            Gdx.app.postRunnable(() -> lobbyScreen.setToLobbySelectionScreen());
            System.out.printf("recieved SwitchToLobbySelectionScreenMessage");

        }
    }
}
