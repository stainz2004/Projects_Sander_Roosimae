package ee.taltech.examplegame;

import com.badlogic.gdx.Game;
import com.esotericsoftware.kryonet.Client;
import ee.taltech.examplegame.network.ServerConnection;
import ee.taltech.examplegame.screen.TitleScreen;


/**
 * Extending the Game class in here is very important for multiple screen
 * support (title screen, lobby screen, etc.) in the game.
 */
public class Main extends Game {
    private Client client;
    @Override
    public void create() {
        // establish connection to the server
        ServerConnection.getInstance().connect();
        client = ServerConnection.getClient();
        // display the title screen to the user
        setScreen(new TitleScreen(this, client));

    }
}
