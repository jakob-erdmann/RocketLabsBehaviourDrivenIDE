package de.rocketlabs.behatide.application.fx;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameters;
import javafx.fxml.LoadException;
import javafx.util.Builder;
import javafx.util.BuilderFactory;
import sun.reflect.misc.ReflectUtil;

/**
 * @author Jakob Erdmann
 * @since 03.03.17
 */
public class ApplicationParameterBuilderFactory implements BuilderFactory {

    private static ApplicationParameterBuilderFactory instance;
    private String[] args;

    private ApplicationParameterBuilderFactory() {
    }

    public static ApplicationParameterBuilderFactory getInstance() {
        if (instance == null) {
            instance = new ApplicationParameterBuilderFactory();
        }
        return instance;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    @Override
    public Builder<?> getBuilder(Class<?> type) {
        Parameters annotation = type.getAnnotation(Parameters.class);
        if (annotation == null) {
            return null;
        }

        Object instance;
        try {
            instance = ReflectUtil.newInstance(type);
        } catch (InstantiationException | IllegalAccessException exception) {
            //TODO: Log
            return null;
        }

        JCommander jCommander = new JCommander(instance);
        jCommander.setAcceptUnknownOptions(true);
        jCommander.parse(args);
        return null;
    }
}
