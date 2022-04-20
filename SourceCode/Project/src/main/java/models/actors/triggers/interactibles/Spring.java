package models.actors.triggers.interactibles;

import models.actors.player.PlayerAvatar;
import models.camera.Camera;
import models.environments.game.GameEnvironment;
import models.prototypes.actor.AActor;
import models.prototypes.actor.pawn.character.ACharacter;
import models.prototypes.level.prop.trigger.ATrigger;
import models.sprites.SpriteSheet;
import models.utils.config.Config;
import models.utils.drawables.IDrawable;
import models.utils.drawables.IHUDDrawable;
import models.utils.resources.Resources;
import models.utils.updates.IUpdatable;

import java.awt.*;
import java.util.HashMap;

/**
 *
 */
public class Spring extends ATrigger implements IDrawable, IHUDDrawable, IUpdatable {

    private ActionType actionState = ActionType.IDLE;

    protected HashMap<ActionType, SpriteSheet> spriteSheets = new HashMap<>();

    /**
     * <p></p>
     * @param resources -
     * @param gameEnvironment -
     * @param x -
     * @param y -
     * @param w -
     * @param h -
     * @param vx -
     * @param vy -
     * @param MAX_CYCLES -
     * @param hasGravity -
     * @param canMoveOnCollision -
     */
    public Spring(Resources resources, GameEnvironment gameEnvironment, float x, float y, float w, float h, float vx,
                  float vy,
                  int MAX_CYCLES, boolean hasGravity, boolean canMoveOnCollision) {
        super(resources, gameEnvironment, x, y, w, h, vx, vy, MAX_CYCLES, hasGravity,
                canMoveOnCollision);

        spriteSheets.put(actionState, resources.getSpriteSheet("spritesheet_spring"));
    }

    @Override
    public boolean hasCollision(AActor a, float delta) {
        boolean hasCollision = super.hasCollision(a, delta);

        if(MAX_CYCLES != -1) {
            if (currentCycles > MAX_CYCLES) {
                return false;
            }
        }

        if(hasCollision && a.isFalling()) {
            lastActor = a;

            doAction();
            currentCycles++;
        }

        return hasCollision;
    }

    @Override
    public void doAction() {
        lastActor.setVY(-100);
        if(lastActor instanceof PlayerAvatar p) {
            p.setAction(ACharacter.ActionType.FLOOR_JUMPING);
            p.getCurrentSpriteSheet().reset();
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);
    }

    @Override
    public void draw(Graphics2D g) {
        float offsetX = ((x * Config.scaledW_zoom) + (Camera.camX));
        float offsetY = ((y * Config.scaledH_zoom) + (Camera.camY));

        float scaledW = w * Config.scaledW_zoom;
        float scaledH = h * Config.scaledH_zoom;

        g.setColor(new Color(255, 100, 100, 50));
        g.fillRect((int) ((offsetX)), (int) (offsetY), (int) (scaledW), (int) (scaledH));
        g.setColor(new Color(100, 255, 100));
        g.drawRect((int) ((offsetX)), (int) (offsetY), (int) (scaledW), (int) (scaledH));
        g.setColor(Color.BLACK);
        g.drawString("Spring Trigger", (int) (offsetX) + 3, (int) (offsetY) + 12);
    }

    @Override
    public void drawAsHUD(Graphics2D g) {

    }

    /**
     * <p></p>
     */
    private enum ActionType {
        IDLE
    }

}
