package ee.taltech.examplegame.screen;

// siin saadab sõnumi serverile

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.esotericsoftware.kryonet.Client;
import ee.taltech.examplegame.game.LobbyDataManager;
import ee.taltech.examplegame.network.ServerConnection;
import ee.taltech.examplegame.network.listener.LobbyDataMessageListener;
import ee.taltech.examplegame.network.listener.SwitchToLobbyScreenListener;
import message.GameJoinMessage;
import message.LobbyDataMessage;
import message.LobbyJoinMessage;
import message.LobbyLeaveMessage;

import java.util.Objects;

import static ee.taltech.examplegame.component.ButtonComponents.*;

public class LobbySelectionScreen extends ScreenAdapter {
    private final Stage stage;
    private final Game game;
    private final Client client;
    private TextButton joinLobbyButton1;
    private TextButton joinLobbyButton2;
    private TextButton joinLobbyButton3;
    private final Texture backgroundTexture;
    private int joinedLobby;
    private String playerLeaveMessage = "Your teammate has left the lobby.\n" +"     Choose your lobby again.";
    private Label playerLeftMessage;



    private final LobbyDataManager lobbyDataManager = new LobbyDataManager(this);

    public LobbySelectionScreen(Game game, Client client) {
        this.client = client;
        stage = new Stage(new ScreenViewport()); // Use ScreenViewport instead of StretchViewport


        backgroundTexture = new Texture(Gdx.files.internal("menu_bg.png"));
        Image backgroundImage = new Image(new TextureRegionDrawable(backgroundTexture));
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage);

        Gdx.input.setInputProcessor(stage);
        this.game = game;
        ServerConnection.getInstance().getClient().sendTCP(new GameJoinMessage());

        SwitchToLobbyScreenListener switchToLobbyScreenListener = new SwitchToLobbyScreenListener(this);
        client.addListener(switchToLobbyScreenListener);


        FreeTypeFontGenerator generator =
            new FreeTypeFontGenerator(Gdx.files.internal("fonts/dogica.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter =
            new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = 16;              // pick a pixel size that looks good
        parameter.color = Color.WHITE;    // you can also set color here
        parameter.spaceY = 8;

        BitmapFont dogicaFont = generator.generateFont(parameter);
        generator.dispose();

        Label.LabelStyle style = new Label.LabelStyle(dogicaFont, Color.WHITE);

        style.font.getData().markupEnabled = true; // if you want markup

        playerLeftMessage = new Label(
            "Choose your lobby",
            style
        );
        playerLeftMessage.setWidth(3000f);


        joinLobbyButton1 = getButton(16, "Lobby 1", () -> {
            if (joinedLobby != 0) {
                client.sendTCP(new LobbyLeaveMessage(joinedLobby));
            }
            client.sendTCP(new LobbyJoinMessage(0));
            joinedLobby = 0;
            disableButton(joinLobbyButton1);

            joinLobbyButton1.setColor(Color.GRAY);
            joinLobbyButton2.setColor(Color.WHITE);
            joinLobbyButton3.setColor(Color.WHITE);
        });

        joinLobbyButton2 = getButton(16, "Lobby 2", () -> {
            if (joinedLobby != 1) {
                client.sendTCP(new LobbyLeaveMessage(joinedLobby));
            }
            joinedLobby = 1;

            client.sendTCP(new LobbyJoinMessage(1));
            disableButton(joinLobbyButton2);
            joinLobbyButton2.setColor(Color.GRAY);
            joinLobbyButton1.setColor(Color.WHITE);
            joinLobbyButton3.setColor(Color.WHITE);
        });

        joinLobbyButton3 = getButton(16, "Lobby 3", () -> {
            if (joinedLobby != 2) {
                client.sendTCP(new LobbyLeaveMessage(joinedLobby));
            }
            joinedLobby = 2;

            client.sendTCP(new LobbyJoinMessage(2));
            disableButton(joinLobbyButton3);
            joinLobbyButton3.setColor(Color.GRAY);
            joinLobbyButton1.setColor(Color.WHITE);
            joinLobbyButton2.setColor(Color.WHITE);
        });

        var goBackButton = getButton(16, "Back", () -> {
            game.setScreen(new TitleScreen(game, client));
        });
        var settingsButton = getButton(16, "Settings", () -> SettingsPopup.openSettingsPopup(stage));

        var table = new Table();
        table.add(playerLeftMessage)
            .colspan(1)
            .padBottom(30)
            .center();
        table.row();
        table.setFillParent(true);
        table.add(joinLobbyButton1).padBottom(20);
        table.row();
        table.add(joinLobbyButton2).padBottom(20);
        table.row();
        table.add(joinLobbyButton3).padBottom(20);
        table.row();
        table.add(settingsButton).padBottom(10);
        table.row();
        table.add(goBackButton);

        stage.addActor(table);
    }

    public void setToLobbyScreen() {
        game.setScreen(new LobbyScreen(game, client));
    }

    public void setTextToPlayerLeaveMessage() {
        playerLeftMessage.setText(playerLeaveMessage);

        playerLeftMessage.invalidate();
        playerLeftMessage.layout();

        float h = playerLeftMessage.getPrefHeight();
        playerLeftMessage.setHeight(h);
    }

    public void updateButtons(LobbyDataMessage lobbyDataMessage) {
        joinLobbyButton1.setText("Lobby 1 " + lobbyDataMessage.playerCount.get(0) + "/" + lobbyDataMessage.maxPlayers.get(0));
        joinLobbyButton2.setText("Lobby 2 " + lobbyDataMessage.playerCount.get(1) + "/" + lobbyDataMessage.maxPlayers.get(1));
        joinLobbyButton3.setText("Lobby 3 " + lobbyDataMessage.playerCount.get(2) + "/" + lobbyDataMessage.maxPlayers.get(2));

        if (Objects.equals(lobbyDataMessage.playerCount.get(0), lobbyDataMessage.maxPlayers.get(0))) {
            disableButton(joinLobbyButton1);
        } else {
            enableButton(joinLobbyButton1);
        }

        if (Objects.equals(lobbyDataMessage.playerCount.get(1), lobbyDataMessage.maxPlayers.get(1))) {
            disableButton(joinLobbyButton2);
        } else {
            enableButton(joinLobbyButton2);
        }

        if (Objects.equals(lobbyDataMessage.playerCount.get(2), lobbyDataMessage.maxPlayers.get(2))) {
            disableButton(joinLobbyButton3);
        } else {
            enableButton(joinLobbyButton3);
        }

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
        stage.getViewport().update(width, height, true);
        // Optional: reposition popup window and resize dimOverlay if they're visible
        if (SettingsPopup.isOpen()) {
            SettingsPopup.resizeOverlayAndWindow(stage);
        }
    }

    @Override
    public void show() {
        // Lisame listeneri, mis kuulab LobbyDataMessage sõnumeid
        client.addListener(new LobbyDataMessageListener(lobbyDataManager));
    }
}
