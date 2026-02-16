package ee.taltech.examplegame.server.game.object;

import lombok.Getter;
import lombok.Setter;
import message.dto.Direction;
import message.dto.EnemyState;

import static constant.Constants.ENEMY_SPEED;


/**
 * Server-side representation of an enemy in game.
 * Movement/player following happens here.
 */
@Getter
@Setter
public class Enemy extends Character{

    private int id;
    private static int idCounter = 1;
    // How close the enemy must be on an axis to be considered "aligned" with the player.
    // Stops the AI from overshooting and getting stuck.
    private static final float ALIGNMENT_THRESHOLD = 0.3f;

    /**
     * Initializes a new enemy with x and y coordinates.
     * Also sets enemy's hp.
     */
    public Enemy(float x, float y, int lives) {
        // Initialize the Character superclass with starting position and lives
        super(x, y, lives);
        this.id = idCounter++;
    }

    /**
     * Follow the player towards the correct direction.
     */
    public void follow(Player player) {
        float currentX = getX();
        float currentY = getY();
        float xDistance = Math.abs(player.getX() - currentX);

        // Move horizontally first until aligned with player (within 0.3 units), then move vertically.
        // The threshold prevents overshooting and getting stuck. Adjust if ENEMY_SPEED changes.
        // TODO 4.1 Muuda liikumissuunad õigeks.
        if (xDistance > ALIGNMENT_THRESHOLD) {
            if (player.getX() > currentX) {
                move(Direction.LEFT, ENEMY_SPEED);
            } else {
                move(Direction.RIGHT, ENEMY_SPEED);
            }
        } else {
            if (player.getY() > currentY) {
                move(Direction.DOWN, ENEMY_SPEED);
            } else {
                move(Direction.UP, ENEMY_SPEED);
            }
        }
    }


    /**
     * Returns the current state of the enemy, consisting of their position and remaining lives.
     */
    public EnemyState getState() {
        EnemyState enemyState = new EnemyState();
        enemyState.setX(getX());
        enemyState.setY(getY());
        enemyState.setLives(getLives());
        return enemyState;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Enemy enemy = (Enemy) obj;
        return id == enemy.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
