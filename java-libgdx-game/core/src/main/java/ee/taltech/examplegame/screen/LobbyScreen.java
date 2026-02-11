package ee.taltech.examplegame.screen;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import ee.taltech.examplegame.network.ServerConnection;
import ee.taltech.examplegame.network.listener.LobbyDataMessageListener;
import ee.taltech.examplegame.network.listener.SelectedCharactersListener;
import ee.taltech.examplegame.network.listener.SwitchToGameScreenListener;
import ee.taltech.examplegame.network.listener.SwitchToLobbySelectionScreenListener;
import message.*;

import java.util.List;

import static com.badlogic.gdx.graphics.Color.WHITE;
import static ee.taltech.examplegame.component.ButtonComponents.*;

public class LobbyScreen extends ScreenAdapter {
    private final Stage stage;
    private final Client client;
    private Game game;
    private final Texture backgroundTexture;

    private ImageButton chooseCharacterButtonBoy;
    private ImageButton chooseCharacterButtonGirl;
    private Button startGameButton;

    public LobbyScreen(Game game, Client client) {
        this.game = game;
        this.client = client;
        stage = new Stage();

        FreeTypeFontGenerator generator =
            new FreeTypeFontGenerator(Gdx.files.internal("fonts/dogica.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter =
            new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = 16;              // pick a pixel size that looks good
        parameter.color = Color.WHITE;    // you can also set color here

        BitmapFont dogicaFont = generator.generateFont(parameter);
        generator.dispose();

        Label.LabelStyle style = new Label.LabelStyle(dogicaFont, Color.WHITE);

        style.font.getData().markupEnabled = true; // if you want markup

        Label chooseCharacter = new Label("Choose your character", style);

        backgroundTexture = new Texture(Gdx.files.internal("menu_bg.png"));
        Image backgroundImage = new Image(new TextureRegionDrawable(backgroundTexture));
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage);

        Gdx.input.setInputProcessor(stage);

        SelectedCharactersListener selectedCharactersListener =
            new SelectedCharactersListener(this);

        SwitchToGameScreenListener switchToGameScreenListener = new SwitchToGameScreenListener(this);
        SwitchToLobbySelectionScreenListener switchToLobbySelectionScreenListener = new SwitchToLobbySelectionScreenListener(this);

        client.addListener(selectedCharactersListener);
        client.addListener(switchToGameScreenListener);
        client.addListener(switchToLobbySelectionScreenListener);

        this.chooseCharacterButtonBoy = getImageButton(() -> {
            ServerConnection.getInstance().getClient().sendTCP(new CharacterSelectionMessage(1));
        }, "charBoy2.png", 96, 128);

        this.chooseCharacterButtonGirl = getImageButton(() -> {
            ServerConnection.getInstance().getClient().sendTCP(new CharacterSelectionMessage(2));
        }, "charGirl.png", 128, 128);

        this.startGameButton = getButton(16, "Start game", () -> {
            ServerConnection.getInstance().getClient().sendTCP(new PlayerReadyMessage());
            disableButton((TextButton) startGameButton);
        });

        var goBackButton = getButton(16, "Back", () -> {
            game.setScreen(new LobbySelectionScreen(game, client));
            ServerConnection.getInstance().getClient().sendTCP(new LobbyLeaveMessage());
        });


        var table = new Table();
        table.setFillParent(true);
        table.center();
        table.defaults().center();

        table.add(chooseCharacter)
            .colspan(2)
            .padTop(30)
            .center();
        table.row();

// First row: Character buttons with uniform cells and no extra horizontal spacing
        table.add(chooseCharacterButtonBoy)
            .uniform()
            .padBottom(10);

        table.add(chooseCharacterButtonGirl)
            .uniform()
            .padBottom(20);

        table.row();

// Second row: "Start game" button spanning both columns and centered
        table.add(startGameButton)
            .colspan(2)
            .padBottom(20)
            .center();

        table.row();

// Third row: "Back" button spanning both columns and centered
        table.add(goBackButton)
            .colspan(2)
            .center();

        stage.addActor(table);

    }

    public void updateSelectedCharacters(List<Integer> selectedIds) {
        Gdx.app.postRunnable(() -> {
            boolean boyTaken = selectedIds.contains(1);
            chooseCharacterButtonBoy.getStyle().imageUp = boyTaken
                ? new TextureRegionDrawable(
                new TextureRegion(new Texture("charboy2gray.png"))
            )
                : new TextureRegionDrawable(
                new TextureRegion(new Texture("charBoy2.png"))
            );
            chooseCharacterButtonBoy.setDisabled(boyTaken);

            boolean girlTaken = selectedIds.contains(2);
            chooseCharacterButtonGirl.getStyle().imageUp = girlTaken
                ? new TextureRegionDrawable(
                new TextureRegion(new Texture("charGirlGray.png"))
            )
                : new TextureRegionDrawable(
                new TextureRegion(new Texture("charGirl.png"))
            );
            chooseCharacterButtonGirl.setDisabled(girlTaken);
        });
    }

    private void chooseCharacter(int playerNumber) {
        ServerConnection.getInstance().getClient().sendTCP(new CharacterSelectionMessage(playerNumber));

    }

    public void setToGameScreen() {
        game.setScreen(new GameScreen(game, client));
    }

    public void setToLobbySelectionScreen() {
        LobbySelectionScreen selection = new LobbySelectionScreen(game, client);
        selection.setTextToPlayerLeaveMessage();
        game.setScreen(selection);
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
    public void resize(int width, int height) {
        super.resize(width, height);
        // stage.getViewport().update(width, height, true);
    }

}
