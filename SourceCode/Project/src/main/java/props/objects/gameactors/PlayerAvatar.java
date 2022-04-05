package props.objects.gameactors;

import models.camera.Camera;
import models.controls.GameControlsModel;
import prototypes.actor.AActor;
import prototypes.actor.pawn.character.ACharacter;
import utils.config.ConfigData;
import utils.files.Resources;

import java.awt.*;

/**
 * TODO: Add description
 */
public class PlayerAvatar extends ACharacter {

    /**
     * Instantiates a new Test character.
     *
     * @param cModel     the c model
     * @param x          the x
     * @param y          the y
     * @param w          the w
     * @param h          the h
     * @param vx         the vx
     * @param vy         the vy
     * @param hasGravity the has gravity
     */
    public PlayerAvatar(GameControlsModel cModel, float x, float y, float w, float h, float vx, float vy,
                        boolean hasGravity) {
        super(cModel, x - w, y - h, w, h, vx, vy, hasGravity);

        spriteSheets.put(ActionType.FLOOR_IDLE, Resources.loadSpriteSheet("avatarrun_spritesheet"));
        spriteSheets.put(ActionType.FLOOR_WALKING, Resources.loadSpriteSheet("avatarrun_spritesheet"));
        spriteSheets.put(ActionType.FLOOR_RUNNING, Resources.loadSpriteSheet("avatarrun_spritesheet2"));
        spriteSheets.put(ActionType.FLOOR_JUMPING, Resources.loadSpriteSheet("button_spritesheet"));
        spriteSheets.put(ActionType.WALL_CLIMBING, Resources.loadSpriteSheet("avatarrun_spritesheet2"));
        spriteSheets.put(ActionType.WALL_JUMPING, Resources.loadSpriteSheet("avatarrun_spritesheet2"));
    }

    @Override
    public boolean hasCollision(AActor a, float delta) {
        return super.hasCollision(a, delta);
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if(vX < 0) {
            facing = Facing.RIGHT;
        } else if (vX > 0) {
            facing = Facing.LEFT;
        }

        float tx =
                (float)(((ConfigData.window_width_actual * .5) - (w * ConfigData.scaledW)) - (x * ConfigData.scaledW));
        float ty =
                (float)(((ConfigData.window_height_actual * .5) - (h * ConfigData.scaledH)) - (y * ConfigData.scaledH));

        Camera.moveTo(tx, ty);

        float tickRate = delta;
        if(vX != 0) {
            tickRate *= ((MAX_VEL_X*.5f)/(Math.abs(vX) + (MAX_VEL_X * .5)));
        }
        if(isUserControlled) {
            spriteSheets.get(actionState).update(tickRate * (MAX_VEL_X * .5f / Math.abs(vX)));
        } else {
            spriteSheets.get(actionState).setCurrentFrame(0);
        }
    }

    @Override
    public void draw(Graphics g) {
        //super.draw(g);

        // Scaled size
        float scaleW = w * ConfigData.scaledW;
        float scaleH = h * ConfigData.scaledH;

        //Half window width
        float centerX = (x * ConfigData.scaledW) + (Camera.x) + scaleW;
        float centerY = (y * ConfigData.scaledH) + (Camera.y) + scaleH;

        centerX -= scaleW;
        centerY -= scaleH;

        if (facing == Facing.RIGHT) {
            centerX += scaleW;
            scaleW *= -1;
        }

        //g.fillRect((int) (centerX), (int) (centerY), (int) (scaleW), (int) (scaleH));
        // TODO: RE-ENABLE THE FOLLOWING
        /*
        if(characterType == CharacterType.MALE) {
            g.drawImage(
                    Resources.getImage("avatar"),
                    (int) (centerX), (int) (centerY),
                    (int) (scaleW), (int) (scaleH),
                    null);
        } else {
            g.drawImage(
                    Resources.getImage("avatar2"),
                    (int) (centerX), (int) (centerY),
                    (int) (scaleW), (int) (scaleH),
                    null);
        }
        */

        spriteSheets.get(actionState).draw(g, (int)centerX, (int)centerY, (int)scaleW, (int)scaleH);

        //g.drawString("TC", (int) (centerX) + 3, (int) (centerY) + 12);

    }

    public String toString() {
        return "TC:  VX= " + (int)vX + ", VY= " + (int)vY;
    }

}
