package models.utils.physics;

import models.levels.LevelsList;
import models.prototypes.actor.AActor;
import models.utils.config.ConfigData;
import models.utils.updates.IUpdatable;

public abstract class APhysics implements IUpdatable {

    public static final float GRAVITY = 9.8f / (float) ConfigData.GAME_UPDATE_RATE;
    protected boolean hasGravity = true;
    protected float x, y, w, h;

    protected final float MAX_VEL_X = 9.8f;
    protected final float MAX_VEL_Y = 9.8f;
    protected final float friction = .5f;
    protected float vX, vY;

    protected final float bufferVert = 5, bufferHoriz = 5;

    protected boolean isMoving;
    protected boolean isFloorCollision,  isWallCollisionLeft, isWallCollisionRight;

    protected boolean isUserControlled;

    protected APhysics(
            float x, float y,
            float w, float h,
            float vX, float vY,
            boolean hasGravity) {

        setPosition(x, y);
        setSize(w, h);
        setVelocity(vX, vY);
        setGravity(hasGravity);

    }

    public void update(float delta) {
        resetCollisions();

        updateVelocity(delta);
    }

    private void resetCollisions() {
        isFloorCollision = false;
        isWallCollisionLeft = false;
        isWallCollisionRight = false;
    }

    private void calculateGravity(float delta) {
        if (hasGravity && !isFloorCollision) {
            vY += (GRAVITY / delta);
        }
    }

    private void updateVelocity(float delta) {

        calculateGravity(delta);

        float acc = 1 - (friction / ConfigData.GAME_UPDATE_RATE / delta);

        //vY *= acc;
        vX *= acc;

        if(!(isMoving = Math.abs(vX) > .1f)) {
            vX = 0;
        }

        limitVelocity();

    }

    public void limitVelocity() {
        if (vY > MAX_VEL_Y * 5f) {
            vY = MAX_VEL_Y * 100f;
        } else if (vY < -MAX_VEL_Y) {
            vY = -MAX_VEL_Y;
        }
        if (vX > MAX_VEL_X) {
            vX = MAX_VEL_X;
        } else if (vX < -MAX_VEL_X) {
            vX = -MAX_VEL_X;
        }
    }

    public boolean hasCollision(AActor a, float delta) {
        return hasCollision(a, delta, true);
    }

    public boolean hasCollision(AActor a, float delta, boolean moveToBounds) {

        boolean isFloorBounded = ((a.bottomBufferOuter()) >= top()) && (a.bottomBufferInner() <= bottom());

        /*
        boolean isWallBoundedLeft =
                !isFloorBounded && ((a.leftBufferOuter() >= right()) && (a.leftBufferInner() <= left()));

        boolean isWallBoundedRight =
                !isFloorBounded && ((a.rightBufferInner() <= left()) && (a.rightBufferOuter() >= right()));
        */

        // Determine the conditions of the object collision
        boolean hitBottom =
                ((a.top() <= bottom()) && (a.top() >= top())) ||
                        ((bottom() >= a.top()) && (bottom() <= a.bottom()));
        boolean hitTop =
                ((a.bottom() >= top()) && (a.bottom() <= bottom())) ||
                        ((top() <= a.bottom()) && (top() >= a.top()));
        boolean hitLeft =
                ((a.right() >= left()) && (a.right() <= right())) ||
                        ((left() <= a.right()) && (left() >= a.left()));
        boolean hitRight =
                ((a.left() <= right()) && (a.left() >= left())) ||
                        ((right() >= a.left()) && (right() <= a.right()));

        if ((hitBottom || hitTop) && (hitLeft || hitRight)) {

            if(!moveToBounds) {
                return true;
            }

            // Determine the side that the object should rebound off of
            float distX, distY;
            if (hitBottom) {
                distY = Math.abs(a.top() - bottom());
            } else {
                distY = Math.abs(a.bottom() - top());
            }
            if (hitLeft) {
                distX = Math.abs(a.right() - left());
            } else {
                distX = Math.abs(a.left() - right());
            }

            if (distX > distY) {
                if (hitTop) {
                    a.y = top() - a.h;

                    a.isFloorCollision = true;

                    if (!a.isUserControlled) {
                        a.vX *= .9f;
                    }
                } else {
                    a.y = bottom();
                }

                a.vY = 0;

            } else if (distX < distY) {
                if (hitRight) {
                    a.x = right();
                    a.isWallCollisionLeft = true;
                } else {
                    a.x = left() - a.w;
                    a.isWallCollisionRight = true;
                }

                a.vX = 0;

            }

            return true;
        }

        return false;

    }

    private void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    private void setSize(float w, float h) {
        this.w = w;
        this.h = h;
    }

    protected void setGravity(boolean hasGravity) {
        this.hasGravity = hasGravity;
    }

    public void setVelocity(float velocityX, float velocityY) {
        this.vX = velocityX;
        this.vY = velocityY;
    }

    public void setVX(int vX) {
        this.vX = vX;
    }

    public void setVY(int vY) {
        this.vY = vY;
    }

    protected float top() {
        return y;
    }

    protected float bottom() {
        return top() + h;
    }

    protected float left() {
        return x;
    }

    protected float right() {
        return left() + w;
    }

    protected float leftBufferInner() {
        return left() + bufferHoriz;
    }

    protected float leftBufferOuter() {
        return left() - bufferHoriz;
    }

    protected float rightBufferInner() {
        return right() + bufferHoriz;
    }

    protected float rightBufferOuter() {
        return right() - bufferHoriz;
    }

    protected float bottomBufferInner() {
        return bottom() - bufferVert;
    }

    protected float bottomBufferOuter() {
        return bottom() + bufferVert;
    }

}
