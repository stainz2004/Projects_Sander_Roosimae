package ee.taltech.examplegame.server.game.object;

import lombok.Getter;
import lombok.Setter;
import message.dto.BulletState;
import message.dto.Direction;

import static constant.Constants.BULLET_SPEED;

@Getter
@Setter
public class Bullet {
    private final Direction direction;
    private float x;
    private float y;
    private int shotById;

    public Bullet(float x, float y, Direction direction, int shotByPlayerWithId) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.shotById = shotByPlayerWithId;
    }

    public void update() {
        if (shotById != 1000) {
            switch (direction) {
                case UP -> y += BULLET_SPEED;
                case DOWN -> y -= BULLET_SPEED;
                case LEFT -> x -= BULLET_SPEED;
                case RIGHT -> x += BULLET_SPEED;
            }
        } else {
            switch (direction) {
                case UP -> y += BULLET_SPEED / 2;
                case DOWN -> y -= BULLET_SPEED / 2;
                case LEFT -> x -= BULLET_SPEED / 2;
                case RIGHT -> x += BULLET_SPEED / 2;
            }
        }
    }

    public BulletState getState() {
        BulletState bulletState = new BulletState();
        bulletState.setX(x);
        bulletState.setY(y);
        bulletState.setDirection(direction);
        return bulletState;
    }
}
