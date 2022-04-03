package prototypes.window.environments;

import models.environments.EnvironmentsHandler;
import prototypes.controls.AKeyController;
import prototypes.controls.AMouseController;
import utils.drawables.IDrawable;
import utils.files.AudioPlayer;
import utils.files.Resources;
import utils.updates.IUpdatable;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

/**
 * The type A environment.
 */
public abstract class AEnvironment implements IUpdatable, IDrawable {

    protected AudioPlayer audioPlayer;

    /**
     * The Parent environments model.
     */
    public EnvironmentsHandler parentEnvironmentsModel;

    /**
     * The Key controller.
     */
    protected AKeyController keyController;
    /**
     * The Mouse controller.
     */
    protected AMouseController mouseController;

    /**
     * Init.
     *
     * @param parentEnvironmentsModel the parent environments model
     * @param keyController           the key controller
     * @param mouseController         the mouse controller
     */
    protected void init(EnvironmentsHandler parentEnvironmentsModel, AKeyController keyController, AMouseController mouseController) {
        this.parentEnvironmentsModel = parentEnvironmentsModel;
        this.keyController = keyController;
        this.mouseController = mouseController;
    }

    /**
     * Gets key controller.
     *
     * @return the key controller
     */
    public AKeyController getKeyController() {
        return keyController;
    }

    /**
     * Gets mouse controller.
     *
     * @return the mouse controller
     */
    public AMouseController getMouseController() {
        return mouseController;
    }

    public abstract void startBackgroundAudio();

    protected void stopBackgroundAudio() {
        if(audioPlayer != null) {
            try {
                audioPlayer.stop();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        }
    }

    public void onResume() {
        startBackgroundAudio();
    }

    public void onExit() {
        stopBackgroundAudio();
    }

    public abstract void reset();
}
