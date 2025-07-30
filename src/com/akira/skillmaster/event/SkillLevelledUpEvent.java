package com.akira.skillmaster.event;

import com.akira.skillmaster.base.SkillEvent;
import com.akira.skillmaster.core.SkillProfile;
import org.bukkit.event.HandlerList;

public class SkillLevelledUpEvent extends SkillEvent {
    private static final HandlerList handlerList = new HandlerList();

    private final int levelFrom;
    private final int levelTo;

    public SkillLevelledUpEvent(SkillProfile profile, int levelFrom, int levelTo) {
        super(profile);

        this.levelFrom = levelFrom;
        this.levelTo = levelTo;
    }

    public int getLevelFrom() {
        return levelFrom;
    }

    public int getLevelTo() {
        return levelTo;
    }

    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
