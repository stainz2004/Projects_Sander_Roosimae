package ee.taltech.examplegame.server.game.object;

import lombok.Getter;
import lombok.Setter;
import message.dto.Direction;

import static constant.Constants.ARENA_LOWER_BOUND_X;
import static constant.Constants.ARENA_LOWER_BOUND_Y;
import static constant.Constants.ARENA_UPPER_BOUND_X;
import static constant.Constants.ARENA_UPPER_BOUND_Y;
import static constant.Constants.CHARACTER_HEIGHT_IN_PIXELS;
import static constant.Constants.CHARACTER_WIDTH_IN_PIXELS;
import static constant.Constants.HIT_COOLDOWN;

/**
 * Abstract base class representing any character in the game (players, enemies, etc.).
 * Common features for all characters:
 * - Position (x, y coordinates)
 * - Health
 * - Movement
 * - Damage system
 */
@Getter
@Setter
abstract class Character {

    private float x;
    private float y;
    private int lives;
    private long lastTimeOfReceivingDamage;

    protected Character(float x, float y, int lives) {
        this.x = x;
        this.y = y;
        this.lives = lives;
    }


    /**
     * Decrease lives by 1 if hit by an enemy.
     * Implements a cooldown to prevent multiple rapid hits.
     */
    public void decreaseLives() {
        long currentTime = System.currentTimeMillis();
        // TODO 6 Lisa elude kaotamise funktsionaalsus
    }

    /**
     * Moves the character in the specified direction within the arena bounds.
     *
     * @param direction The direction in which the player moves.
     */
    public void move(Direction direction, float movementSpeed) {
        if (direction == null) return;

        switch (direction) {
            case UP -> y += 1 * movementSpeed;
            case DOWN -> y -= 1 * movementSpeed;
            case LEFT -> x -= 1 * movementSpeed;
            case RIGHT -> x += 1 * movementSpeed;
        }

        // enforce arena bounds
        setX(Math.clamp(x, ARENA_LOWER_BOUND_X, ARENA_UPPER_BOUND_X - CHARACTER_WIDTH_IN_PIXELS));
        setY(Math.clamp(y, ARENA_LOWER_BOUND_Y, ARENA_UPPER_BOUND_Y - CHARACTER_HEIGHT_IN_PIXELS));

    }
}
