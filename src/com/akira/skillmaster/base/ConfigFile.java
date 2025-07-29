package com.akira.skillmaster.base;

import com.akira.skillmaster.SkillMaster;
import com.akira.skillmaster.func.ThrowingRunnable;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

@SuppressWarnings("ResultOfMethodCallIgnored")
public abstract class ConfigFile {
    protected final String name;
    protected final File file;
    protected YamlConfiguration config;

    protected ConfigFile(String name) {
        this.file = new File(SkillMaster.getInstance().getDataFolder(), name + ".yml");
        this.name = name;
    }

    public final void initialize() {
        File folder = SkillMaster.getInstance().getDataFolder();

        if (!folder.exists()) folder.mkdirs();
        if (!file.exists()) ThrowingRunnable.runSafely(file::createNewFile);

        this.reload();
        this.applyTemplate();
    }

    public final void reload() {
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public final void save() {
        ThrowingRunnable.runSafely(() -> config.save(file));
    }

    public final String getName() {
        return name;
    }

    public final boolean equals(Object o) {
        if (!(o instanceof ConfigFile that)) return false;
        return Objects.equals(name, that.name);
    }

    public final int hashCode() {
        return Objects.hashCode(name);
    }

    private void applyTemplate() {
        String path = "com/akira/skillmaster/config/templates/" + name + ".yml";
        InputStream input = SkillMaster.getInstance().getResource(path);
        if (input == null) return;
        config.addDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(input)));

        int sizeBefore = config.getKeys(true).size();
        this.config.options().copyDefaults(true);
        if (config.getKeys(true).size() > sizeBefore) this.save();
    }
}
