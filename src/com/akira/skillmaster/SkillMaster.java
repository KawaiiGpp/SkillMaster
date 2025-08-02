package com.akira.skillmaster;

import com.akira.skillmaster.command.perk.PerkAdminExecutor;
import com.akira.skillmaster.command.perk.PerkExecutor;
import com.akira.skillmaster.command.skill.SkillAdminExecutor;
import com.akira.skillmaster.command.skill.SkillExecutor;
import com.akira.skillmaster.config.instances.ConfigProfile;
import com.akira.skillmaster.config.instances.ConfigSettings;
import com.akira.skillmaster.listener.NotificationListener;
import com.akira.skillmaster.listener.PerkListener;
import com.akira.skillmaster.listener.PlayerRegistrationListener;
import com.akira.skillmaster.listener.SkillListener;
import com.akira.skillmaster.manager.ConfigManager;
import com.akira.skillmaster.utils.BukkitUtils;
import org.apache.commons.lang3.Validate;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class SkillMaster extends JavaPlugin {
    private static SkillMaster instance;

    private final String name;
    private final String version;
    private final String[] authors;

    public SkillMaster() {
        instance = this;

        PluginDescriptionFile desc = this.getDescription();
        this.name = desc.getName();
        this.version = desc.getVersion();
        this.authors = desc.getAuthors().toArray(new String[0]);
    }

    public void onEnable() {
        BukkitUtils.info("Plugin has been enabled.");
        BukkitUtils.info("Version: " + version + ", Authors: " + String.join(", ", authors));

        ConfigManager manager = ConfigManager.getInstance();
        manager.register(new ConfigProfile());
        manager.register(new ConfigSettings());
        manager.initializeAll();

        BukkitUtils.registerListener(new PlayerRegistrationListener());
        BukkitUtils.registerListener(new PerkListener());
        BukkitUtils.registerListener(new SkillListener());
        BukkitUtils.registerListener(new NotificationListener());

        BukkitUtils.registerCommand(new PerkExecutor());
        BukkitUtils.registerCommand(new PerkAdminExecutor());
        BukkitUtils.registerCommand(new SkillExecutor());
        BukkitUtils.registerCommand(new SkillAdminExecutor());
    }

    public void onDisable() {
        BukkitUtils.info("Thank you for using. See you next time :)");
    }

    public String getPluginName() {
        return name;
    }

    public String getPluginVersion() {
        return version;
    }

    public String[] getPluginAuthors() {
        return authors;
    }

    public static SkillMaster getInstance() {
        Validate.notNull(instance, "Plugin instance is null.");
        return instance;
    }
}
