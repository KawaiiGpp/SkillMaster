package com.akira.skillmaster.utils;

import com.akira.skillmaster.SkillMaster;
import com.akira.skillmaster.base.ImprovedExecutor;
import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.UUID;
import java.util.function.Consumer;

@SuppressWarnings("deprecation")
public class BukkitUtils {
    public static void info(String message) {
        msg(ChatColor.GREEN, "[INFO] " + message);
    }

    public static void warn(String message) {
        msg(ChatColor.YELLOW, "[WARN] " + message);
    }

    public static void err(String message) {
        msg(ChatColor.RED, "[ERR] " + message);
    }

    public static void debug(Object object) {
        msg(ChatColor.AQUA, "[DEBUG] " + object);
    }

    public static void msg(ChatColor color, String message) {
        String name = SkillMaster.getInstance().getPluginName();
        Bukkit.getConsoleSender().sendMessage(color + "[" + name + "] " + message);
    }

    public static void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, SkillMaster.getInstance());
    }

    public static void registerCommand(ImprovedExecutor executor) {
        String name = executor.getName();
        PluginCommand command = Bukkit.getPluginCommand(name);

        Validate.notNull(command, "Couldn't find the command: " + name);
        command.setExecutor(executor);
    }

    public static void getUniqueId(String name, Consumer<UUID> callback) {
        SkillMaster plugin = SkillMaster.getInstance();
        BukkitScheduler scheduler = Bukkit.getScheduler();

        scheduler.runTaskAsynchronously(plugin, () -> {
            UUID uuid = Bukkit.getOfflinePlayer(name).getUniqueId();
            scheduler.runTask(plugin, () -> callback.accept(uuid));
        });
    }
}
