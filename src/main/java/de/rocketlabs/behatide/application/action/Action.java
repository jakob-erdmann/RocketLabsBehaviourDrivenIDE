package de.rocketlabs.behatide.application.action;

/**
 * @author Jakob Erdmann
 * @since 08.02.17
 */
public interface Action {
    void doAction();

    default AbstractAction.ProgressBar progressBar() {
        return AbstractAction.ProgressBar.NONE;
    }
}
