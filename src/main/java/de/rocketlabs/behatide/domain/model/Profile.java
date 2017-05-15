package de.rocketlabs.behatide.domain.model;

import java.util.List;
import java.util.Set;

public interface Profile<T extends Suite> {

    String getName();

    List<String> getPaths();

    Suite getSuite(String name);

    Set<String> getSuiteNames();

    List<T> getSuites();

    void addSuite(T suite);
}
