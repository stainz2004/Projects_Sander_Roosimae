package ee.taltech.examplegame.screen.overlay;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import message.GameStateMessage;
import message.dto.EnemyState;
import message.dto.PlayerState;

import java.util.List;

import static constant.Constants.ENEMY_LIVES_COUNT;
import static constant.Constants.PLAYER_LIVES_COUNT;
import static ee.taltech.examplegame.component.LabelComponents.createLabel;
import static ee.taltech.examplegame.component.LabelComponents.createLabelWithBackground;

public class Hud {

    private static final Integer LABEL_SIZE = 20;
    private static final Integer TABLE_PADDING_TOP = 20;
    public static final Integer GAME_STATUS_LABEL_PADDING_TOP = 150;
    public static final String INITIAL_PLAYER_LIVES_COUNT = String.valueOf(PLAYER_LIVES_COUNT);
    public static final String INITIAL_ENEMY_LIVES_COUNT = String.valueOf(ENEMY_LIVES_COUNT);

    private final Stage stage;

    // Initialize HUD labels with placeholder values, which can later be updated as the game state changes
    private final Label localPlayerNameLabel = createLabelWithBackground("You", Color.GREEN, LABEL_SIZE);
    private final Label timeLabel = createLabelWithBackground("0:00", Color.WHITE, LABEL_SIZE);
    private final Label remotePlayerNameLabel = createLabelWithBackground("Enemy", Color.WHITE, LABEL_SIZE);

    private final Label localPlayerLivesLabel = createLabelWithBackground(INITIAL_PLAYER_LIVES_COUNT, Color.RED, LABEL_SIZE);
    private final Label enemyLivesLabel = createLabelWithBackground(INITIAL_ENEMY_LIVES_COUNT, Color.RED, LABEL_SIZE);

    private final Label gameStatusLabel = createLabel("", Color.BLACK, LABEL_SIZE);

    /**
     * Info overlay that contains information about: player names, lives, game status, game time.
     *
     * @param spriteBatch used for rendering all visual elements. Using the same spritebatch as the Arena (players,
     *                    bullets etc.) helps with scaling and resizing
     */
    public Hud(SpriteBatch spriteBatch) {
        // The viewport with current hardcoded width and height works decently with most screen/window sizes
        // There's no need for additional font re-scaling when adjusting the window size
        Viewport viewport = new FitViewport(640, 480, new OrthographicCamera());

        // Create a stage to render the HUD content
        stage = new Stage(viewport, spriteBatch);

        // Create a table to display fields such as lives count
        Table table = createHudTable();
        table.setDebug(false);  // true - outline all table cells, labels with a red line (makes table non-transparent)
        stage.addActor(table);
    }

    /**
     * Table manages where different fields such as player names and lives are displayed on the screen
     */
    private Table createHudTable() {
        // For simple tables, using an empty placeholder label is usually the easiest solution to adjust alignment
        Label emptyLabel = createLabel("", Color.WHITE, LABEL_SIZE);

        Table table = new Table();
        table.setFillParent(true);  // Fill whole screen
        table.top();  // Align the table's content to the top
        table.padTop(TABLE_PADDING_TOP);

        // First row: player names and time
        table.add(localPlayerNameLabel);
        table.add(timeLabel);
        table.add(remotePlayerNameLabel);
        table.row().expandX();  // make the row fill the entire width of the screen

        // Second row: player lives
        table.add(localPlayerLivesLabel);
        table.add(emptyLabel);  // empty label as a placeholder for alignment
        table.add(enemyLivesLabel);
        table.row().expandX();

        // Third row: game status message
        table.add(gameStatusLabel)
            .colspan(3)  // Make the label span across all 3 table columns
            .padTop(GAME_STATUS_LABEL_PADDING_TOP)
            .align(Align.center);  // Center-align the label within the table cell
        table.row();

        return table;
    }

    /**
     * Stage, which contains all HUD content, must be re-rendered each frame, even if there are no changes.
     */
    public void render() {
        stage.draw();
    }

    /**
     * Update time, lives and game state (waiting for other players / active / game over).
     *
     * @param gameStateMessage latest game state received from the server
     */
    public void update(GameStateMessage gameStateMessage) {
        // TODO 7 Vastase elude uuendamine
        updateLives(gameStateMessage.getPlayerStates());
        updateTime(gameStateMessage.getGameTime());
        updateGameStatus(gameStateMessage);
    }


    private void updateLives(List<PlayerState> players) {
        localPlayerLivesLabel.setText(players.get(0).getLives());
        // TODO 7 Vastase elude uuendamine
    }

    private void updateTime(int gameTime) {
        int minutes = Math.floorDiv(gameTime, 60);
        int seconds = gameTime % 60;
        timeLabel.setText(minutes + ":" + String.format("%02d", seconds));
    }

    private void updateGameStatus(GameStateMessage gameState) {
        if (gameState.getPlayerStates().get(0).getLives() == 0) {
            gameStatusLabel.getStyle().fontColor = Color.RED;  // Make displayed game status message red
            gameStatusLabel.setText("You lost");
        }
        if (gameState.getEnemyStates().get(0).getLives() == 0) {
            gameStatusLabel.getStyle().fontColor = Color.GREEN;  // Make displayed game status message green
            gameStatusLabel.setText("You won");
        }
    }

}
