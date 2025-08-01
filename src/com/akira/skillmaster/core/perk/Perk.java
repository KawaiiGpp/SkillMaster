package com.akira.skillmaster.core.perk;

import com.akira.skillmaster.base.AttributeType;
import com.akira.skillmaster.base.SimplePerkUpdater;
import com.akira.skillmaster.core.SkillPlayer;
import com.akira.skillmaster.core.SkillProfile;
import com.akira.skillmaster.func.PerkUpdater;
import com.akira.skillmaster.manager.AttributeTypeManager;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;

public class Perk extends AttributeType {
    private static final AttributeTypeManager<Perk> manager = new AttributeTypeManager<>();

    public static final Perk DEFENSE = new Perk("defense", "防御", ChatColor.DARK_GREEN, '❈', "减少你受到的伤害");
    public static final Perk STRENGTH = new Perk("strength", "力量", ChatColor.DARK_PURPLE, '❁', "增加你的攻击伤害");
    public static final Perk SPEED = new Perk("speed", "移速", ChatColor.DARK_AQUA, '✦', "提升你的移动速度", new SpeedPerkUpdater());

    private final PerkUpdater updater;

    private Perk(String regName, String dispName, ChatColor color, char icon, String desc, PerkUpdater updater) {
        super(regName, dispName, color, icon, desc);
        this.updater = updater;
        manager.register(this);
    }

    private Perk(String regName, String dispName, ChatColor color, char icon, String desc) {
        this(regName, dispName, color, icon, desc, null);
    }

    public PerkUpdater getUpdater() {
        return updater;
    }

    public void attemptHandleUpdate(SkillProfile profile, double value) {
        if (updater == null) return;
        if (!(profile instanceof SkillPlayer sp)) return;

        updater.onUpdate(sp.getPlayer(), value);
    }

    public static AttributeTypeManager<Perk> getManager() {
        return manager;
    }

    private static class SpeedPerkUpdater extends SimplePerkUpdater {
        public SpeedPerkUpdater() {
            super(Attribute.GENERIC_MOVEMENT_SPEED, "skillmaster.perk.speed", v -> v * settings.getVanillaValuePerSpeed());
        }
    }
}
