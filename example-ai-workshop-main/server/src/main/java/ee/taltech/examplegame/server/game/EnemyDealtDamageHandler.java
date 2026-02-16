package ee.taltech.examplegame.server.game;

import ee.taltech.examplegame.server.game.object.Enemy;
import ee.taltech.examplegame.server.game.object.Player;

import java.awt.Rectangle;
import java.util.List;


/**
 * Handles damage dealt by the enemies.
 */
public class EnemyDealtDamageHandler {

    /**
     * Checks if a player and an enemy are colliding. If so then decreases players hp.
     *
     * @param players The list of players in the game.
     * @param enemies The list of enemies in the game.
     */
    public void handleEnemyDamage(List<Player> players, List<Enemy> enemies) {
        for (Enemy enemy : enemies) {
            Rectangle enemyHitBox = HitBoxBuilder.constructEnemyHitBox(enemy);
            for (Player player : players) {
                // TODO 5.2 Lisa vastasele collisionid mängijaga
            }
        }
    }
}
