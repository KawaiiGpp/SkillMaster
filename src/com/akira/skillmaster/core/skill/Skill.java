package com.akira.skillmaster.core.skill;

import com.akira.skillmaster.base.AttributeType;
import com.akira.skillmaster.manager.AttributeTypeManager;
import org.bukkit.ChatColor;

public class Skill extends AttributeType {
    private static final AttributeTypeManager<Skill> manager = new AttributeTypeManager<>();

    public static final Skill COMBAT = new Skill("combat", "战斗", ChatColor.LIGHT_PURPLE, '⚔', "击杀生物以提升技能");
    public static final Skill MINING = new Skill("mining", "矿业", ChatColor.AQUA, '⸕', "探索矿洞并采集矿物以提升技能");
    public static final Skill FORAGING = new Skill("foraging", "伐木", ChatColor.GOLD, '☘', "伐木以提升技能");
    public static final Skill FARMING = new Skill("farming", "农业", ChatColor.YELLOW, '☀', "参与农业活动以提升技能");

    private Skill(String regName, String dispName, ChatColor color, char icon, String desc) {
        super(regName, dispName, color, icon, desc);
        manager.register(this);
    }

    public static AttributeTypeManager<Skill> getManager() {
        return manager;
    }
}
