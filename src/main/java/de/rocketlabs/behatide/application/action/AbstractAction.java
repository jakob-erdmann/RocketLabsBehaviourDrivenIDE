package de.rocketlabs.behatide.application.action;

import com.sun.javafx.application.PlatformImpl;
import javafx.application.Platform;

/**
 * @author Jakob Erdmann
 * @since 10.02.17
 */
public abstract class AbstractAction implements Action {
    private static final ProgressManager PROGRESS_MANAGER = ProgressManager.getInstance();

    protected void executeInFxThread(Runnable action) {
        Platform.runLater(action);
    }

    protected void executeInFxThreadAndWait(Runnable action) {
        PlatformImpl.runAndWait(action);
    }

    /**
     * Sets current progress to percentage and updates progress message
     */
    protected void doProgress(float percentage, String message) {
        PROGRESS_MANAGER.doProgress(this, percentage, message);
    }

    /**
     * Calculates the current percentage from the total itemCount and the currently handled item number.
     * Updates the current progress to that percentage and updates progress message
     */
    protected void doProgress(int itemCount, int current, String message) {
        float percentage = 100f * current / itemCount;
        doProgress(percentage, message);
    }

    /**
     * Calculates the total progress percentage based on the absolute start & stop percentage of the current sub task
     * and the given progress percentage of the sub task.
     * This means if a sub task is meant to take up progress from 20% to 40% and the progress percentage of the sub task
     * is 50%, overall progress will be set to 30%.
     * Also updates the progress message
     */
    protected void doProgress(float start, float stop, float percentage, String message) {
    }

    /**
     * Calculates the total progress percentage based on the absolute start & stop percentage of the current sub task
     * and the given item count and currently handled item number.
     * This means if a sub task is meant to take up progress from 20% to 40% and will handle a total of 50 items where
     * the currently handled item is number 25, overall progress will be set to 30%.
     * Also updates the progress message.
     */
    protected void doProgress(float start, float stop, int itemCount, int current, String message) {
        float percentage = 100f * current / itemCount;
        doProgress(start, stop, percentage, message);
    }

    public enum ProgressBar {
        NONE,
        SEPARATE,
        IN_CONTEXT_WINDOW
    }
}
