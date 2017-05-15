package de.rocketlabs.behatide.domain.model;

import java.util.List;
import java.util.Set;

public interface Configuration<T extends Profile> {

    T getProfile(String name);

    Set<String> getProfileNames();

    List<T> getProfiles();

    void addProfile(T profile);
}
