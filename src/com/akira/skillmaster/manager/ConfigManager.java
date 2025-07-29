package com.akira.skillmaster.manager;

import com.akira.skillmaster.base.ConfigFile;
import com.akira.skillmaster.base.Manager;

import java.util.stream.Stream;

public class ConfigManager extends Manager<ConfigFile> {
    private static final ConfigManager instance = new ConfigManager();

    private ConfigManager() {}

    public ConfigFile fromString(String name) {
        Stream<ConfigFile> stream = set.stream().filter(f -> name.equals(f.getName()));
        return stream.findFirst().orElse(null);
    }

    public void initializeAll() {
        set.forEach(ConfigFile::initialize);
    }

    public static ConfigManager getInstance() {
        return instance;
    }
}
