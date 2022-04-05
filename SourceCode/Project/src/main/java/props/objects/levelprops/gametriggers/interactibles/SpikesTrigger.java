package props.objects.levelprops.gametriggers.interactibles;

import models.camera.Camera;
import models.environments.game.GameEnvironment;
import props.objects.gameactors.PlayerAvatar;
import prototypes.actor.AActor;
import prototypes.level.prop.trigger.ATrigger;
import utils.config.ConfigData;
import utils.files.Resources;

import java.awt.*;

public class SpikesTrigger extends ATrigger {
    /**
     * Instantiates a new A trigger.
     *
     * @param gameEnvironment    the game model
     * @param x                  the x
     * @param y                  the y
     * @param w                  the w
     * @param h                  the h
     * @param vx                 the vx
     * @param vy                 the vy
     * @param MAX_CYCLES
     */
    public SpikesTrigger(GameEnvironment gameEnvironment, float x, float y, float w, float h, float vx, float vy,
                         int MAX_CYCLES) {
        super(gameEnvironment, x, y, w, h, vx, vy, MAX_CYCLES, false, false);
    }

    @Override
    public void doAction() {
        gameEnvironment.reset();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
    }

    @Override
    public boolean hasCollision(AActor a, float delta) {
        if(!(a instanceof PlayerAvatar)) {
            return false;
        }

        boolean hasCollision = super.hasCollision(a, delta);

        if(MAX_CYCLES != -1) {
            if (currentCycles > MAX_CYCLES) {
                return false;
            }
        }

        if(hasCollision) {
            doAction();
            currentCycles++;
        }

        return hasCollision;
    }

    @Override
    public void draw(Graphics g) {
        double offsetX = ((x * ConfigData.scaledW) + (Camera.x));
        double offsetY = ((y * ConfigData.scaledH) + (Camera.y));

        double scaledW = w * ConfigData.scaledW;
        double scaledH = h * ConfigData.scaledH;

        g.drawImage(Resources.getImage("spikes"), (int)offsetX, (int)offsetY, (int)scaledW, (int)scaledH, null);
    }
}
