package de.rocketlabs.behatide.application.component.progress;

import de.rocketlabs.behatide.application.fx.FxmlLoading;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jakob Erdmann
 * @since 27.02.17
 */
public class StandaloneProgressWindow extends Stage implements ProgressHandler, FxmlLoading<Stage> {
    private static StandaloneProgressWindow instance;
    private final Map<String, ProgressBar> openTasks = new HashMap<>();
    @FXML
    private ListView<ProgressBar> view;

    private StandaloneProgressWindow() {
        loadFxml();
    }

    public static StandaloneProgressWindow getInstance() {
        if (instance == null) {
            instance = new StandaloneProgressWindow();
        }
        return instance;
    }

    @Override
    public void addTask(String identifier) {
        synchronized (openTasks) {
            ProgressBar progressBar = new ProgressBar();
            openTasks.put(identifier, progressBar);
            view.getItems().add(progressBar);
            show();
        }
    }

    @Override
    public void removeTask(String identifier) {
        synchronized (openTasks) {
            ProgressBar progressBar = openTasks.remove(identifier);
            view.getItems().remove(progressBar);
            if (view.getItems().size() == 0) {
                hide();
            }
        }
    }

    @Override
    public void doProgress(String taskIdentifier, float percentage, String message) {
        ProgressBar progressBar = openTasks.get(taskIdentifier);
        if (progressBar != null) {
            progressBar.doProgress(percentage, message);
        }
    }

    @Override
    public String getFxmlPath() {
        return "/view/progress/StandaloneProgressWindow.fxml";
    }
}
