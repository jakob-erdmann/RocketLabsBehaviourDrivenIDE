package de.rocketlabs.behatide.application.component.menu;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class FileMenu extends Menu {

    @FXML
    private ObjectProperty<Scene> scene = new SimpleObjectProperty<>();

    public FileMenu() {
        super("File");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/menu/FileMenu.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    private void exitAction() {
        Window window = getScene().getWindow();
        window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    public Scene getScene() {
        return scene.get();
    }

    public void setScene(Scene scene) {
        this.scene.set(scene);
    }

    @FXML
    public ObjectProperty<Scene> sceneProperty() {
        return scene;
    }
}
