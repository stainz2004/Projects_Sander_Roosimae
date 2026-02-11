package ee.taltech.examplegame.server.listener;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import ee.taltech.examplegame.server.lobby.Lobby;
import message.CharacterSelectionMessage;
import message.LobbyDataMessage;
import message.LobbyJoinMessage;
import message.SelectedCharacterMessage;

import java.util.ArrayList;
import java.util.List;

public class CharacterSelectionMessageListener extends Listener {

    private Lobby lobby;


    public CharacterSelectionMessageListener(Lobby lobby) {
        this.lobby = lobby;
    }

    @Override
    public void received(Connection connection, Object object) {
        super.received(connection, object);
        if (object instanceof CharacterSelectionMessage characterMsg) {
            int selectedCharacterId = characterMsg.getPlayerNumber();
            System.out.println("Vastuvõetud character selection: " + selectedCharacterId + " ühenduse ID-ga: " + connection.getID() + "Lobby" + lobby.getLobbyId());
            lobby.addCharacterSelection(connection, selectedCharacterId);
        }
    }
}
