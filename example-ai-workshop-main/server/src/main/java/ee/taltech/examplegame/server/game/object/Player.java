package ee.taltech.examplegame.server.game.object;

import com.esotericsoftware.kryonet.Connection;
import ee.taltech.examplegame.server.game.GameInstance;
import ee.taltech.examplegame.server.listener.PlayerMovementListener;
import ee.taltech.examplegame.server.listener.PlayerShootingListener;
import lombok.Getter;
import lombok.Setter;
import message.dto.Direction;
import message.dto.PlayerState;

import static constant.Constants.PLAYER_HEIGHT_IN_PIXELS;
import static constant.Constants.PLAYER_LIVES_COUNT;
import static constant.Constants.PLAYER_WIDTH_IN_PIXELS;

/**
 * Server-side representation of a player in the game. This class listens for player movements or shooting actions
 * and changes the player's server-side state accordingly. Lives management.
 */
@Getter
@Setter
public class Player extends Character {
    private final Connection connection;
    // Keep track of listener objects for each player connection, so they can be disposed when the game ends
    private final PlayerMovementListener movementListener = new PlayerMovementListener(this);
    private final PlayerShootingListener shootingListener = new PlayerShootingListener(this);

    private final int id;
    private final GameInstance game;


    /**
     * Initializes a new server-side representation of a Player with a game reference and connection to client-side.
     *
     * @param connection Connection to client-side.
     * @param game Game instance that this player is a part of.
     */
    public Player(Connection connection, GameInstance game) {
        // Initialize the Character superclass with starting position and lives
        super(0, 0, PLAYER_LIVES_COUNT);
        this.connection = connection;
        this.id = connection.getID();
        this.game = game;
        this.connection.addListener(movementListener);
        this.connection.addListener(shootingListener);
    }


    /**
     * Returns the current state of the player, consisting of their position and remaining lives.
     */
    public PlayerState getState() {
        PlayerState playerState = new PlayerState();
        playerState.setId(connection.getID());
        playerState.setX(getX());
        playerState.setY(getY());
        playerState.setLives(getLives());
        return playerState;
    }

    /**
     * Creates a new bullet object and adds it to the game instance.
     *
     * @param direction The direction in which the bullet is shot.
     */
    public void shoot(Direction direction) {
        // adjust bullet spawn position to be in the center of player
        game.addBullet(
            new Bullet(getX() + PLAYER_WIDTH_IN_PIXELS / 2, getY() + PLAYER_HEIGHT_IN_PIXELS / 2, direction, id)
        );
    }

    /**
     * Removes the movement and shooting listeners from the player's connection.
     * This should be called when the player disconnects or the game ends.
     * Disposing of the listeners prevents potential thread exceptions when reusing
     * same connections for future game instances.
     */
    public void dispose() {
        connection.removeListener(movementListener);
        connection.removeListener(shootingListener);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Player player = (Player) obj;
        return id == player.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
