package models.actors.gameactors.props.triggers.interactibles;

import models.actors.gameactors.props.player.PlayerAvatar;
import models.camera.Camera;
import models.environments.game.GameEnvironment;
import models.prototypes.actor.AActor;
import models.prototypes.level.prop.trigger.ATrigger;
import models.utils.config.ConfigData;
import models.utils.drawables.IDrawable;
import models.utils.drawables.IHUDDrawable;
import models.utils.resources.Resources;
import models.utils.updates.IUpdatable;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SpikesTrigger extends ATrigger implements IDrawable, IHUDDrawable, IUpdatable {
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
        float offsetX = ((x * ConfigData.scaledW_zoom) + (Camera.camX));
        float offsetY = ((y * ConfigData.scaledH_zoom) + (Camera.camY));

        float scaledW = w * ConfigData.scaledW_zoom;
        float scaledH = h * ConfigData.scaledH_zoom;

        BufferedImage img = Resources.getImage("spikes");
        float imgScaledH = scaledH/img.getHeight();

        if(scaledW < scaledH) {
            g.drawImage(img,
                    (int) (offsetX), (int) (offsetY),
                    (int) (scaledW), (int) (scaledH),
                    null);
            return;
        }

        float imgScaledW = img.getWidth() * imgScaledH;
        float numImgs = scaledW / imgScaledW;
        int i;
        for(i = 0; i < numImgs-1; i++) {
            g.drawImage(img,
                    (int) (offsetX + (i * imgScaledW)), (int) (offsetY),
                    (int) (imgScaledW), (int) (scaledH),
                    null);
        }
        float lastImgScale = numImgs-i;
        if(lastImgScale > 0) {
            g.drawImage(img,
                    (int) (offsetX + (i * imgScaledW)), (int) (offsetY),
                    (int) (lastImgScale * imgScaledW), (int) (scaledH),
                    null);
        }
    }
}
