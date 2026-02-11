package ee.taltech.examplegame.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import ee.taltech.examplegame.util.Sprites;
import message.dto.BulletState;
import message.GameStateMessage;

import java.util.ArrayList;
import java.util.List;

import static constant.Constants.BULLET_HEIGHT_IN_PIXELS;
import static constant.Constants.BULLET_WIDTH_IN_PIXELS;


/**
 * Initialize a new Arena, which is responsible for updating and rendering the following: players, bullets, map.
 * Updating - modifying the inner state of objects (e.g. Player) based on game state messages received from the server.
 * Rendering - making the (updated) objects visible on the screen.
 */
public class Arena {

    private final List<Player> players = new ArrayList<>();
    private final World world;
    private List<BulletState> bullets = new ArrayList<>();
    private final Monster monster = new Monster();
    private final List<aiBot> aiBots = new ArrayList<>();

    public Arena(World world) {
        this.world = world;
    }

    /**
     * Update players and bullets, so they are later rendered in the correct position.
     *
     * @param gameStateMessage latest game state received from the server
     */
    public void update(GameStateMessage gameStateMessage) {
        gameStateMessage.getPlayerStates().forEach(playerState -> {
            var player = players
                .stream()
                .filter(p -> p.getId() == playerState.getId())
                .findFirst()
                .orElseGet(() -> {



                    // Set initial position


                    var newPlayer = new Player(world, playerState.getId(), playerState.getPlayerNumber());
                    newPlayer.setLocalPlayer(true);
                    players.add(newPlayer);
                    return newPlayer;
                });
            player.updateFromServer(playerState);


            player.setX(playerState.getX());
            player.setY(playerState.getY());
        });
        gameStateMessage.getAiBotStates().forEach(aiBotState -> {
            var aibot = aiBots.stream().filter(aiBot -> aiBot.getId() == aiBotState.getId())
                .findFirst()
                .orElseGet(() -> {
                    var newaiBot = new aiBot(aiBotState.getId());
                    aiBots.add(newaiBot);
                    return newaiBot;
                });

            aibot.setX(aiBotState.getX());
            aibot.setY(aiBotState.getY());
        });
        // Client-side: receiving the monster state and updating the position
        var monsterState = gameStateMessage.getMonsterState();
        monster.setX(monsterState.getX());
        monster.setY(monsterState.getY());



        this.bullets = gameStateMessage.getBulletStates();
    }

    /**
     * Calculates if the ghost is close enough. If he is then returns true, else false.
     */
    public boolean isMonsterCloseEnough() {
        float monsterX = monster.getX();
        float monsterY = monster.getY();
        for (Player player : players) {
            float distanceToPlayer = (float) Math.sqrt(
                (player.getX() - monsterX) * (player.getX() - monsterX) +
                    (player.getY() - monsterY) * (player.getY() - monsterY)
            );
            if (distanceToPlayer < 100) {
                return true;
            }
        }
        return false;
    }

    /**
     * Render map, players and bullets. This makes them visible on the screen.
     *
     * @param spriteBatch used for rendering (and scaling/resizing) all visual elements
     */
    public void render(SpriteBatch spriteBatch) {
        renderMap(spriteBatch);
        renderPlayers(spriteBatch);
        renderBullets(spriteBatch);
        renderAiBots(spriteBatch);
    }

    private void renderMap(SpriteBatch spriteBatch) {
        // TODO Map rendering here
    }

    private void renderMonster(SpriteBatch spriteBatch) {
        if (isMonsterCloseEnough()) {
            monster.render(spriteBatch);
        }
    }

    private void renderPlayers(SpriteBatch spriteBatch) {
        players.forEach(player -> player.render(spriteBatch));
    }

    private void renderAiBots(SpriteBatch spriteBatch) {
        aiBots.forEach(aiBot -> aiBot.render(spriteBatch));
    }

    private void renderBullets(SpriteBatch spriteBatch) {
        bullets.forEach(bullet -> {
            spriteBatch.draw(
                Sprites.bulletTexture,
                bullet.getX(),
                bullet.getY(),
                BULLET_WIDTH_IN_PIXELS,
                BULLET_HEIGHT_IN_PIXELS
            );
        });
    }


    public List<Player> getPlayers() {
        if (!players.isEmpty()) {
            return players;
        }
        return null;
    }
}
