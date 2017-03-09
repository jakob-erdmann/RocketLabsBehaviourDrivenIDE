package de.rocketlabs.behatide.application.component.menu;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import de.rocketlabs.behatide.application.action.AbstractAction;
import de.rocketlabs.behatide.application.action.ActionRunner;
import de.rocketlabs.behatide.application.action.EndlessAction;
import de.rocketlabs.behatide.application.fx.FxmlLoading;
import javafx.fxml.FXML;

/**
 * @author Jakob Erdmann
 * @since 28.02.17
 */
@Parameters
public class DebugMenu extends AbstractMenu implements FxmlLoading {

    public DebugMenu() {
        super("_Debug");
        loadFxml();
    }

    @Override
    public String getFxmlPath() {
        return "/view/menu/DebugMenu.fxml";
    }

    @FXML
    private void runEndlessAction() {
        ActionRunner.run(new EndlessAction());
    }

    @Parameter(names = {"--debug", "-d"})
    public void setDebug(boolean debug) {
        setVisible(debug);
    }

}
