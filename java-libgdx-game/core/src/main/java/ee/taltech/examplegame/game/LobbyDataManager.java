package ee.taltech.examplegame.game;

import ee.taltech.examplegame.network.ServerConnection;
import ee.taltech.examplegame.network.listener.LobbyDataMessageListener;
import ee.taltech.examplegame.screen.LobbySelectionScreen;
import message.LobbyDataMessage;


public class LobbyDataManager {

    private LobbySelectionScreen lobbySelectionScreen;
    private LobbyDataMessage latestLobbyDataMessage;

    public LobbyDataManager(LobbySelectionScreen lobbySelectionScreen) {
        this.lobbySelectionScreen = lobbySelectionScreen;
    }

    public LobbyDataMessage getLatestLobbyDataMessage() {
        return latestLobbyDataMessage;
    }

    public void setLatestLobbyDataMessage(LobbyDataMessage latestLobbyDataMessage) {
        System.out.println("lobby data manager");
        this.latestLobbyDataMessage = latestLobbyDataMessage;
        lobbySelectionScreen.updateButtons(latestLobbyDataMessage);
    }

    public LobbyDataManager() {
        ServerConnection
            .getInstance()
            .getClient()
            .addListener(new LobbyDataMessageListener(this));


    }

}
