package ee.taltech.examplegame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import ee.taltech.examplegame.network.ServerConnection;
import ee.taltech.examplegame.screen.MusicControl;
import message.dto.Direction;
import message.PlayerMovementMessage;
import message.PlayerShootingMessage;

/**
 * Listens for user input in the GameScreen regarding player movement and shooting,
 * forwards the corresponding messages to the server.
 */
public class PlayerInputManager {
    private static final float SHOOT_SOUND_COOLDOWN = 0.3f; // seconds
    private float timeSinceLastShot = 0f;
    Sound explosionSound = Gdx.audio.newSound(Gdx.files.internal("shooting_vfx3.mp3"));
    Sound jumpSound = Gdx.audio.newSound(Gdx.files.internal("jump_sfx.mp3"));
    private long explosionSoundId = -1;


    public void handleMovementInput() {
        var movementMessage = new PlayerMovementMessage();

        // detect key presses and send a movement message with the desired direction to the server
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.A)) {
            movementMessage.setDirection(Direction.LEFT);
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            movementMessage.setDirection(Direction.RIGHT);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            movementMessage.setDirection(Direction.UP);
        }

        // don't send anything if player is not moving
        if (movementMessage.getDirection() == null) return;

        // message is sent to the server
        ServerConnection
            .getInstance()
            .getClient()
            .sendUDP(movementMessage);  // UDP, because nothing bad happens when some messages don't reach the server
    }

    public void handleShootingInput(float delta) {
        var shootingMessage = new PlayerShootingMessage();
        Direction direction = null;
        timeSinceLastShot += delta;

        // detect key presses and send shooting message to the server
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.LEFT)) {
            direction = Direction.LEFT;
        } else if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.RIGHT)) {
            direction = Direction.RIGHT;
        } else if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.UP)) {
            direction = Direction.UP;
        } else if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.DOWN)) {
            direction = Direction.DOWN;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            MusicControl.getInstance().playSound(jumpSound, 0.8f);

        }

        if (direction != null) {
            shootingMessage.setDirection(direction);

            if (timeSinceLastShot >= SHOOT_SOUND_COOLDOWN) {
                // Stop previous instance if still playing
                if (explosionSoundId != -1) {
                    explosionSound.stop(explosionSoundId);
                }

                // Play new sound and save its ID
                MusicControl.getInstance().playSound(explosionSound, 1f);
                timeSinceLastShot = 0f;
            }
        }

        // don't send anything if player is not shooting
        if (shootingMessage.getDirection() == null) return;

        // message is sent to the server
        ServerConnection
            .getInstance()
            .getClient()
            .sendUDP(shootingMessage);
    }
    public void dispose() {
        if (explosionSound != null) {
            explosionSound.dispose();
        }
    }
}
