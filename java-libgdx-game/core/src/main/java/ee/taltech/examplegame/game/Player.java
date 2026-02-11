package ee.taltech.examplegame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import message.dto.Direction;
import message.dto.PlayerState;

import static constant.Constants.PLAYER_HEIGHT_IN_PIXELS;
import static constant.Constants.PLAYER_WIDTH_IN_PIXELS;

public class Player {
    private final int id;
    private float x;
    private float y;
    private Direction direction = Direction.RIGHT;

    private float stateTime;
    private float lastX; // Track the last X position to better detect movement

    // Walking animation assets
    private Texture walkSheet;
    private Animation<TextureRegion> walkLeftAnimation;
    private Animation<TextureRegion> walkRightAnimation;

    // Still frame assets (single image per character)
    private Texture stillSheet;
    private TextureRegion stillRightFrame;
    private TextureRegion stillLeftFrame;

    private Animation<TextureRegion> currentWalkAnimation; // Holds the active walking animation
    private boolean isMoving; // Flag to track if the player is actively moving
    private boolean wasMovingLastFrame; // To detect transition from still to moving

    // Movement threshold to avoid tiny movements triggering animations
    private static final float MOVEMENT_THRESHOLD = 0.5f;

    private boolean grounded;
    private Body body;
    private boolean isLocalPlayer;
    private int playerNumber;

    private static final int WALK_FRAME_COLS = 3;
    private static final int WALK_FRAME_ROWS = 2;

