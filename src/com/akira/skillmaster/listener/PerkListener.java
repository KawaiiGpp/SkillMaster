package com.akira.skillmaster.listener;

import com.akira.skillmaster.config.instances.ConfigSettings;
import com.akira.skillmaster.core.SkillPlayer;
import com.akira.skillmaster.core.perk.Perk;
import com.akira.skillmaster.manager.ConfigManager;
import com.akira.skillmaster.manager.SkillPlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class PerkListener implements Listener {
    private final ConfigSettings config = (ConfigSettings) ConfigManager.getInstance().fromString("settings");

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAttack(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player attacker)) return;
        SkillPlayer sp = this.getManager().fromPlayer(attacker);

        double multiplier = config.getPerkMultiplierPerStrength();
        double strength = sp.getPerkData().get(Perk.STRENGTH);
        double damageBonus = strength * multiplier * 1.5;
        double damage = (e.getDamage() + damageBonus) * (1 + multiplier * strength);

        e.setDamage(damage);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDefense(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player victim)) return;
        SkillPlayer sp = this.getManager().fromPlayer(victim);

        double defense = sp.getPerkData().get(Perk.DEFENSE);
        int factor = config.getPerkDefenseScalingFactor();
        double reduction = defense / (factor + defense);
        double damage = e.getDamage() * (1 - reduction);

        e.setDamage(damage);
    }

    private SkillPlayerManager getManager() {
        return SkillPlayerManager.getInstance();
    }
}
