package proptypes.types.actor.pawn;

import proptypes.types.actor.AActor;
import utils.IDrawable;

import java.awt.*;

/**
 * TODO: Add description
 */
public abstract class APawn extends AActor implements IDrawable {

    protected APawn(float x, float y, float w, float h, float vx, float vy, boolean hasGravity, float mass) {
        super(x, y, w, h, vx, vy, hasGravity, mass);
    }

    @Override
    protected void update(double delta) {
        super.update(delta);

        x += vX / delta;
        y += vY / delta;

    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
    }

}
