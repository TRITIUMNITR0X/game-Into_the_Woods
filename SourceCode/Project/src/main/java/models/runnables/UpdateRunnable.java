package models.runnables;

import models.prototypes.environments.AEnvironment;
import models.prototypes.threading.ARunnable;
import models.utils.config.Config;

/**
 * <p></p>
 */
public class UpdateRunnable extends ARunnable {

    private AEnvironment environment;

    /**
     * <p></p>
     * @param environment -
     */
    public void init(AEnvironment environment) {
        this.environment = environment;
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime(), timer = System.currentTimeMillis();
        final short targetTicks = Config.GAME_UPDATE_RATE;
        float ns = 1000000000 / (float) targetTicks, delta = 0;
        float updateRatio = 1;

        isRunning = true;
        while(isRunning) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            if(delta >= 1) {

                updateRatio = lastUpdates / (float) Config.GAME_UPDATE_RATE;
                environment.update(updateRatio);
                updates++;

                delta--;
            }

            if(System.currentTimeMillis() - timer > 1000) {
                timer += 1000;

                lastUpdates = updates;
                updates = 0;


            }

        }
    }
}
