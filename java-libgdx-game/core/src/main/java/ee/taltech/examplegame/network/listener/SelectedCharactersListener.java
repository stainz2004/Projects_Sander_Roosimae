package ee.taltech.examplegame.network.listener;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import ee.taltech.examplegame.screen.LobbyScreen;
import message.SelectedCharacterMessage;

import java.util.List;

public class SelectedCharactersListener extends Listener {
    private LobbyScreen lobbyScreen;

    public SelectedCharactersListener(LobbyScreen lobbyScreen) {
        this.lobbyScreen = lobbyScreen;
    }


    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof SelectedCharacterMessage msg) {
            List<Integer> selectedIds = msg.getSelectedCharacterIds();
            lobbyScreen.updateSelectedCharacters(selectedIds);
            System.out.printf("test");
        }
    }
}
