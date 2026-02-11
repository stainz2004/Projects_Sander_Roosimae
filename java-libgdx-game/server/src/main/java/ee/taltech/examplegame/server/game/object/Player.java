package ee.taltech.examplegame.server.game.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.esotericsoftware.kryonet.Connection;
import ee.taltech.examplegame.server.game.GameInstance;
import ee.taltech.examplegame.server.listener.PlayerMovementListener;
import ee.taltech.examplegame.server.listener.PlayerShootingListener;
import lombok.Getter;
import lombok.Setter;
import message.dto.Direction;
import message.dto.PlayerState;

import static constant.Constants.*;

/**
 * Server-side representation of a player in the game. This class listens for player movements or shooting actions
 * and changes the player's server-side state accordingly. Lives management.
 */
@Getter
@Setter
public class Player {
    private static final float PPM = 32f;
    private final Connection connection;
    // Keep track of listener objects for each player connection, so they can be disposed when the game ends
    private final PlayerMovementListener movementListener = new PlayerMovementListener(this);
    private final PlayerShootingListener shootingListener = new PlayerShootingListener(this);

    private final int id;
    private final GameInstance game;
    private float x = 150f;
    private float y = 150f;
    private int lives = PLAYER_LIVES_COUNT;
    private float velocityY = 0f;
    private Body body;
    private World world;
    private long lastTimeShot;

    private int playerNumber;

    private boolean isGrounded = true; // True if the player can jump

    /**
     * Initializes a new server-side representation of a Player with a game reference and connection to client-side.
     *
     * @param connection Connection to client-side.
     * @param game Game instance that this player is a part of.
     */
    public Player(Connection connection, GameInstance game, Integer playerNumber, World world) {
        createBody(world);
        this.connection = connection;
        this.playerNumber = playerNumber;
        this.id = connection.getID();
        this.game = game;
        this.connection.addListener(movementListener);
        this.connection.addListener(shootingListener);
        this.world = world;
    }

    public void createBody(World world) {
        // Define body definition
        // Create the body in the world
        if (body == null) {
            BodyDef bdef = new BodyDef();
            bdef.type = BodyDef.BodyType.DynamicBody;
            bdef.position.set(x / PPM, y / PPM);
            body = world.createBody(bdef);

            // Define shape and fixture
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(
                (PLAYER_WIDTH_IN_PIXELS / 2) / PPM,
                (PLAYER_HEIGHT_IN_PIXELS / 2) / PPM
            );

            FixtureDef fdef = new FixtureDef();
            // Add collision filtering
            fdef.shape = shape;
            fdef.friction = 0;
            Fixture fixture = body.createFixture(fdef);
            fixture.setUserData("player");


            body.createFixture(fdef);

            shape.dispose();
        }
    }

    public void updatePlayer() {
        if (body != null) {
            x = body.getPosition().x * PPM;
            y = body.getPosition().y * PPM;
            float verticalVelocity = body.getLinearVelocity().y;
            isGrounded = (verticalVelocity > -0.2f && verticalVelocity < 0.2f);

            enforceArenaBounds();

            move(null);
        }
    }

    /**
     * Moves the player in the specified direction within the arena bounds.
     *
     * @param direction The direction in which the player moves.
     */
    public void move(Direction direction) {
        if (direction == null) {
            body.setLinearVelocity(0, body.getLinearVelocity().y);
            return;
        }

        if (direction == Direction.UP) {
            jump();
        } else if (direction == Direction.RIGHT && body.getLinearVelocity().x <= MOVE_RIGHT) {
            body.setLinearVelocity(MOVE_RIGHT, body.getLinearVelocity().y);
        }
        else if (direction == Direction.LEFT && body.getLinearVelocity().x >= MOVE_LEFT) {
            body.setLinearVelocity(MOVE_LEFT, body.getLinearVelocity().y);
        }
        else {
            body.setLinearVelocity(0, body.getLinearVelocity().y);
        }
    }

    /**
     * Jump.
     */
    public void jump() {
        if (isGrounded) {
            // Apply jump impulse
            body.applyLinearImpulse(new Vector2(0, JUMP), body.getWorldCenter(), true);

            // Set isGrounded to false as the player is now in the air
        }
    }

    /**
     * Ensures the player stays within the arena bounds.
     */
    private void enforceArenaBounds() {
        float minX = ARENA_LOWER_BOUND_X / PPM;
        float maxX = (ARENA_UPPER_BOUND_X - PLAYER_WIDTH_IN_PIXELS) / PPM;
        float minY = ARENA_LOWER_BOUND_Y / PPM;
        float maxY = (ARENA_UPPER_BOUND_Y - PLAYER_HEIGHT_IN_PIXELS) / PPM;

        Vector2 pos = body.getPosition();
        Vector2 vel = body.getLinearVelocity();

        if (pos.x <= minX && vel.x < 0) {
            body.setLinearVelocity(0, vel.y);
        } else if (pos.x >= maxX && vel.x > 0) {
            body.setLinearVelocity(0, vel.y);
        }

        if (pos.y <= minY && vel.y < 0) {
            body.setLinearVelocity(vel.x, 0);
        } else if (pos.y >= maxY && vel.y > 0) {
            body.setLinearVelocity(vel.x, 0);
        }
    }

    /**
     * Returns the current state of the player, consisting of their position and remaining lives.
     */
    public PlayerState getState() {
        PlayerState playerState = new PlayerState();
        playerState.setId(id);
        playerState.setPlayerNumber(playerNumber);
        playerState.setX(x);
        playerState.setY(y);
        playerState.setLives(lives);

        return playerState;
    }

    public void shoot(Direction direction) {
        // adjust bullet spawn position to be in the center of player
        game.addBullet(
            new Bullet(x + PLAYER_WIDTH_IN_PIXELS / 2, y + PLAYER_HEIGHT_IN_PIXELS / 2, direction, id)
        );
    }

    public void decreaseLives(int damage) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTimeShot > 1000) {
            if (lives > 0) {
                lives -= damage;
            }
            lastTimeShot = currentTime;
        }
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
}
