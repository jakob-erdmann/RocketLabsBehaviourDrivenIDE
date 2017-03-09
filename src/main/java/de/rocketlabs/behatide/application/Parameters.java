package de.rocketlabs.behatide.application;

import com.beust.jcommander.Parameter;

/**
 * @author Jakob Erdmann
 * @since 02.03.17
 */
public class Parameters {
    @Parameter(names = {"-d", "--debug"}, description = "Enable debug mode")
    private boolean debugMode = false;
}
