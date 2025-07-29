package com.akira.skillmaster.core.perk;

import com.akira.skillmaster.base.AttributeType;
import com.akira.skillmaster.manager.AttributeTypeManager;
import org.bukkit.ChatColor;

public class Perk extends AttributeType {
    private static final AttributeTypeManager<Perk> manager = new AttributeTypeManager<>();

    public static final Perk DEFENSE = new Perk("defense", "防御", ChatColor.DARK_GREEN, '❈', "减少你受到的伤害");
    public static final Perk STRENGTH = new Perk("strength", "力量", ChatColor.DARK_PURPLE, '❁', "增加你的攻击伤害");
    public static final Perk SPEED = new Perk("speed", "移速", ChatColor.DARK_AQUA, '✦', "提升你的移动速度");

    private Perk(String regName, String dispName, ChatColor color, char icon, String desc) {
        super(regName, dispName, color, icon, desc);
        manager.register(this);
    }

    public static AttributeTypeManager<Perk> getManager() {
        return manager;
    }
}
