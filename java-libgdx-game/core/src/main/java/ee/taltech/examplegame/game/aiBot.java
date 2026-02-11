package ee.taltech.examplegame.game;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ee.taltech.examplegame.util.Sprites;
import static constant.Constants.AIBOT_WIDTH_IN_PIXELS;
import static constant.Constants.AIBOT_HEIGHT_IN_PIXELS;

public class aiBot{

    private float x;
    private float y;
    private int id;
    private boolean delete;


    public aiBot(int id) {
        this.id = id;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public int getId() {
        return id;
    }

    public void deleteAi() {

    }

    public void render(SpriteBatch spriteBatch) {
        spriteBatch.draw(Sprites.monster, x, y, AIBOT_WIDTH_IN_PIXELS, AIBOT_HEIGHT_IN_PIXELS);
    }
}
