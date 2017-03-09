package de.rocketlabs.behatide.application.action;

import static java.lang.Thread.sleep;

/**
 * @author Jakob Erdmann
 * @since 28.02.17
 */
public class EndlessAction extends AbstractAction {
    private float progress = 0;

    @Override
    public ProgressBar progressBar() {
        return ProgressBar.SEPARATE;
    }

    @Override
    public void doAction() {
        //noinspection InfiniteLoopStatement
        while (true) {
            try {
                progress = (progress + 10) % 100;
                doProgress(progress, "Endless action");
                sleep(1000);
            } catch (InterruptedException e) {
                //TODO: Handle Exception
            }
        }
    }
}
