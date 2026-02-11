package ee.taltech.examplegame.component;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Align;

import static ee.taltech.examplegame.util.Font.getArialFont;

/**
 * Utility class for creating different types of labels for the HUD.
 * Provides methods to generate standard labels or labels with a background.
 */
public class LabelComponents {

    /**
     * Creates a simple label with the specified text, color, and font size.
     */
    public static Label createLabel(String text, Color textColor, int fontSize) {
        BitmapFont font = getArialFont(fontSize);
        LabelStyle labelStyle = new LabelStyle(font, textColor);
        return new Label(text, labelStyle);
    }

    /**
     * Creates a label with centered text, and specified font properties.
     * Quite hacky, but it works.
     */
    public static Label createLabelWithBackground(String text, Color textColor, int fontSize) {
        BitmapFont font = getArialFont(fontSize);
        LabelStyle labelStyle = new LabelStyle(font, textColor);

        Label label = new Label(text, labelStyle);
        label.setAlignment(Align.center); // Align text to the center of the background

        return label;
    }

}
