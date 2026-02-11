package ee.taltech.examplegame.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import ee.taltech.examplegame.game.Arena;
import ee.taltech.examplegame.game.GameStateManager;
import ee.taltech.examplegame.game.Player;
import ee.taltech.examplegame.game.PlayerInputManager;
import ee.taltech.examplegame.game.WorldContactListener;
import ee.taltech.examplegame.network.ServerConnection;
import ee.taltech.examplegame.screen.overlay.Hud;
import message.GameStateMessage;
import message.LobbyLeaveMessage;
import message.PlaySoundMessage;
import message.PlayerShootingMessage;
import message.dto.Direction;

import static constant.Constants.ARENA_UPPER_BOUND_X;
import static constant.Constants.ARENA_UPPER_BOUND_Y;

public class GameScreen extends ScreenAdapter {

    private final Game game;
    private final GameStateManager gameStateManager;
    private final PlayerInputManager playerInputManager;

    private final SpriteBatch spriteBatch;
    private final Arena arena;
    private final Hud hud;

    public static OrthographicCamera camera;
    private final Viewport viewport;
    private final TmxMapLoader mapLoader;
    private final TiledMap map;
    private final OrthogonalTiledMapRenderer renderer;

    //Physics and gravity
    private final World world;
    private final Box2DDebugRenderer b2dr;


    /**
     * Initialize new GameScreen, where the main gameplay happens.
     *
     * @param game Current game instance, used for navigating between screens, for example GameScreen -> TitleScreen
     */
    private final Client client;
    private Player localPlayer;

    private Stage stage;

    // Camera smoothing variables
    private static final float CAMERA_LERP_FACTOR = 0.1f; // Adjust this value (0.1 = 10% movement per frame)
    private static final float PLAYER_CAMERA_OFFSET_X = 16f;
    private static final float PLAYER_CAMERA_OFFSET_Y = 16f;


    public GameScreen(Game game, Client client) {

        this.game = game;
        this.client = client;
        gameStateManager = new GameStateManager();
        playerInputManager = new PlayerInputManager();

        MusicControl musicControl = MusicControl.getInstance();
        musicControl.playMusic(Gdx.audio.newMusic(Gdx.files.internal("balada.mp3")));
        musicControl.setMusicLooping(true);

        spriteBatch = new SpriteBatch();  // where all the content is rendered
        world = new World(new Vector2(0, -10), true); //GRAVITY
        arena = new Arena(world);  // manages players and bullets
        hud = new Hud(spriteBatch);  // info overlay


        // COLLISIONS HOLD UP GUYS ITS HAPPENING AND GONNA WORK FO SHO

        world.setContactListener(new WorldContactListener());

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport = new StretchViewport(1000, 600, camera);
        this.mapLoader = new TmxMapLoader();
        this.map = mapLoader.load("level 1 v1 improvement.tmx");
        this.renderer = new OrthogonalTiledMapRenderer(map, 1);
        stage = new Stage(viewport, spriteBatch);
        Gdx.input.setInputProcessor(stage); // So it receives input

        //Physics

        this.b2dr = new Box2DDebugRenderer();

        // button sound player
        ServerConnection.getClient().addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof PlaySoundMessage) {
                    PlaySoundMessage msg = (PlaySoundMessage) object;
                    // Play the sound on client side
                    MusicControl.getInstance().playSoundById(msg.soundId);
                }
                // handle other messages here...
            }
        });

    }

    /**
     * Render a new frame.
     *
     * @param delta time since rendering the previous frame
     */
    @Override
    public void render(float delta) {
        super.render(delta);
        // clear the screen
        Gdx.gl.glClearColor(192 / 255f, 192 / 255f, 192 / 255f, 1); // to get that win 95 look
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//

        playerInputManager.handleMovementInput(); // send player movement info to server
        playerInputManager.handleShootingInput(delta); // send player shooting info to server
        handleScreenNavigation(game); // ESC - navigate back to Title screen
//

        // Handle input and update game state first
        playerInputManager.handleMovementInput();
        playerInputManager.handleShootingInput(delta);
        handleScreenNavigation(game);

        // Get current game state and update arena
        GameStateMessage currentGameState = gameStateManager.getLatestGameStateMessage();
        arena.update(currentGameState);
        hud.update(currentGameState);

        // Leiab lokaalse playeri keda jälitada
        if (localPlayer == null && arena.getPlayers() != null) {
            localPlayer = arena.getPlayers().stream()
                .filter(p -> p.getId() == client.getID())
                .findFirst()
                .orElse(null);

            if (localPlayer != null) {
                localPlayer.setLocalPlayer(true);
            }
        }

        // Kaamera jälitab playerit
        if (localPlayer != null) {
            float targetX = localPlayer.getX() + PLAYER_CAMERA_OFFSET_X;
            float targetY = localPlayer.getY() + PLAYER_CAMERA_OFFSET_Y;

            // Teeb natuke ühtlasemaks
            float adjustedLerp = CAMERA_LERP_FACTOR * Math.min(delta * 60f, 1f);
            camera.position.x += (targetX - camera.position.x) * adjustedLerp;
            camera.position.y += (targetY - camera.position.y) * adjustedLerp;

            keepCameraInBounds();
        }
        camera.update();
        float timeStep = 1 / 60f; // 60 updates per second
        int velocityIterations = 12; // Recommended for Box2D
        int positionIterations = 12; // Recommended for Box2D
        world.step(timeStep, velocityIterations, positionIterations);

        // Render everything with updated positions
        renderer.setView(camera);
        renderer.render();

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        arena.render(spriteBatch);  // all spriteBatch rendering should occur between .begin() and .end()


        arena.render(spriteBatch);
        spriteBatch.end();
        b2dr.render(world, camera.combined);
        this.b2dr.setDrawBodies(true);


        hud.render();
        stage.act(delta);
        stage.draw();
    }


    /**
     * Kaamera näitab nüüd ainult arenat (ei näita väljaspoolt)
     * Võtab info Constantidest shared kaustas (praegu hard coded)
     */
    private void keepCameraInBounds() {
        float halfViewportWidth = camera.viewportWidth / 2;
        float halfViewportHeight = camera.viewportHeight / 2;

        // X-axis bounds (working correctly)
        camera.position.x = Math.max(halfViewportWidth,
            Math.min(ARENA_UPPER_BOUND_X - halfViewportWidth,
                camera.position.x));

        // Y-axis bounds (fixed implementation)
        camera.position.y = Math.max(halfViewportHeight,
            Math.min(ARENA_UPPER_BOUND_Y - halfViewportHeight,
                camera.position.y));
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    private void handleScreenNavigation(Game game) {
        // navigate to TitleScreen
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            SettingsPopupInGame.openSettingsPopupInGame(stage, game, client);
        }
    }
    @Override
    public void dispose() {
        playerInputManager.dispose();
        // also dispose spriteBatch, world, etc. if needed
    }

}
