package ee.taltech.examplegame.server.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.minlog.Log;
import ee.taltech.examplegame.server.TMXLoaders.HijackedTmxLoader;
import ee.taltech.examplegame.server.TMXLoaders.MyServer;
import ee.taltech.examplegame.server.game.object.Bullet;
import ee.taltech.examplegame.server.game.object.Player;
import ee.taltech.examplegame.server.game.object.Monster;
import ee.taltech.examplegame.server.game.object.aiBot;
import ee.taltech.examplegame.server.lobby.Lobby;
import message.PlaySoundMessage;


import java.time.Duration;
import java.util.*;

import static constant.Constants.GAME_TICK_RATE;
import static constant.Constants.PLAYER_COUNT_IN_GAME;

/**
 * Represents the game logic and server-side management of the game instance.
 * Handles player connections, game state updates, bullet collisions, and communication with clients.
 * <p>
 * This class extends {@link Thread} because the game loop needs to run continuously
 * in the background, independent of other server operations. By running in a separate thread,
 * it ensures that the game state updates at a fixed tick rate without blocking other processes in the main server.
 */
public class GameInstance extends Thread {

    private final Lobby server;
    private final BulletCollisionHandler collisionHandler = new BulletCollisionHandler();
    private final GameStateHandler gameStateHandler = new GameStateHandler();
    private final Map<Connection, Integer> selectedCharacters = new HashMap<>();

    private final Set<Connection> connections = new HashSet<>();  // Avoid a connection (player) joining the game twice
    private final List<Player> players = new ArrayList<>();
    private final World world;
    private List<Bullet> bullets = new ArrayList<>();
    private List<aiBot> aiBots = new ArrayList<>();
    private Monster monster = new Monster();
    private int[][] collisionMap;
    private boolean button1Pressed = false;
    private boolean button2Pressed = false;
    private boolean aiSpawned = false;
    private List<aiBot> removalBots = new ArrayList<>();



    boolean playedBothButtonsSound = false;

    private TiledMap tiledMap;
    private MapLayer mapLayer;
    private static final float PPM = 32f;


    /**
     * Initializes the game instance.
     *
     * @param server Reference to ServerListener to call dispose() when the game is finished or all players leave.
     * @param selectedCharacters Connection of the first player and his id.
     */
    public GameInstance(Lobby server, Map<Connection, Integer> selectedCharacters) {
        this.server = server;
        this.world = new World(new Vector2(0, -20f), true);
        Log.info("GameInstance created");
        initializeMap();
        initializeWorldEdgeCollision();
        for (Connection conn : selectedCharacters.keySet()) {
            Player newPlayer = new Player(conn, this, selectedCharacters.get(conn), world);
            players.add(newPlayer);
            connections.add(conn);
        }
        if (hasEnoughPlayers()) {
            gameStateHandler.setAllPlayersHaveJoined(true);
        }

        Log.info(String.valueOf(server.getConnections()));



        // Button listener if someone jumped on it
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Fixture a = contact.getFixtureA();
                Fixture b = contact.getFixtureB();

                String aData = (String) a.getUserData();
                String bData = (String) b.getUserData();

                if ((aData != null && bData != null)) {
                    checkButtonPress(a, b);
                    checkButtonPress(b, a);
                }

                if (button1Pressed && button2Pressed && !aiSpawned) {
                    aiSpawned = true;

                    for (Connection conn : server.getConnections()) {
                        conn.sendUDP(new PlaySoundMessage("button_press"));
                    }

//                    MusicControl.getInstance().playSound(buttonPressSound, 1f);
//                    MusicControl.getInstance().playSound(gameStart, 1f);
                    System.out.println("🎉 Both buttons pressed!");
                    for (Connection conn : server.getConnections()) {
                        conn.sendUDP(new PlaySoundMessage("game_start"));
                    }
                    generateAiBots();
                }
            }

