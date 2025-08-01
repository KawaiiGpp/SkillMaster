package com.akira.skillmaster.core;

import org.bukkit.entity.Player;

public class SkillPlayer extends SkillProfile {
    private final Player player;

    public SkillPlayer(Player player) {
        super(player.getUniqueId());
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
