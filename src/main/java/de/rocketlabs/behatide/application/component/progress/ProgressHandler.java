package de.rocketlabs.behatide.application.component.progress;

/**
 * @author Jakob Erdmann
 * @since 27.02.17
 */
public interface ProgressHandler {
    void addTask(String identifier);

    void removeTask(String identifier);

    void doProgress(String taskIdentifier, float percentage, String message);
}
