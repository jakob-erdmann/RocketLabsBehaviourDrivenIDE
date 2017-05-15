package de.rocketlabs.behatide.modules.behat.model;

import de.rocketlabs.behatide.domain.model.Profile;
import de.rocketlabs.behatide.modules.behat.filter.GherkinFilter;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class BehatProfile implements Profile<BehatSuite> {

    private String name;
    private String locale;
    private String fallbackLocale;
    private Map<String, String> autoLoadPaths = new HashMap<>();
    private List<GherkinFilter> filters = new LinkedList<>();
    private Map<String, BehatSuite> suites = new HashMap<>();
    private Map<String, String> classPaths;

    public BehatProfile(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public List<String> getPaths() {
        //TODO: Implement
        //should hold paths to feature files
        return null;
    }

    @Override
    public void addSuite(@NotNull BehatSuite suite) {
        suites.put(suite.getName(), suite);
    }

    @Override
    public BehatSuite getSuite(String name) {
        return suites.get(name);
    }

    @NotNull
    @Override
    public Set<String> getSuiteNames() {
        return suites.keySet();
    }

    @Override
    public List<BehatSuite> getSuites() {
        return Collections.unmodifiableList(new LinkedList<>(suites.values()));
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getFallbackLocale() {
        return fallbackLocale;
    }

    public void setFallbackLocale(String fallbackLocale) {
        this.fallbackLocale = fallbackLocale;
    }

    public void addAutoLoadPath(String prefix, String autoLoadPath) {
        autoLoadPaths.put(prefix, autoLoadPath);
    }

    public Map<String, String> getAutoLoadPaths() {
        return autoLoadPaths;
    }

    public void addFilter(GherkinFilter filter) {
        filters.add(filter);
    }

    public List<GherkinFilter> getFilters() {
        return filters;
    }

    public void setClassPaths(Map<String, String> classPaths) {
        this.classPaths = classPaths;
    }

    public Map<String, String> getClassPaths() {
        return classPaths;
    }
}