            private void checkButtonPress(Fixture playerFixture, Fixture buttonFixture) {
                if (!"player".equals(playerFixture.getUserData())) return;

                Body playerBody = playerFixture.getBody();
                Body buttonBody = buttonFixture.getBody();

                float playerBottom = playerBody.getPosition().y - getHalfHeight(playerFixture);
                float buttonTop = buttonBody.getPosition().y + getHalfHeight(buttonFixture);

                float playerX = playerBody.getPosition().x;
                float buttonX = buttonBody.getPosition().x;
                float playerHalfWidth = getHalfWidth(playerFixture);
                float buttonHalfWidth = getHalfWidth(buttonFixture);

                boolean verticallyAligned = Math.abs(playerBottom - buttonTop) < 0.1f;
                boolean horizontallyAligned = Math.abs(playerX - buttonX) < (playerHalfWidth + buttonHalfWidth);

                if (verticallyAligned && horizontallyAligned) {
                    if ("button".equals(buttonFixture.getUserData())) {
                        button1Pressed = true;
                        for (Connection conn : server.getConnections()) {
                            conn.sendUDP(new PlaySoundMessage("button_press"));
                        }
                        System.out.println("✅ Player on top of button 1!");
                    } else if ("button2".equals(buttonFixture.getUserData())) {
                        button2Pressed = true;
                        for (Connection conn : server.getConnections()) {
                            conn.sendUDP(new PlaySoundMessage("button_press"));
                        }
                        System.out.println("✅ Player on top of button 2!");
                    }
                }
            }
            private float getHalfWidth(Fixture fixture) {
                if (fixture.getShape() instanceof PolygonShape shape) {
                    Vector2 v = new Vector2();
                    shape.getVertex(0, v);
                    return Math.abs(v.x);
                }
                return 0;
            }

            @Override
            public void endContact(Contact contact) {
                Fixture a = contact.getFixtureA();
                Fixture b = contact.getFixtureB();

                String aData = (String) a.getUserData();
                String bData = (String) b.getUserData();

                if ((aData != null && bData != null)) {
                    checkButtonRelease(a, b);
                    checkButtonRelease(b, a);
                }
            }

            private void checkButtonRelease(Fixture playerFixture, Fixture buttonFixture) {
                if (!"player".equals(playerFixture.getUserData())) return;

                if ("button".equals(buttonFixture.getUserData())) {
                    button1Pressed = false;
                    System.out.println("Player left button 1.");
                } else if ("button2".equals(buttonFixture.getUserData())) {
                    button2Pressed = false;
                    System.out.println("Player left button 2.");
                }
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
            }

