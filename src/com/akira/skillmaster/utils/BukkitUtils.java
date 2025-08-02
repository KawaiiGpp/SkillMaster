package com.akira.skillmaster.utils;

import com.akira.skillmaster.SkillMaster;
import com.akira.skillmaster.base.ImprovedExecutor;
import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.Random;
import java.util.UUID;
import java.util.function.Consumer;

@SuppressWarnings("deprecation")
public class BukkitUtils {
    private static final Random random = new Random();

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
        debug(object, true);
    }

    public static void debug(Object object, boolean bc) {
        if (bc) Bukkit.broadcastMessage("§b[DEBUG] §f" + object);
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

    public static void playSound(Player player, Sound sound, float pitch) {
        Location location = player.getLocation();
        player.playSound(location, sound, 1.0F, pitch);
    }

    public static void playSound(Player player, Sound sound) {
        playSound(player, sound, 1.0F);
    }

    public static boolean random(int chance) {
        Validate.isTrue(chance >= 0 && chance <= 100, "Chance must be from 0 to 100.");
        return random.nextInt(100) + 1 <= chance;
    }
}
