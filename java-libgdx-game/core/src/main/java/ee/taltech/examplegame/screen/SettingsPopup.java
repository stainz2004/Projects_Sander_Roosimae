package ee.taltech.examplegame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import ee.taltech.examplegame.screen.MusicControl;

public class SettingsPopup {

    private static Skin skin = new Skin(Gdx.files.internal("pixthulhu-ui.json"));
    private static Image dimOverlay;
    private static Window settingsWindow;

    public static void openSettingsPopup(Stage stage) {
        // Create a new window
        settingsWindow = new Window("", skin);
        TextArea settingsText = new TextArea("Settings", skin);
        settingsText.setDisabled(true);

        // Create a slider to control volume
        final Slider volumeSlider = new Slider(0f, 1f, 0.01f, false, skin);
        volumeSlider.setValue(MusicControl.getInstance().getMusicVolume());

        final Label volumeLabel = new Label("Volume: " + (int)(volumeSlider.getValue() * 100) + "%", skin);

        // Volume slider listener
        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float newVolume = volumeSlider.getValue();
                MusicControl.getInstance().setMusicVolume(newVolume);
                volumeLabel.setText("Volume: " + (int)(newVolume * 100) + "%");
            }
        });

        final SelectBox<String> resolutionDropdown = new SelectBox<>(skin);
        resolutionDropdown.setItems("800x600", "1024x768", "1280x720", "1600x900", "1920x1080", "Fullscreen");
        resolutionDropdown.setSelected("800x600"); // Default selected option

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

        // Close button
        TextButton closeButton = new TextButton("Close", skin);
        closeButton.getLabel().setFontScale(0.8f); // Make text smaller
        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settingsWindow.remove();
                if (dimOverlay != null) dimOverlay.remove();
            }
        });

        // Layout in the settings window
        settingsWindow.add(settingsText).fillX().padBottom(10).row(); // Add "Settings" at top
        settingsWindow.add(volumeLabel).padBottom(10).row(); // Add volume label below settings
        settingsWindow.add(volumeSlider).width(400).padBottom(10).row();
        settingsWindow.add(new Label("Resolution", skin)).row();
        settingsWindow.add(resolutionDropdown).width(350).padBottom(10).row();
        settingsWindow.add(applyResolutionButton).padBottom(20).row(); // Add volume slider below label
        settingsWindow.add(closeButton).padTop(10); // Add close button last

        settingsWindow.pack();
        settingsWindow.setSize(500, 600);
        settingsWindow.setPosition(
            (stage.getWidth() - settingsWindow.getWidth()) / 2f,
            (stage.getHeight() - settingsWindow.getHeight()) / 2f
        );
        settingsWindow.setModal(true); // block input behind
        settingsWindow.setMovable(true); // allow dragging
        settingsWindow.setResizable(false); // optional
        settingsWindow.setKeepWithinStage(true); // prevent going off-screen

        dimOverlay = new Image(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("black-pixel.png")))));
        dimOverlay.setSize(stage.getWidth(), stage.getHeight());
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

    }
    public static boolean isOpen() {
        return settingsWindow != null && settingsWindow.isVisible();
    }

    public static void resizeOverlayAndWindow(Stage stage) {
        if (dimOverlay != null) {
            dimOverlay.setSize(stage.getWidth(), stage.getHeight());
        }
        if (settingsWindow != null) {
            settingsWindow.setPosition(
                (stage.getWidth() - settingsWindow.getWidth()) / 2f,
                (stage.getHeight() - settingsWindow.getHeight()) / 2f
            );
        }
    }
}