            private float getHalfHeight(Fixture fixture) {
                if (fixture.getShape() instanceof PolygonShape shape) {
                    Vector2 v = new Vector2();
                    shape.getVertex(0, v);
                    return Math.abs(v.y);
                }
                return 0;
            }
        });
    }

    public void initializeWorldEdgeCollision() {
        float worldWidth = tiledMap.getProperties().get("width", Integer.class) * 32;  // Assuming tile width is 32
        float worldHeight = tiledMap.getProperties().get("height", Integer.class) * 30; // Assuming tile height is 32
        float worldWidthMeters = worldWidth / PPM;
        float worldHeightMeters = worldHeight / PPM;
        float margin = 50f / PPM;

        createEdge(0, 0, worldWidthMeters + margin, 0.5f);  // Bottom edge
        createEdge(0, worldHeightMeters + margin, worldWidthMeters + margin, 0.5f); // Top edge
        createEdge(0, 0, 0.5f, worldHeightMeters + margin); // Left edge
        createEdge(worldWidthMeters + margin, 0, 0.5f, worldHeightMeters + margin); // Right edge
    }



    private void createEdge(float x, float y, float width, float height) {
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.StaticBody; // Static boundary

        bdef.position.set(x + (width - 2) / 2 / PPM, y + (height - 1) / 2 / PPM);

        Body body = world.createBody(bdef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 / PPM, height / 2 / PPM);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.friction = 0.0f; // Adjust ground friction
        fdef.restitution = 0.0f; // Slight bounce?

        body.createFixture(fdef);
        shape.dispose();  // Clean up shape after use
    }


    public void initializeMap() {
        this.tiledMap = getMap();
        this.mapLayer = tiledMap.getLayers().get("Collisions");

        int mapWidth = tiledMap.getProperties().get("width", Integer.class);
        int mapHeight = tiledMap.getProperties().get("height", Integer.class);
        int tileWidth = tiledMap.getProperties().get("tilewidth", Integer.class);
        int tileHeight = tiledMap.getProperties().get("tileheight", Integer.class);

        collisionMap = new int[mapHeight][mapWidth];

        for (MapObject tileObject : mapLayer.getObjects().getByType(MapObject.class)) {

            // 1. Create Body Definition
            BodyDef bdef = new BodyDef();
            bdef.type = BodyDef.BodyType.StaticBody;

            // 2. Calculate Position and Dimensions
            float x = (float) tileObject.getProperties().get("x");
            float y = (float) tileObject.getProperties().get("y");
            float width = tileObject.getProperties().get("width", Float.class);
            float height = tileObject.getProperties().get("height", Float.class);

            // Convert from pixels to meters (PPM scaling)
            x /= PPM;  // Convert x position to meters
            y /= PPM;  // Convert y position to meters
            width /= PPM;  // Convert width to meters
            height /= PPM;  // Convert height to meters

            // Remove hard-coded offset or scale it properly
            bdef.position.set(x + (width - 2) / 2, (float) (y + (height - 1) / 2));

            // 3. Create Body and Shape
            Body body = world.createBody(bdef);
            PolygonShape shape = new PolygonShape();
            shape.setAsBox((width) / 2, (float) ((height) / 2));  // Define shape size in meters

            // 4. Create Fixture Definition
            FixtureDef fdef = new FixtureDef();
            fdef.shape = shape;
            fdef.density = 0.0f;  // Static body, no need for density
            fdef.friction = 0.0f;  // Standard friction value
            fdef.restitution = 0.0f;  // No bounce

            Fixture fixture = body.createFixture(fdef);
            fixture.setUserData("ground");  // Mark as ground object for collision handling

            // 5. Cleanup shape immediately after use
            shape.dispose();

            // Mark collision map areas as blocked based on tile position
            int startX = (int)Math.floor(x * PPM / tileWidth);
            int startY = (int)Math.floor(y * PPM / tileHeight);
            int endX = (int)Math.ceil((x * PPM + width * PPM) / tileWidth);
            int endY = (int)Math.ceil((y * PPM + height * PPM) / tileHeight);

            for (int i = startX; i < endX; i++) {
                for (int j = startY; j < endY; j++) {
                    if (i >= 0 && j >= 0) {
                        collisionMap[j][i] = 1; // Assuming [y][x]
                    }
                }
            }

        }
        // Button collision
        BodyDef bDef = new BodyDef();
        bDef.position.set(945f / PPM, 862f / PPM);
        bDef.type = BodyDef.BodyType.StaticBody;


        // 3. Create Body and Shape
        Body body = world.createBody(bDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((16f) / PPM, (48f) / PPM);

        // 4. Create Fixture Properly
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.density = 1f;
        fdef.friction = 10f;
        fdef.restitution = 0f;

        Fixture fixture = body.createFixture(fdef);
        fixture.setUserData("button");

        // 5. Cleanup shape immediately after use
        shape.dispose();

        // Button2 collision
        BodyDef bDef2 = new BodyDef();
        bDef2.position.set(110f / PPM, 862f / PPM);
        bDef2.type = BodyDef.BodyType.StaticBody;


        // 3. Create Body and Shape
        Body body2 = world.createBody(bDef2);
        PolygonShape shape2 = new PolygonShape();
        shape2.setAsBox((14f) / PPM, (16f) / PPM);

        // 4. Create Fixture Properly
        FixtureDef fdef2 = new FixtureDef();
        fdef2.shape = shape2;
        fdef2.density = 1f;
        fdef2.friction = 10f;
        fdef2.restitution = 0f;

        Fixture fixture2 = body2.createFixture(fdef2);
        fixture2.setUserData("button2");

        // 5. Cleanup shape immediately after use
        shape2.dispose();
    }

    public TiledMap getMap() {
        return new HijackedTmxLoader(new MyServer.MyFileHandleResolver())
            // Ubuntu: /home/ubuntu/iti0301-2021/Server/core/assets/DesertMap.tmx
            // Peeter's computer: C:/Users/37256/IdeaProjects/iti0301-2021/Server/core/assets/DesertMap.tmx
            // Your computer: Server/core/src/com/mygdx/game/World/DesertMap.tmx
            .load("level 1 v1Collisions.tmx");

    }

    public void addBullet(Bullet bullet) {
        this.bullets.add(bullet);
    }

    /**
     * Check if the game has the required number of players to start.
     */
    public boolean hasEnoughPlayers() {
        return connections.size() == 2;
    }

    public void removeConnection(Connection connection) {
        this.connections.remove(connection);
    }

    /**
     * Stops and disposes the current game instance, so a new one can be created with the same or new players.
     */
    private void disposeGame() {
        players.forEach(Player::dispose);  // remove movement and shooting listeners
        connections.clear();
        server.disposeGame();
    }

    private void generateAiBots() {
        this.aiBots.add(new aiBot(700, 760, this, collisionMap, players));
        this.aiBots.add(new aiBot(200, 760, this, collisionMap, players));
    }

    public void deleteAiFirst(aiBot aiBot) {
        aiBot.setX(-111);
        aiBot.setY(-111);
    }

    public void deleteAiSecond(aiBot aiBot) {
        aiBots.remove(aiBot);
    }

    /**
     * Game loop. Updates the game state, checks for collisions, and sends updates to clients.
     * The game loop runs until the game is stopped or no players remain.
     */
    @Override
    public void run() {
        boolean isGameRunning = true;


        while (isGameRunning) {
            gameStateHandler.incrementGameTimeIfPlayersPresent();

            // Safely get all bot states
            aiBots.forEach(aiBot::moveThread);
            removalBots = aiBots.stream().filter(aiBot -> aiBot.getLives() < 1).toList();
            removalBots.forEach(this::deleteAiFirst);

            // update bullets, check for collisions and remove out of bounds bullets
            bullets.forEach(Bullet::update);
            bullets = collisionHandler.handleCollisions(bullets, players, aiBots, collisionMap);

            float timeStep = 1 / 60f; // 60 updates per second
            int velocityIterations = 12; // Recommended for Box2D
            int positionIterations = 12; // Recommended for Box2D
            world.step(timeStep, velocityIterations, positionIterations);

            // construct gameStateMessage
            var gameStateMessage = gameStateHandler.getGameStateMessage(players, bullets, monster, aiBots);
            removalBots.forEach(this::deleteAiSecond);
            // send the state of current game to all connected clients
            connections.forEach(connection -> connection.sendUDP(gameStateMessage));
            players.forEach(Player::updatePlayer);

            // If any player is dead, end the game
            if (players.stream().anyMatch(x -> x.getLives() == 0) || aiSpawned && aiBots.isEmpty()) {
                // Use TCP to ensure that the last gameStateMessage reaches all clients
                connections.forEach(connection -> connection.sendTCP(gameStateMessage));
                disposeGame();
                isGameRunning = false;
            }
            // If no players are connected, stop the game loop
            if (connections.isEmpty() || connections.size() == 1) {
                Log.info("No players connected, stopping game loop.");
                disposeGame();
                isGameRunning = false;
            }

            try {
                // We don't want to update the game state every millisecond, that would be
                // too much for the server to handle. So a tick rate is used to limit the
                // amount of updates per second.
                Thread.sleep(Duration.ofMillis(1000 / GAME_TICK_RATE));
            } catch (InterruptedException e) {
                Log.error("Game loop sleep interrupted", e);
            }
        }
    }
}
