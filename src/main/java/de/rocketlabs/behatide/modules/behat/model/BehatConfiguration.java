package de.rocketlabs.behatide.modules.behat.model;

import de.rocketlabs.behatide.domain.model.Configuration;
import de.rocketlabs.behatide.domain.model.Profile;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class BehatConfiguration implements Configuration<BehatProfile> {

    private Map<String, BehatProfile> profiles = new HashMap<>();

    @Override
    public BehatProfile getProfile(String name) {
        return profiles.get(name);
    }

    @NotNull
    @Override
    public Set<String> getProfileNames() {
        return profiles.keySet();
    }

    @Override
    public List<BehatProfile> getProfiles() {
        return Collections.unmodifiableList(new LinkedList<>(profiles.values()));
    }

    @Override
    public void addProfile(@NotNull BehatProfile profile) {
        profiles.put(profile.getName(), profile);
    }
}
