package ee.taltech.examplegame.server.game;

import com.esotericsoftware.minlog.Log;
import ee.taltech.examplegame.server.game.object.Bullet;
import ee.taltech.examplegame.server.game.object.Enemy;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static constant.Constants.ARENA_LOWER_BOUND_X;
import static constant.Constants.ARENA_LOWER_BOUND_Y;
import static constant.Constants.ARENA_UPPER_BOUND_X;
import static constant.Constants.ARENA_UPPER_BOUND_Y;


/**
 * Handles bullet collisions with enemies and arena boundaries.
 */
public class BulletCollisionHandler {

    /**
     * Checks for collisions between bullets and enemies, removes bullets that hit enemies or moved out of bounds.
     *
     * @param bullets The list of bullets in the game.
     * @param enemies The list of enemies to check for collisions.
     * @return A list of remaining bullets after handling collisions.
     */
    public List<Bullet> handleCollisions(List<Bullet> bullets, List<Enemy> enemies) {
        List<Bullet> bulletsToBeRemoved = new ArrayList<>();

        // Collisions for enemies.
        for (Enemy enemy: enemies) {
            // A hitbox is an invisible area around an object that is used to
            // detect collisions with other objects, such as bullets or enemies.
            Rectangle enemyHitBox = HitBoxBuilder.constructEnemyHitBox(enemy);
            for (Bullet bullet : bullets) {
                // TODO 5.1 Lisa kuulile collisionid vastasega
            }
        }
        bulletsToBeRemoved.addAll(findOutOfBoundsBullets(bullets));
        bullets.removeAll(bulletsToBeRemoved);  // remove bullets that hit enemies or move out of bounds
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
}
