package com.akira.skillmaster.func;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface PerkUpdater {
    void onUpdate(Player player, double value);
}
