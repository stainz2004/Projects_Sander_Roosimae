package ee.taltech.examplegame.server.game.object;

import lombok.Getter;
import lombok.Setter;
import message.dto.MonsterState;

import java.util.Random;

import static constant.Constants.*;

@Getter
@Setter
public class Monster {

    private float x = 100;
    private float y = 100;
    private int lives = MONSTER_LIVES_COUNT;


    /**
     * Monster when shot moves to random location.
     */
    public void move() {
        Random random = new Random();
        x = random.nextFloat(0, 300);
        y = random.nextFloat(0, 300);
    }

    /**
     * Monster follows player to greater lengths.
     */
    public void follow(Player player1, Player player2) {
        float player1x = player1.getX() - 5;
        float player2x = player2.getX() - 5;
        float player1y = player1.getY() + 5;
        float player2y = player2.getY() + 5;
        float difference = 0.2f;

        float distanceToPlayer1 = (float) Math.sqrt(
            (player1x - this.x) * (player1x - this.x) +
                (player1y - this.y) * (player1y - this.y)
        );

        float distanceToPlayer2 = (float) Math.sqrt(
            (player2x - this.x) * (player2x - this.x) +
                (player2y - this.y) * (player2y - this.y)
        );

        if (distanceToPlayer1 < distanceToPlayer2) {
            followPlayer1(player1, difference);
        } else {
            followPlayer2(player2, difference);
        }
    }

    /**
     * Following player2.
     */
    public void followPlayer2(Player player2, float difference) {
        float player2x = player2.getX() - 5;
        float player2y = player2.getY() + 5;
        if (Math.abs(player2x - this.x) > difference) {
            if (player2x > this.x) {
                this.x += 0.4f;
            } else {
                this.x -= 0.4f;
            }
        }
        if (Math.abs(player2y - this.y) > difference) {
            if (player2y > this.y) {
                this.y += 0.4f;
            } else {
                this.y -= 0.4f;
            }
        }
    }

    /**
     * Following player1.
     */
    public void followPlayer1(Player player1, float difference) {
        float player1x = player1.getX() - 5;
        float player1y = player1.getY() + 5;
        if (Math.abs(player1x - this.x) > difference) {
            if (player1x > this.x) {
                this.x += 0.4f;
            } else {
                this.x -= 0.4f;
            }
        }
        if (Math.abs(player1y - this.y) > difference) {
            if (player1y > this.y) {
                this.y += 0.4f;
            } else {
                this.y -= 0.4f;
            }
        }
    }


    public void decreaseLives() {
        if (lives > 0) {
            lives--;
        }
    }

    /**
     * Returns the current state of the monster, consisting of their position and remaining lives.
     */
    public MonsterState getState() {
        MonsterState monsterState = new MonsterState();
        monsterState.setX(x);
        monsterState.setY(y);
        monsterState.setLives(lives);
        return monsterState;
    }
}
