package ee.taltech.examplegame.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.esotericsoftware.kryonet.Client;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;


import javax.swing.event.ChangeEvent;


import static ee.taltech.examplegame.component.ButtonComponents.getButton;

/**
 * TitleScreen represents the main menu of the game, where players can choose to start the game or exit.
 * It listens for user input and directs user to different screens, for example TitleScreen -> GameScreen.
 */
public class TitleScreen extends ScreenAdapter {
    private final Stage stage;
    private final Texture backgroundTexture;
    Music backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("tacata.mp3"));


    public TitleScreen(Game game, Client client) {
        stage = new Stage(new ScreenViewport()); // Use ScreenViewport instead of StretchViewport
        Gdx.graphics.setWindowedMode(800, 600);
        //Starts playing background music
        MusicControl musicControl = MusicControl.getInstance();

        musicControl.playMusic(Gdx.audio.newMusic(Gdx.files.internal("tacata.mp3")));
        musicControl.setMusicLooping(true);

        // NB! important line - will make the stage listen for user input
        // For example when this is not set no hover or click events will be triggered
        Gdx.input.setInputProcessor(stage);

        // Load background
        backgroundTexture = new Texture(Gdx.files.internal("menu_bg.png"));
        Image backgroundImage = new Image(new TextureRegionDrawable(backgroundTexture));
        backgroundImage.setFillParent(true); // Ensure that the background fills the entire screen.
        stage.addActor(backgroundImage); // Add the background first.

        // menu buttons
        // start viib praegu mängu, aga kui lobby tööle saada, siis viib lobbysse ja lobbyst juba saab mängu
        var startButton = getButton(16, "Start", () -> {
            game.setScreen(new LobbySelectionScreen(game, client));
        });
        var exitButton = getButton(16, "Exit", () -> Gdx.app.exit());
        var settingsButton = getButton(16, "Settings", () -> SettingsPopup.openSettingsPopup(stage));

        // positioning the buttons. you can think of the following as a table (or flexbox) in HTML
        var table = new Table();
        table.setFillParent(true);
        table.add(startButton).padBottom(20);
        table.row();
        table.add(settingsButton).padBottom(20);
        table.row();
        table.add(exitButton);

        stage.addActor(table);
    }


    /**
     * Renders the TitleScreen, clearing it and drawing the buttons.
     *
     * @param delta time since last frame.
     */
    @Override
    public void render(float delta) {
        super.render(delta);
        // clear the screen
        Gdx.gl.glClearColor(192 / 255f, 192 / 255f, 192 / 255f, 1); // to get that win 95 look
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // draw the buttons
        stage.act(delta);
        stage.draw();
    }


    @Override
    public void hide() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
            backgroundMusic.dispose(); // clean up if no longer needed
        }
    }


    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        stage.getViewport().update(width, height, true);
        // Optional: reposition popup window and resize dimOverlay if they're visible
        if (SettingsPopup.isOpen()) {
            SettingsPopup.resizeOverlayAndWindow(stage);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        stage.dispose();
        backgroundTexture.dispose(); // Ensure that the texture is freed from memory.
    }
}
