package ee.taltech.examplegame.server.game;

import com.esotericsoftware.minlog.Log;
import ee.taltech.examplegame.server.game.object.Bullet;
import ee.taltech.examplegame.server.game.object.Player;
import ee.taltech.examplegame.server.game.object.aiBot;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import static constant.Constants.AIBOT_HEIGHT_IN_PIXELS;
import static constant.Constants.AIBOT_WIDTH_IN_PIXELS;
import static constant.Constants.ARENA_LOWER_BOUND_X;
import static constant.Constants.ARENA_LOWER_BOUND_Y;
import static constant.Constants.ARENA_UPPER_BOUND_X;
import static constant.Constants.ARENA_UPPER_BOUND_Y;
import static constant.Constants.PLAYER_HEIGHT_IN_PIXELS;
import static constant.Constants.PLAYER_WIDTH_IN_PIXELS;




/**
 * Handles bullet collisions with players and arena boundaries.
 */
public class BulletCollisionHandler {

    private List<Rectangle> collisions;


    /**
     * Checks for collisions between bullets and players, removes bullets that hit players or moved out of bounds.
     *
     * @param bullets The list of bullets in the game.
     * @param players The list of players to check for collisions.
     * @return A list of remaining bullets after handling collisions.
     */
    public List<Bullet> handleCollisions(List<Bullet> bullets, List<Player> players, List<aiBot> aibots, int[][] collisionmap) {
        List<Bullet> bulletsToBeRemoved = new ArrayList<>();

        for (Bullet bullet : bullets) {
            boolean hitSomething = false;

            int tileX = (int)(bullet.getX() / 32f);
            int tileY = (int)(bullet.getY() / 32f);
            if (tileX >= 0 && tileY >= 0 && tileX < 1020 && tileY < 960) {
                if (collisionmap[tileY][tileX] == 1) {
                    bulletsToBeRemoved.add(bullet);
                    Log.info("wall hit ");
                    hitSomething = true;
                    break;
                }
            }

            for (Player player : players) {
                Rectangle playerhitBox = constructPlayerHitBox(player);
                if (bullet.getShotById() != player.getId() && playerhitBox.contains(bullet.getX(), bullet.getY())) {
                    player.decreaseLives(1);
                    bulletsToBeRemoved.add(bullet);
                    Log.info("Player with id " + player.getId() + " was hit. " + player.getState() + " lives left.");
                    hitSomething = true;
                    break;
                }
                for (aiBot aibot : aibots) {
                    Rectangle aibotHitBox = constructAiBotHitBox(aibot);
                    if (aibotHitBox.contains(playerhitBox)) {
                        player.decreaseLives(2);
                        break;
                    }
                    if (aibotHitBox.contains(bullet.getX(), bullet.getY()) && bullet.getShotById() != 1000) {
                        bulletsToBeRemoved.add(bullet);
                        aibot.tookDamage();
                        hitSomething = true;
                        break;
                    }
                }
                }
            }

        bulletsToBeRemoved.addAll(findOutOfBoundsBullets(bullets));
        bullets.removeAll(bulletsToBeRemoved);

        return bullets;
    }

    /**
     * Finds bullets that are out of the arena bounds.
     *
     * @param bullets All active bullets.
     * @return Bullets that are out of bounds.
     */
    private List<Bullet> findOutOfBoundsBullets(List<Bullet> bullets) {
        List<Bullet> outOfBoundsBullets = new ArrayList<>();
        for (Bullet bullet : bullets) {
            if (bullet.getX() < ARENA_LOWER_BOUND_X ||
                bullet.getX() > ARENA_UPPER_BOUND_X ||
                bullet.getY() < ARENA_LOWER_BOUND_Y ||
                bullet.getY() > ARENA_UPPER_BOUND_Y
            ) {
                outOfBoundsBullets.add(bullet);
            }
        }
        return outOfBoundsBullets;
    }

    /**
     * Constructs a rectangular hitbox for a player based on their position.
     * A hitbox is essential for detecting collisions between players and bullets.
     * Only bullets that visually overlap with the player's sprite register as hits.
     */
    private Rectangle constructPlayerHitBox(Player player) {
        return
            new Rectangle(
                (int) (player.getX()),  // bottom left corner coordinates
                (int) (player.getY()),  // bottom left corner coordinates
                (int) PLAYER_WIDTH_IN_PIXELS,  // rectangle width
                (int) PLAYER_HEIGHT_IN_PIXELS  // rectangle height
            );
    }

    /**
     * Constructs a rectangular hitbox for aibot based on their position.
     */
    private Rectangle constructAiBotHitBox(aiBot aiBot) {
        return
            new Rectangle(
                (int) (aiBot.getX()),
                (int) (aiBot.getY()),
                (int) (AIBOT_WIDTH_IN_PIXELS),
                (int) (AIBOT_HEIGHT_IN_PIXELS)
            );
    }
}
