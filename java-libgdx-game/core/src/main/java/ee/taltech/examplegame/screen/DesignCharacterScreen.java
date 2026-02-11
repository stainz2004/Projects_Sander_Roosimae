package ee.taltech.examplegame.screen;

    // siin saadab sõnumi serverile

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;

public class DesignCharacterScreen extends ScreenAdapter {
    private final Game game;

    public DesignCharacterScreen(Game game) {
        this.game = game;
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
        // stage.act(delta);
        // stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        // stage.getViewport().update(width, height, true);
    }
}
