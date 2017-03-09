package de.rocketlabs.behatide.application.action;

import de.rocketlabs.behatide.application.component.progress.HiddenProgressHandler;
import de.rocketlabs.behatide.application.component.progress.ProgressHandler;
import de.rocketlabs.behatide.application.component.progress.StandaloneProgressWindow;

/**
 * @author Jakob Erdmann
 * @since 07.03.17
 */
class ProgressManager {

    private static ProgressManager instance;

    static ProgressManager getInstance() {
        if (instance == null) {
            instance = new ProgressManager();
        }
        return instance;
    }

    void doProgress(Action action, float percentage, String message) {
        ProgressHandler progressHandler = getProgressHandler(action);
        progressHandler.doProgress(getTaskIdentifier(action), percentage, message);
    }

    void addTask(Action action) {
        ProgressHandler progressHandler = getProgressHandler(action);
        progressHandler.addTask(getTaskIdentifier(action));
    }

    void removeTask(Action action) {
        ProgressHandler progressHandler = getProgressHandler(action);
        progressHandler.removeTask(getTaskIdentifier(action));
    }
    
    private ProgressHandler getProgressHandler(Action action) {
        switch (action.progressBar()) {
            case NONE:
                return HiddenProgressHandler.getInstance();
            case SEPARATE:
                break;
            case IN_CONTEXT_WINDOW:
                break;
        }
        return null;
    }
    
    private String getTaskIdentifier(Action action) {
        return action.getClass().getName() + action.hashCode();
    }
}
