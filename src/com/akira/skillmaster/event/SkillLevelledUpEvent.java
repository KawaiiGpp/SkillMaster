package com.akira.skillmaster.event;

import com.akira.skillmaster.base.SkillEvent;
import com.akira.skillmaster.core.SkillProfile;
import com.akira.skillmaster.core.skill.Skill;
import org.bukkit.event.HandlerList;

public class SkillLevelledUpEvent extends SkillEvent {
    private static final HandlerList handlerList = new HandlerList();

    private final Skill skill;
    private final int levelFrom;
    private final int levelTo;

    public SkillLevelledUpEvent(SkillProfile profile, Skill skillType, int levelFrom, int levelTo) {
        super(profile);

        this.skill = skillType;
        this.levelFrom = levelFrom;
        this.levelTo = levelTo;
    }

    public int getLevelFrom() {
        return levelFrom;
    }

    public int getLevelTo() {
        return levelTo;
    }

    public Skill getSkill() {
        return skill;
    }

    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
