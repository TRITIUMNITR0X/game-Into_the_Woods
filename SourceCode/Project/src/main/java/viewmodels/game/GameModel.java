package viewmodels.game;

import controls.game.GameMouseControls;
import props.gameactors.TestActor;
import props.gameactors.TestCharacter;
import props.levelactors.TestLevelPropStatic;
import proptypes.actors.levelactors.animated.ALevelProp;
import proptypes.types.actor.AActor;
import utils.AMouseController;
import viewmodels.controls.ControlsModel;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class GameModel {

    private final LevelModel levelModel = new LevelModel();

    private ControlsModel controlsViewModel;

    private final ArrayList<AActor> gameObjects = new ArrayList<>();

    public void init(ControlsModel controlsViewModel) {
        this.controlsViewModel = controlsViewModel;

        // Main Test Character
        gameObjects.add(new TestCharacter(
                controlsViewModel,
                200,50,
                50,50,
                0,0,
                true,
                1f
        ));

        // Wall
        levelModel.addProp(new TestLevelPropStatic(0 , 0, 100, 1080, 0, 0, false, 0));
        levelModel.addProp(new TestLevelPropStatic(0 , 0, 100, 1080, 0, 0, false, 0));
        // Climbing Walls
        levelModel.addProp(new TestLevelPropStatic(320 , -150, 50, 200, 0, 0, false, 0));
        levelModel.addProp(new TestLevelPropStatic(500 , 0, 50, 200, 0, 0, false, 0));
        levelModel.addProp(new TestLevelPropStatic(320 , 220, 50, 200, 0, 0, false, 0));
        levelModel.addProp(new TestLevelPropStatic(500 , 360, 50, 200, 0, 0, false, 0));
        levelModel.addProp(new TestLevelPropStatic(320 , 500, 50, 200, 0, 0, false, 0));

        levelModel.addProp(new TestLevelPropStatic(9000 , 0, 100, 1080, 0, 0, false, 0));
        // Floor
        levelModel.addProp(new TestLevelPropStatic(0 , 980, 10000, 100, 0, 0, false, 0));

        // Other Props
        levelModel.addProp(new TestLevelPropStatic(1800 , 100, 500, 100, 0, 0, false, 0));
        levelModel.addProp(new TestLevelPropStatic(70 , 800, 500, 100, 0, 0, false, 0));
        levelModel.addProp(new TestLevelPropStatic(500 , 700, 500, 100, 0, 0, false, 0));
        levelModel.addProp(new TestLevelPropStatic(1100 , 600, 500, 100, 0, 0, false, 0));

        levelModel.addProp(new TestLevelPropStatic(1800 , 600, 500, 50, 0, 0, false, 0));
        levelModel.addProp(new TestLevelPropStatic(2000 , 650, 220, 100, 0, 0, false, 0));
        levelModel.addProp(new TestLevelPropStatic(3500 , 750, 500, 100, 0, 0, false, 0));
        levelModel.addProp(new TestLevelPropStatic(4000 , 780, 200, 100, 0, 0, false, 0));

        levelModel.addProp(new TestLevelPropStatic(6800 , 400, 500, 50, 0, 0, false, 0));
        levelModel.addProp(new TestLevelPropStatic(2500 , 720, 220, 100, 0, 0, false, 0));
        levelModel.addProp(new TestLevelPropStatic(3000 , 700, 500, 50, 0, 0, false, 0));
        levelModel.addProp(new TestLevelPropStatic(3200 , 650, 100, 100, 0, 0, false, 0));
    }

    public void addGameObject(AActor gameObject) {
        gameObjects.add(gameObject);
    }

    public synchronized void update(double delta) {

        // Mouse Input (Adding Game Objects)
        AMouseController mouseController = controlsViewModel.getMouseController();
        if (mouseController instanceof GameMouseControls) {

            GameMouseControls gmc = (GameMouseControls) mouseController;
            if (gmc.isLeftPressed()) {
                int count = (int)(1/delta);
                if(count < 1) {
                    count = 1;
                }
                for(int i = 0; i < count; i++) {
                    addGameObject(
                        new TestActor(
                                (float)(gmc.getPos()[0] - WorldModel.offX),
                                (float)(gmc.getPos()[1] - WorldModel.offY),
                                50f,
                                50f,
                                new Random().nextFloat(-100, 100),
                                new Random().nextFloat(-100, 100),
                               true,
                               1f
                        )
                    );
                }
            }
        }

        // Update the Game Objects
        updateGameObjects(delta);

        // Check Game Object Collisions with Level Props
        checkCollisions();

    }

    public void updateGameObjects(double updateRate) {

        // Update all Actors
        for(AActor gameObject: gameObjects) {

            // Update TestActors
            if(gameObject instanceof TestActor a) {
                a.update(updateRate);
            }

            // Update Characters
            if(gameObject instanceof TestCharacter tc) {
                tc.control();
                tc.update(updateRate);
            }

        }

    }

    private void checkCollisions() {
        for(ALevelProp p: levelModel.getLevelProps()) {
            for(AActor a: gameObjects) {
                p.hasCollision(a);
            }
        }
    }

    public synchronized void renderGameObjects(Graphics g) {
        for(AActor gameObject: gameObjects) {
            if(gameObject instanceof TestCharacter o) {
                //if(o.isInFrameBounds()) {
                    o.draw(g);
                //}
            }
            if(gameObject instanceof TestActor o) {
                //if(o.isInFrameBounds()) {
                    o.draw(g);
                //}
            }
        }

        for(AActor levelProps : levelModel.getLevelProps()) {
            if(levelProps instanceof ALevelProp p) {
                //if(p.isInFrameBounds()) {
                    p.draw(g);
                //}
            }
        }
    }

}
