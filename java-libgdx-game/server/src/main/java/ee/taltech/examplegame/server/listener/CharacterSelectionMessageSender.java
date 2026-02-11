package ee.taltech.examplegame.server.listener;

import ee.taltech.examplegame.server.lobby.Lobby;
import message.SelectedCharacterMessage;

public class CharacterSelectionMessageSender extends Thread {
    private Lobby lobby;
    private SelectedCharacterMessage msg;


    public CharacterSelectionMessageSender(Lobby lobby, SelectedCharacterMessage msg) {
        this.lobby = lobby;
        this.msg = msg;
    }

    @Override
    public void run() {
        super.run();
        try {
            while (true) {
                lobby.sendDataToConnections(msg);
                Thread.sleep(1 * 1000);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}
