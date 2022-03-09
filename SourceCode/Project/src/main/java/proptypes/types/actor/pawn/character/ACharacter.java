package proptypes.types.actor.pawn.character;

import proptypes.types.actor.pawn.APawn;
import utils.IDrawable;
import viewmodels.controls.ControlsModel;

/**
 * This is an abstract class for a controllable Actor object. Allows for direct control from User Controls.
 */
public abstract class ACharacter extends APawn implements IDrawable {

    private final ControlsModel controlsModel;

    /* Dictates whether or not the character has attempted to jump or not. This resets if the character collides with
     * an ALevelProp or presses the Jump button.
     */
    private boolean isJumpLocked = false;
    private final int MAX_ALLOWED_JUMP_TIME = 10;
    private int jumpTime = MAX_ALLOWED_JUMP_TIME;

    private final float MAX_ALLOWED_WALLRIDE_TIME = .7f;
    private float wallrideTime = MAX_ALLOWED_JUMP_TIME;

    protected ACharacter(ControlsModel cModel, float x, float y, float w, float h, float vx, float vy,
                         boolean hasGravity, float mass) {
        super(x, y, w, h, vx, vy, hasGravity, mass);
        this.controlsModel = cModel;
    }

    /**
     * The overloaded call to the parent class update method
     *
     * @param delta - The ratio of current update rate vs targetted framerate
     */
    protected void update(double delta) {
        super.update(delta);
    }

    /**
     * The direct call to movement and ability handlers
     */
    public void control() {
        doAbilitiy();
        doMove();
    }

    /**
     * This handles most conditions directly pertaining to Character movement based on User Input
     */
    public void doMove() {

        boolean[] directionals = controlsModel.getDirectionals();

        int xDir = (directionals[0] ? -1 : 0) + (directionals[1] ? 1 : 0);
        int yDir = (directionals[2] ? -1 : 0) + (directionals[3] ? 1 : 0);

        isUserControlled = directionals[0] || directionals[1] || directionals[2] || directionals[3];

        // If control direction goes against character movement direction, slow velocity down
        if (vX * xDir < 0) {
            vX *= .85; //.95
        }

        // Handle wall collisions with control input considered
        if ((xDir < 0 && isWallCollisionLeft) || (xDir > 0 && isWallCollisionRight)) {
            //Decrement y velocity using time
            vY -= (vY * wallrideTime);
            if (wallrideTime > 0) {
                wallrideTime -= .05f;
            }
        } else {
            // If jumping, reset the wallride
            if (isJumpLocked) {
                wallrideTime = MAX_ALLOWED_WALLRIDE_TIME;
            }
        }

        // Increment by a specific velocity from control input
        vX += xDir;
        vY += yDir;
    }

    /**
     * This handles most conditions pertaining to Character Abilities based on User Input
     */
    protected void doAbilitiy() {

        boolean[] abilities = controlsModel.getAbilities();

        if (jumpTime > 0) {
            jumpTime--;
        }

        if (abilities[0]) {

            if (isJumpLocked) {
                return;
            }

            if (jumpTime > 0 && (isFloorCollision || isWallCollisionLeft || isWallCollisionRight)) {

                lockJumpState(true);

                vY = -50;

                if (isFloorCollision) {

                    if (!isUserControlled) {
                        vX *= .25;
                    }
                    isFloorCollision = false;

                }
                if (isWallCollisionLeft) {

                    vX = 10;
                    isWallCollisionLeft = false;

                }
                if (isWallCollisionRight) {

                    vX = -10;
                    isWallCollisionRight = false;

                }
            }

        } else {

            lockJumpState(false);

        }

    }

    /**
     * Resets the jump window and sets the jump state.
     *
     * @param state - if the user is jumping or not
     */
    private void lockJumpState(boolean state) {
        isJumpLocked = state;

        jumpTime = MAX_ALLOWED_JUMP_TIME;
    }

}