    public Player(World world, int id, int playerNumber) {
        this.id = id;
        this.x = 300;
        this.y = 50f;
        this.lastX = this.x; // Initialize lastX
        this.playerNumber = playerNumber;
        this.isMoving = false; // Player starts as not moving
        this.wasMovingLastFrame = false; // Initially not moving

        // --- Load Walking Sprites ---
        String walkSpritePath = (playerNumber == 1)
            ? "sprites/watergirl_walking_updated.png"
            : "sprites/charboy_walking222.png";
        walkSheet = new Texture(Gdx.files.internal(walkSpritePath));
        walkSheet.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        int walkActualFrameWidth = walkSheet.getWidth() / WALK_FRAME_COLS;
        int walkActualFrameHeight = walkSheet.getHeight() / WALK_FRAME_ROWS;

        TextureRegion[][] walkTmpFrames = TextureRegion.split(walkSheet,
            walkActualFrameWidth,
            walkActualFrameHeight);

        TextureRegion[] walkRightFrames = new TextureRegion[WALK_FRAME_COLS];
        TextureRegion[] walkLeftFrames = new TextureRegion[WALK_FRAME_COLS];

        for (int i = 0; i < WALK_FRAME_COLS; i++) {
            walkRightFrames[i] = walkTmpFrames[0][i];
            walkLeftFrames[i] = walkTmpFrames[1][i];
        }

        // Slower animation speed for smoother movement
        walkRightAnimation = new Animation<>(0.2f, walkRightFrames);
        walkLeftAnimation = new Animation<>(0.2f, walkLeftFrames);


        // --- Load Still Frame ---
        String stillSpritePath = (playerNumber == 1)
            ? "sprites/charGirlFlipped.png"
            : "sprites/charboy_still.png";
        stillSheet = new Texture(Gdx.files.internal(stillSpritePath));
        stillSheet.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        // Assuming the loaded still image is naturally right-facing or neutral
        stillRightFrame = new TextureRegion(stillSheet);
        stillLeftFrame = new TextureRegion(stillSheet);
        stillLeftFrame.flip(true, false); // Flip the texture region for left-facing

        // Set initial animation state (player is still, facing right)
        this.currentWalkAnimation = walkRightAnimation; // Default walk animation
        stateTime = 0f;

        // Debug message to verify animations loaded properly
        System.out.println("Player " + id + " initialized with " +
            walkRightAnimation.getKeyFrames().length + " animation frames");
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public Body getBody() {
        return body;
    }

    public void setPlayerNumber(int num) {
        this.playerNumber = num;
    }

    public int getId() {
        return id;
    }

    public float getY() {
        return y;
    }

    public float getX() {
        return x;
    }

    public void setX(float newX) {
        // Save the previous position before updating
        float oldX = this.x;
        this.x = newX;

        // Detect significant movement (using a threshold to prevent tiny movements)
        if (Math.abs(this.x - oldX) > MOVEMENT_THRESHOLD) {
            this.isMoving = true;
            if (newX > oldX) {
                this.direction = Direction.RIGHT;
            } else {
                this.direction = Direction.LEFT;
            }
        } else {
            // Small or no movement
            this.isMoving = false;
        }
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setLocalPlayer(boolean localPlayer) {
        isLocalPlayer = localPlayer;
    }

    public void render(SpriteBatch spriteBatch) {
        float delta = Gdx.graphics.getDeltaTime();
        TextureRegion frameToRender = null;

        // Check if player is moving (comparing with last rendered position)
        boolean isCurrentlyMoving = Math.abs(this.x - lastX) > MOVEMENT_THRESHOLD;
        this.isMoving = isCurrentlyMoving || this.isMoving;
        lastX = this.x; // Update the last position

        if (isMoving) {
            // Determine which animation to use based on direction
            if (direction == Direction.LEFT) {
                currentWalkAnimation = walkLeftAnimation;
            } else {
                currentWalkAnimation = walkRightAnimation;
            }

            // Always update stateTime when moving
            stateTime += delta;

            // Get the current animation frame
            frameToRender = currentWalkAnimation.getKeyFrame(stateTime, true);

            // Debug animation progression
            if (isLocalPlayer && Gdx.graphics.getFrameId() % 60 == 0) {
                System.out.println("Animation state: " + stateTime +
                    ", Frame: " + currentWalkAnimation.getKeyFrameIndex(stateTime) +
                    ", Direction: " + direction);
            }
        } else {
            // Not moving - use still frame
            frameToRender = (direction == Direction.LEFT) ? stillLeftFrame : stillRightFrame;
            // Don't reset stateTime when stopping, this allows animation to continue from current frame
        }

        // Safety check for null texture
        if (frameToRender == null) {
            System.err.println("Error: Null frame for player " + id +
                ", Direction: " + direction + ", Moving: " + isMoving);
            frameToRender = stillRightFrame; // Fallback
            if (frameToRender == null) return; // Skip rendering if still null
        }

        // Draw the sprite
        spriteBatch.draw(
            frameToRender,
            x - PLAYER_WIDTH_IN_PIXELS / 2f + 32,
            y - PLAYER_HEIGHT_IN_PIXELS / 2f + 16,
            PLAYER_WIDTH_IN_PIXELS,
            PLAYER_HEIGHT_IN_PIXELS
        );

        // If we've been moving but have now stopped
        if (wasMovingLastFrame && !isCurrentlyMoving) {
            this.isMoving = false; // Reset moving flag
        }

        wasMovingLastFrame = isMoving;
    }

    public void dispose() {
        if (walkSheet != null) walkSheet.dispose();
        if (stillSheet != null) stillSheet.dispose();
    }

    public void setDirection(Direction newDirection) {
        // Only update if direction has changed
        if (this.direction != newDirection) {
            this.direction = newDirection;
            // Don't reset stateTime here - let the animation continue for smoother transitions
        }
    }

    public Direction getDirection() {
        return direction;
    }

    public void updateFromServer(PlayerState state) {
        float oldX = this.x;
        this.x = state.getX();
        this.y = state.getY();

        if (Math.abs(this.x - oldX) > MOVEMENT_THRESHOLD) {
            this.isMoving = true;
            if (this.x > oldX) {
                this.direction = Direction.RIGHT;
            } else {
                this.direction = Direction.LEFT;
            }
        }
        // Don't automatically set isMoving to false here - we'll do that
        // in the render method after confirming no further movement
    }

    // Call this for the local player if movement input stops
    public void forceStopMoving() {
        // Apply a short delay before actually stopping animation
        if (isMoving) {
            // Keep isMoving true briefly so animation completes current cycle
            Gdx.app.postRunnable(() -> {
                isMoving = false;
            });
        }
    }

    public void setGrounded(boolean b) {
        this.grounded = b;
    }
}
