package ee.taltech.examplegame.network.listener;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import ee.taltech.examplegame.screen.LobbyScreen;
import message.SwitchToGameScreenMessage;

public class SwitchToGameScreenListener extends Listener {
    private LobbyScreen lobbyScreen;

    public SwitchToGameScreenListener(LobbyScreen lobbyScreen) {
        this.lobbyScreen = lobbyScreen;
    }

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof SwitchToGameScreenMessage msg) {
            Gdx.app.postRunnable(() -> lobbyScreen.setToGameScreen());
            System.out.printf("recieved SwitchToGameScreenMessage");
        }
    }
}
