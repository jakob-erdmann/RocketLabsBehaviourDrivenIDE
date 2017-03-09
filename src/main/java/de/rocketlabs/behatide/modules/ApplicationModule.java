package de.rocketlabs.behatide.modules;

import de.rocketlabs.behatide.application.component.menu.DebugMenu;
import de.rocketlabs.behatide.domain.model.ProjectType;

import java.util.List;

/**
 * @author Jakob Erdmann
 * @since 02.03.17
 */
public class ApplicationModule extends AbstractModule {
    @Override
    public List<ProjectType> getProjectTypes() {
        return null;
    }

    @Override
    public void configureGson() {

    }

    @Override
    protected void configure() {
        bind(DebugMenu.class);
    }
}
