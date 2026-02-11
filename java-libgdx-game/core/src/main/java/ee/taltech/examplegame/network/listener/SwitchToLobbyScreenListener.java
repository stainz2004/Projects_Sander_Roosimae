package ee.taltech.examplegame.network.listener;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import ee.taltech.examplegame.screen.LobbyScreen;
import ee.taltech.examplegame.screen.LobbySelectionScreen;
import message.SwitchToGameScreenMessage;
import message.SwitchToLobbyScreenMessage;

public class SwitchToLobbyScreenListener extends Listener {
    private LobbySelectionScreen lobbySelectionScreen;

    public SwitchToLobbyScreenListener(LobbySelectionScreen lobbySelectionScreen) {
        this.lobbySelectionScreen = lobbySelectionScreen;
    }

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof SwitchToLobbyScreenMessage msg) {
            Gdx.app.postRunnable(() -> lobbySelectionScreen.setToLobbyScreen());
            System.out.printf("recieved SwitchToGameScreenMessage");
        }
    }
}
