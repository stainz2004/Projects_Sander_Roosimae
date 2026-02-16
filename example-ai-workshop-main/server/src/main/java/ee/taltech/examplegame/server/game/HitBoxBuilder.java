package ee.taltech.examplegame.server.game;

import ee.taltech.examplegame.server.game.object.Enemy;
import ee.taltech.examplegame.server.game.object.Player;

import java.awt.Rectangle;

import static constant.Constants.ENEMY_WIDTH_IN_PIXELS;
import static constant.Constants.ENEMY_HEIGHT_IN_PIXELS;
import static constant.Constants.PLAYER_HEIGHT_IN_PIXELS;
import static constant.Constants.PLAYER_WIDTH_IN_PIXELS;


public class HitBoxBuilder {

    private HitBoxBuilder() {
        // Private constructor to prevent instantiation of utility class
    }

    /**
     * Constructs a rectangular hitbox for an enemy based on their position.
     *
     * @param enemy The enemy to construct a hitbox for.
     * @return A Rectangle representing the enemy's hitbox.
     */
    public static Rectangle constructEnemyHitBox(Enemy enemy) {
        return new Rectangle(
            (int) (enemy.getX()),
            (int) (enemy.getY()),
            (int) ENEMY_WIDTH_IN_PIXELS,
            (int) ENEMY_HEIGHT_IN_PIXELS
        );
    }

    /**
     * Constructs a rectangular hitbox for a player based on their position.
     *
     * @param player The player to construct a hitbox for.
     * @return A Rectangle representing the player's hitbox.
     */
    public static Rectangle constructPlayerHitBox(Player player) {
        return new Rectangle(
            (int) (player.getX()),
            (int) (player.getY()),
            (int) PLAYER_WIDTH_IN_PIXELS,
            (int) PLAYER_HEIGHT_IN_PIXELS
        );
    }
}
