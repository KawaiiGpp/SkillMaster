package com.akira.skillmaster.listener;

import com.akira.skillmaster.config.instances.ConfigSettings;
import com.akira.skillmaster.core.SkillPlayer;
import com.akira.skillmaster.manager.ConfigManager;
import com.akira.skillmaster.manager.SkillPlayerManager;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class SkillListener implements Listener {
    private final ConfigSettings settings = (ConfigSettings) ConfigManager.getInstance().fromString("settings");

    @EventHandler
    public void onCombat(EntityDeathEvent e) {
        LivingEntity entity = e.getEntity();
        EntityDamageEvent lastDamage = entity.getLastDamageCause();

        if (!(lastDamage instanceof EntityDamageByEntityEvent event)) return;
        if (!(event.getDamager() instanceof Player damager)) return;

        AttributeInstance attribute = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (attribute == null) return;

        SkillPlayer sp = SkillPlayerManager.getInstance().fromPlayer(damager);
        double baseExp = settings.getCombatExpPerHealth() * attribute.getValue();
    }
}
