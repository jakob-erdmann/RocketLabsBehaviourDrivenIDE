package de.rocketlabs.behatide.application.component.progress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jakob Erdmann
 * @since 08.03.17
 */
public class HiddenProgressHandler implements ProgressHandler {
    private static final Logger LOG = LoggerFactory.getLogger(HiddenProgressHandler.class);
    private static HiddenProgressHandler instance;

    private HiddenProgressHandler() {
        //no instance
    }

    public static HiddenProgressHandler getInstance() {
        if (instance == null) {
            instance = new HiddenProgressHandler();
        }
        return instance;
    }
    @Override
    public void addTask(String identifier) {
        LOG.debug("New Task: " + identifier);
    }

    @Override
    public void removeTask(String identifier) {
        LOG.debug("Task done: " + identifier);
    }

    @Override
    public void doProgress(String taskIdentifier, float percentage, String message) {
        LOG.debug(String.format("Progress: %s\t%s%%\t%s", taskIdentifier, percentage, message));
    }
}
