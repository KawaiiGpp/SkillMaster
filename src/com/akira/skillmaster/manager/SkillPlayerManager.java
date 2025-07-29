package com.akira.skillmaster.manager;

import com.akira.skillmaster.base.Manager;
import com.akira.skillmaster.core.SkillPlayer;
import org.bukkit.entity.Player;

import java.util.function.Predicate;

public class SkillPlayerManager extends Manager<SkillPlayer> {
    private static final SkillPlayerManager instance = new SkillPlayerManager();

    public SkillPlayer fromString(String string) {
        return filter(p -> string.equals(p.getPlayer().getName()));
    }

    public SkillPlayer fromPlayer(Player player) {
        return filter(p -> player.equals(p.getPlayer()));
    }

    public void unregister(SkillPlayer skillPlayer) {
        super.unregister(skillPlayer);
    }

    private SkillPlayer filter(Predicate<SkillPlayer> predicate) {
        return set.stream().filter(predicate).findFirst().orElse(null);
    }

    public static SkillPlayerManager getInstance() {
        return instance;
    }
}
