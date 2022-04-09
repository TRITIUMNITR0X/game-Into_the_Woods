package models.environments.game;

import models.camera.Camera;
import controls.GameControls;
import controls.game.GameKeyControls;
import controls.game.GameMouseControls;
import controls.menu.MenuKeyControls;
import models.environments.EnvironmentsHandler;
import models.environments.game.hud.HUDModel;
import models.environments.game.playerinventory.PlayerInventory;
import models.environments.menus.pausemenumodel.PauseMenuEnvironment;
import models.actors.gameactors.PlayerAvatar;
import models.actors.gameactors.TestActor;
import models.levels.LevelsList;
import models.prototypes.actor.AActor;
import models.prototypes.actor.pawn.character.ACharacter;
import models.prototypes.level.ALevel;
import models.prototypes.level.prop.ALevelProp;
import models.prototypes.window.environments.AEnvironment;
import models.utils.config.ConfigData;
import models.utils.drawables.IDrawable;
import models.utils.resources.Resources;
import models.utils.updates.IUpdatable;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class GameEnvironment extends AEnvironment implements IDrawable, IUpdatable {

    private PauseMenuEnvironment pauseMenuModel;

    private LevelsList levelModel;

    private HUDModel hudModel;
    private PlayerInventory inventory;

    private final ArrayList<AActor> actors = new ArrayList<>();
    private final Queue<AActor> actorsQueue = new LinkedList<>();

    private PlayerAvatar character;

    private boolean isPaused = false;

    public void init(EnvironmentsHandler parentEnvironmentsHandler,
                     PauseMenuEnvironment pauseMenuModel, GameControls controlsModel,
                     LevelsList levelModel, HUDModel hudModel, PlayerInventory inventory) {

        super.init(parentEnvironmentsHandler, controlsModel.getKeyController(),
                controlsModel.getMouseController());

        setPauseMenuModel(pauseMenuModel);
        setLevelModel(levelModel);
        setHUDModel(hudModel);
        setPlayerInventory(inventory);

        build(controlsModel);
    }

    private void setPlayerInventory(PlayerInventory inventory) {
        this.inventory = inventory;
    }

    private void setHUDModel(HUDModel hudModel) {
        this.hudModel = hudModel;
    }

    private void setPauseMenuModel(PauseMenuEnvironment pauseMenuModel) {
        this.pauseMenuModel = pauseMenuModel;
    }

    public void build(GameControls controlsModel) {
        setPlayerAvatar(controlsModel, levelModel);
    }

    @Override
    public void update(float delta) {

        if(!isPaused) {
            doGameUpdates(delta);
        } else {
            doPauseMenuUpdates(delta);
        }

    }

    @Override
    public void draw(Graphics g) {

        // Render Level Props
        levelModel.draw(g);

        // Render Game Actors
        for (AActor gameObject : actors) {
            gameObject.draw(g);
        }

        if(!isPaused) {
            hudModel.draw(g);
        }

        if(isPaused) {
            //Draw Pause Menu
            pauseMenuModel.draw(g);
        }
    }

    public void doGameUpdates(float delta) {

        doGameControls();

        //testAddingActors(delta); // TODO: Delete this (testing purposes only)

        detectCollisions(delta); // Check Game Object Collisions with Level Props

        insertQueuedActors(); // Dequeue queued actors and add them to list of actors

        updateActors(delta); // Update the Game Objects

        updateLevel(delta); // Update level models.props

        updateHUD(delta); // Update HUD overlay
    }

    public void doPauseMenuUpdates(float delta) {
        doPauseMenuControls();

        pauseMenuModel.update(delta);
    }

    private void doGameControls() {
        if(keyController instanceof GameKeyControls kc) {
            if(kc.getControlsModel().getAction(GameControls.Actions.ESCAPE)) {
                kc.getControlsModel().resetAction(GameControls.Actions.ESCAPE);
                isPaused = true;
                parentEnvironmentsModel.swapToEnvironmentType(
                        EnvironmentsHandler.EnvironmentType.GAME_PAUSE_MENU, false).applyEnvironment();
            }
        }
    }

    public void doPauseMenuControls() {
        if(pauseMenuModel.getKeyController() instanceof MenuKeyControls kc) {
            if(kc.getControlsModel().getAction(GameControls.Actions.ESCAPE)) {
                kc.getControlsModel().resetAction(GameControls.Actions.ESCAPE);
                isPaused = false;
                parentEnvironmentsModel.swapToEnvironmentType(
                        EnvironmentsHandler.EnvironmentType.GAME, false).applyEnvironment();
            }
        }
    }

    public void setLevelModel(LevelsList levelModel) {
        this.levelModel = levelModel;
    }

    private void setPlayerAvatar(GameControls controlsViewModel, LevelsList levelModel) {
        int[] startPos = levelModel.getCurrentLevel().getCharacterOrigin();
        // Add in the Main Test Character

        character = new PlayerAvatar (
                controlsViewModel,
                startPos[0],
                startPos[1],
                55, 70,
                0, 0,
                true
        );

    }


    private void updateHUD(float delta) {
        hudModel.update(delta);
    }

    private void updateLevel(float delta) {
        levelModel.getCurrentLevel().update(delta);
    }

    private synchronized void testAddingActors(float delta) {
        if (mouseController instanceof GameMouseControls) {

            GameMouseControls gmc = (GameMouseControls) mouseController;

            if (gmc.isLeftPressed()) {
                int count = (int) (10 / delta);
                if (count < 1) {
                    count = 1;
                }
                for (int i = 0; i < count; i++) {
                    queueActor(
                            new TestActor(
                                    (-Camera.camX / ConfigData.scaledW) + (gmc.getPos()[0]/ ConfigData.scaledW),
                                    (-Camera.camY / ConfigData.scaledW) + (gmc.getPos()[1]/ ConfigData.scaledH),
                                    10f,
                                    10f,
                                    new Random().nextFloat(-5, 5),
                                    new Random().nextFloat(-5, 5),
                                    true
                            )
                    );
                }
            }
        }
    }

    public void updateActors(float delta) {

        // Update all Actors
        for (AActor gameObject : actors) {

            // Update TestActors
            if (gameObject instanceof TestActor a) {
                a.update(delta);
            }

            // Update Characters
            if (gameObject instanceof PlayerAvatar tc) {
                tc.control(delta);
                tc.update(delta);
                System.out.println(tc.actionState);
            }

        }

    }

    private void detectCollisions(float delta) {
        for (ALevelProp p : levelModel.getCurrentLevel().getLevelProps()) {
            for (AActor a : actors) {
                p.hasCollision(a, delta);
            }
        }
    }

    private void insertQueuedActors() {
        for(int i = 0; i < 10 && actorsQueue.size() >= 1; i++) {
            addActor(actorsQueue.remove());
        }
    }

    public void queueActor(AActor a) {
        actorsQueue.add(a);
    }

    public void addActor(AActor actor) {
        actors.add(actor);
    }

    public void setCurrentLevel(int levelIndex) {
        levelModel.setCurrentLevel(levelIndex);
    }

    public ALevel getCurrentLevel() {
        return levelModel.getCurrentLevel();
    }

    public ACharacter getPlayerAvatar() {
        return character;
    }

    @Override
    public void startBackgroundAudio() {
        audioPlayer = Resources.playAudio("game");
    }

    @Override
    public void reset() {
        hudModel.reset();
        inventory.reset();
        actors.clear();
        actors.add(character);
        character.reset(levelModel.getCurrentLevel().getCharacterOrigin());
        levelModel.reset();
    }

    public PlayerInventory getPlayerInventory() {
        return inventory;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }
}
