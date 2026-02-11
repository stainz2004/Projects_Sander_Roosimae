package ee.taltech.examplegame.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.esotericsoftware.kryonet.Client;
import ee.taltech.examplegame.network.ServerConnection;
import message.LobbyLeaveMessage;


public class SettingsPopupInGame {

    private static Skin skin = new Skin(Gdx.files.internal("pixthulhu-ui.json"));
    private static Window settingsWindow; // store the popup instance
    private static Image dimOverlay;


    public static void openSettingsPopupInGame(Stage stage, Game game, Client client) {
        // Create a new window
        if (settingsWindow != null && settingsWindow.hasParent()) {
            return; // Already open
        }

        settingsWindow = new Window("", skin);
        settingsWindow.setSize(500, 600);
        TextArea settingsText = new TextArea("Settings", skin);
        settingsText.setDisabled(true);

        // Music volume
        final Slider musicSlider = new Slider(0f, 1f, 0.01f, false, skin);
        musicSlider.setValue(MusicControl.getInstance().getMusicVolume());
        final Label musicLabel = new Label("Music: " + (int)(musicSlider.getValue() * 100) + "%", skin);

// Sound FX volume
        final Slider fxSlider = new Slider(0f, 1f, 0.01f, false, skin);
        fxSlider.setValue(MusicControl.getInstance().getSoundVolume());
        final Label fxLabel = new Label("Sound FX: " + (int)(fxSlider.getValue() * 100) + "%", skin);


        musicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float newVolume = musicSlider.getValue();
                MusicControl.getInstance().setMusicVolume(newVolume);
                musicLabel.setText("Music: " + (int)(newVolume * 100) + "%");
            }
        });

        fxSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float newVolume = fxSlider.getValue();
                MusicControl.getInstance().setSoundVolume(newVolume);
                fxLabel.setText("Sound FX: " + (int)(newVolume * 100) + "%");
            }
        });

        final SelectBox<String> resolutionDropdown = new SelectBox<>(skin);
        resolutionDropdown.setItems("800x600", "1024x768", "1280x720", "1600x900", "1920x1080", "Fullscreen");
        resolutionDropdown.setSelected("1280x720"); // Default selected option

        TextButton applyResolutionButton = new TextButton("Apply Resolution", skin);
        applyResolutionButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String selected = resolutionDropdown.getSelected();
                if (selected.equals("Fullscreen")) {
                    // Set to fullscreen mode
                    Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
                } else {
                    // Switch to windowed mode with selected resolution
                    String[] parts = selected.split("x");
                    int width = Integer.parseInt(parts[0]);
                    int height = Integer.parseInt(parts[1]);
                    Gdx.graphics.setWindowedMode(width, height);
                }
            }
        });


        TextButton closeButton = new TextButton("Exit", skin);
        closeButton.getLabel().setFontScale(0.8f); // Match Close button font scale
        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new TitleScreen(game, client));
                ServerConnection.getInstance();
                ServerConnection.getClient().sendTCP(new LobbyLeaveMessage());
            }
        });

        TextButton backButton = new TextButton("Back", skin);
        backButton.getLabel().setFontScale(0.8f); // Match Close button font scale
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (settingsWindow != null) settingsWindow.remove();
                if (dimOverlay != null) dimOverlay.remove();
            }
        });

        // Layout in the settings window
        settingsWindow.add(settingsText).fillX().padBottom(10).row(); // Add "Settings" at top

        Table volumeTable = new Table();
        volumeTable.defaults().pad(5);

        volumeTable.add(musicLabel).left();
        volumeTable.add(fxLabel).left().padLeft(20).row();

        volumeTable.add(musicSlider).width(250);
        volumeTable.add(fxSlider).width(250).padLeft(20).row();

        settingsWindow.add(volumeTable).padBottom(20).row();

        settingsWindow.add(new Label("Resolution", skin)).row();
        settingsWindow.add(resolutionDropdown).width(350).padBottom(10).row();
        settingsWindow.add(applyResolutionButton).padBottom(10).row();

        Table buttonTable = new Table();
        buttonTable.defaults().padLeft(10).padRight(10).width(150); // Wider like "Close" button
        buttonTable.add(backButton);
        buttonTable.add(closeButton);
          // Ensure same size as in the other SettingsPopup

        settingsWindow.add(buttonTable).padTop(10).row();

        settingsWindow.pack();
        settingsWindow.setWidth(600);
        settingsWindow.setHeight(600);
        settingsWindow.setPosition(
            (stage.getWidth() - settingsWindow.getWidth()) / 2f,
            (stage.getHeight() - settingsWindow.getHeight()) / 2f
        );
        settingsWindow.setModal(true); // block input behind
        settingsWindow.setMovable(true); // allow dragging
        settingsWindow.setResizable(false); // optional
        settingsWindow.setKeepWithinStage(true); // prevent going off-screen

        dimOverlay = new Image(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("black-pixel.png")))));
        dimOverlay.setSize(stage.getWidth(), stage.getHeight() + 100);
        dimOverlay.getColor().a = 0.5f; // 50% opacity
        dimOverlay.setTouchable(Touchable.disabled); // let clicks pass through

        stage.addActor(dimOverlay);  // Add before window

        // Optional but recommended:
        stage.setKeyboardFocus(settingsWindow);

        stage.addActor(settingsWindow);

        // Add a resize listener to update the window position when the stage resizes
        stage.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (settingsWindow != null && settingsWindow.isVisible()) {
                    settingsWindow.setPosition(
                        (stage.getWidth() - settingsWindow.getWidth()) / 2f,
                        (stage.getHeight() - settingsWindow.getHeight()) / 2f
                    );
                    dimOverlay.setSize(stage.getWidth(), stage.getHeight());
                }
            }
        });
        stage.setKeyboardFocus(settingsWindow);
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    if (settingsWindow != null) settingsWindow.remove();
                    if (dimOverlay != null) dimOverlay.remove();
                    return true;
                }
                return false;
            }
        });
    }
}
