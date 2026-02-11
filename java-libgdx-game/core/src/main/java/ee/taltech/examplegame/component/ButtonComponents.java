package ee.taltech.examplegame.component;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;

import static com.badlogic.gdx.graphics.Color.*;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.color;


/**
 * Utility class for creating different types of buttons.
 * Provides methods to generate standard TextButtons.
 */
public class ButtonComponents {


    /**
     * This allows passing custom click behavior (function) into the getButton method as a lambda or method reference.
     * In short, for handling clicks.
     */
    public interface OnClickHandler {
        void handleClick();  // Defines the action (function) to be triggered when the button is clicked.
    }

    /**
     * Creates a text button with a specified font size and text.
     *
     * @param fontSize       Font size of the button text.
     * @param text           Text displayed on the button.
     * @param onClickHandler The action (function) to perform when the button is clicked.
     */
    public static TextButton getButton(int fontSize, String text, OnClickHandler onClickHandler) {
        // styling the button
        TextButtonStyle style = createButtonStyle(fontSize);
        TextButton button = createButtonWithPadding(text, style);

        // for handling clicks - a specific function is triggered with each click
        ClickListener clickListener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onClickHandler.handleClick();
                super.clicked(event, x, y);
            }
        };
        button.addListener(clickListener);
        return button;
    }

    public static ImageButton getImageButton(OnClickHandler onClickHandler, String texturePath, int width, int height) {
        // styling the button
        Texture texture = new Texture(Gdx.files.internal(texturePath));
        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(texture));
        ImageButton imageButton = new ImageButton(drawable);
        imageButton.setSize(width, height);
        imageButton.getImageCell().size(width, height);

        imageButton.getImage().setScaling(Scaling.fit);
        final boolean[] isGray = {false};

        imageButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isGray[0] = !isGray[0];

                Color targetColor = isGray[0] ? Color.GRAY : Color.WHITE;

                imageButton.addAction(color(targetColor, 0.1f));

                onClickHandler.handleClick();

                super.clicked(event, x, y);
            }
        });
        imageButton.setTouchable(Touchable.enabled);

        return imageButton;
    }

    private static TextButtonStyle createButtonStyle(int fontSize) {
        TextButtonStyle style = new TextButton.TextButtonStyle();

        // Load TTF font and convert it to BitmapFont
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/dogica.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = fontSize;
        parameter.color = WHITE;

        // Generate BitmapFont from TTF
        style.font = generator.generateFont(parameter);
        generator.dispose(); // Vabasta mälu pärast fonti loomist

        // Add button textures
        style.up = new TextureRegionDrawable(new TextureRegion(new Texture("button/up.png")));
        style.over = new TextureRegionDrawable(new TextureRegion(new Texture("button/down2.png")));

        return style;
    }

    public static void disableButton(TextButton button) {
        button.setTouchable(Touchable.disabled);
        button.getStyle().fontColor = Color.GRAY;
        button.getStyle().overFontColor = Color.GRAY;
    }

    public static void enableButton(TextButton button) {
        button.setTouchable(Touchable.enabled);
        button.getStyle().fontColor = Color.WHITE;
        button.getStyle().overFontColor = Color.WHITE;
    }

    private static TextButton createButtonWithPadding(String text, TextButtonStyle style) {
        TextButton button = new TextButton(text, style);
        button.pad(8);
        button.padLeft(14);
        button.padRight(14);
        return button;
    }

}
