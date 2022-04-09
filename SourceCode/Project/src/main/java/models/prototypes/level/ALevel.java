package models.prototypes.level;

import models.environments.game.GameEnvironment;
import models.levels.inventory.LevelCollectibles;
import models.prototypes.actor.AActor;
import models.prototypes.level.prop.ALevelProp;
import models.prototypes.level.prop.trigger.ATrigger;
import models.utils.config.ConfigData;
import models.utils.drawables.IDrawable;
import models.utils.drawables.IHUDDrawable;
import models.utils.updates.IUpdatable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class ALevel implements IDrawable, IHUDDrawable, IUpdatable {

    protected BufferedImage backgroundImage;

    protected GameEnvironment gameModel;

    protected int[] startOrigin = new int[2];

    protected final ArrayList<ALevelProp> levelProps = new ArrayList<>();

    protected LevelCollectibles collectibles;

    public ALevel(GameEnvironment gameModel) {
        this.gameModel = gameModel;
    }

    protected void addProp(ALevelProp prop) {
        levelProps.add(prop);
    }

    public ArrayList<ALevelProp> getLevelProps() {
        return levelProps;
    }

    public int[] getCharacterOrigin() {
        return startOrigin;
    }

    public void setStartOrigin(int x, int y) {
        startOrigin = new int[]{x, y};
    }

    protected void setBackgroundImage(BufferedImage backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public abstract void build();

    public void reset() {
        for(ALevelProp p: levelProps) {
            if(p instanceof ATrigger t) {
                t.reset();
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(
                backgroundImage,
                0,
                0,
                ConfigData.window_width_actual,
                ConfigData.window_height_actual,
                null);

        for (AActor levelProps : getLevelProps()) {
            if (levelProps instanceof ALevelProp p) {
                p.draw(g);
            }
        }
    }

    @Override
    public void update(float delta) {
        /*
        for(ALevelProp p: levelProps) {
            if(p instanceof SpringTrigger s) {
                s.update(delta);
            }
        }
        */
    }

    @Override
    public void drawAsHUD(Graphics g) {
        g.setColor(new Color(50, 50,50));
        g.fillRect(0, 0, ConfigData.window_width_actual, ConfigData.window_height_actual);

        for (AActor levelProps : getLevelProps()) {
            if (levelProps instanceof ALevelProp p) {
                p.drawAsHUD(g);
            }
        }
    }
}
