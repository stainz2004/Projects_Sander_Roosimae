package ee.taltech.examplegame.server.game.object;

import ee.taltech.examplegame.server.Ai.AStar;
import ee.taltech.examplegame.server.game.GameInstance;
import lombok.Getter;
import lombok.Setter;
import message.dto.Direction;
import message.dto.aiBotState;

import java.util.Arrays;
import java.util.List;

import java.util.concurrent.locks.ReentrantLock;

import static constant.Constants.MONSTER_HEIGHT_IN_PIXELS;
import static constant.Constants.MONSTER_LIVES_COUNT;
import static constant.Constants.MONSTER_WIDTH_IN_PIXELS;

/**
 * The type Ai bot.
 */
@Getter
@Setter
public class aiBot {
    private static int currentId = 0;
    private final int netId;
    private final GameInstance game;
    private final ReentrantLock positionLock = new ReentrantLock();
    private final List<Player> players;

    private float x;
    private float y;
    private int lives = MONSTER_LIVES_COUNT;
    private int[][] navigationMap;
    private long lastUpdateTime;
    private final int movementIntervalMs = 300; // Update every 100ms
    private AStar.Node currentNode;
    private final float stepSize = 1f; // pixels per second
    private final float speed = 2f;
    private float targetX = -1;
    private float targetY = -1;
    private static final long MOVE_DELAY = 500; // Move every 2 seconds
    private static final long SHOOT_DELAY = 500;
    private long lastMoveTime = 0;
    private long lastShootTime = 0;
    private AStar aStar;

    /**
     * Instantiates a new Ai bot.
     *
     * @param x       the x
     * @param y       the y
     * @param game    the game
     * @param map     the map
     * @param players the players
     */
    public aiBot(float x, float y, GameInstance game, int[][] map, List<Player> players) {
        this.x = x;
        this.y = y;
        this.game = game;
        this.netId = currentId++;
        this.navigationMap = map;
        this.lastUpdateTime = System.currentTimeMillis();
        this.players = players;
        aStar = new AStar(navigationMap);
    }


    /**
     * Update.
     */
    public void moveThread() {
        long currentTime = System.currentTimeMillis();

        int aiX = (int) (x / 32f);
        int aiY = (int) (y / 32f);
        float tileSize = 32f;
        int shortestLength = Integer.MAX_VALUE;
        List<AStar.Node> bestPath = null;

        for (Player player : players) {
            int playerX = (int) ((player.getX() + 16) / tileSize);
            int playerY = (int) ((player.getY() + 16) / tileSize);

            // Bounds check
            if (playerY < 0 || playerY >= navigationMap.length || playerX < 0 || playerX >= navigationMap[0].length)
                continue;

            // Skip invalid target
            if (navigationMap[playerY][playerX] != 0) continue;

            // Corrected path direction
            List<AStar.Node> potentialPath = aStar.findPath(aiX, aiY, playerX, playerY);

            if (potentialPath != null && potentialPath.size() < shortestLength) {
                shortestLength = potentialPath.size();
                bestPath = potentialPath;
            }
        }
        if (bestPath == null) {
            return;
        }

        if (!bestPath.isEmpty()) {
            bestPath.removeLast();
            if (!bestPath.isEmpty()) {
                AStar.Node node = bestPath.removeLast();
                targetX = node.x * tileSize;
                targetY = node.y * tileSize;
            }
        }

        float dx = targetX - x;
        float dy = targetY - y;


        float vx = 0, vy = 0;
        if (Math.abs(dx) >= stepSize || Math.abs(dy) >= stepSize) {
            if (Math.abs(dx) >= stepSize) vx = (dx > 0 ? 1 : -1) * speed;
            if (Math.abs(dy) >= stepSize) vy = (dy > 0 ? 1 : -1) * speed;
        }

        x += vx;
        y += vy;

        // Handle shooting logic
        if (currentTime - lastShootTime >= SHOOT_DELAY) {
            lastShootTime = currentTime; // Update last shoot time
            shoot(); // Call shoot method
        }
    }

    public void shoot() {
        for (Player player : players) {
            float playerX = player.getX();
            float playerY = player.getY();

            if (Math.abs(playerY - y) < 24) { // Check if both are at the same Y level or close enough
                if (playerX > x) {
                    game.addBullet(new Bullet(x + MONSTER_WIDTH_IN_PIXELS / 2, y + MONSTER_HEIGHT_IN_PIXELS / 2, Direction.RIGHT, 1000));
                } else if (playerX < x) {
                    game.addBullet(new Bullet(x + MONSTER_WIDTH_IN_PIXELS / 2, y + MONSTER_HEIGHT_IN_PIXELS / 2, Direction.LEFT, 1000));
                }
            }

            // Vertical shooting (same X level)
            if (Math.abs(playerX - x) < 24) { // Check if both are at the same X level or close enough
                if (playerY > y) {
                    game.addBullet(new Bullet(x + MONSTER_WIDTH_IN_PIXELS / 2, y + MONSTER_HEIGHT_IN_PIXELS / 2, Direction.UP, 1000));
                } else if (playerY < y) {
                    game.addBullet(new Bullet(x + MONSTER_WIDTH_IN_PIXELS / 2, y + MONSTER_HEIGHT_IN_PIXELS / 2, Direction.DOWN, 1000));
                }
            }
        }
    }

    /**
     * Thread-safe state snapshot
     *
     * @return the state
     */
    public aiBotState getState() {
        aiBotState state = new aiBotState();
        state.setId(netId);
        state.setX(x);
        state.setY(y);
        state.setLives(lives);
        return state;
    }

    public void tookDamage() {
        lives--;
    }
}
