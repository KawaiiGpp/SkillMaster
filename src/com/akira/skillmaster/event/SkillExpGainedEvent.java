package com.akira.skillmaster.event;

import com.akira.skillmaster.base.SkillEvent;
import com.akira.skillmaster.core.SkillProfile;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class SkillExpGainedEvent extends SkillEvent implements Cancellable {
    private static final HandlerList handlerList = new HandlerList();

    private double expAmount;
    private boolean cancelled;

    public SkillExpGainedEvent(SkillProfile profile, double expAmount) {
        super(profile);

        this.expAmount = expAmount;
        this.cancelled = false;
    }

    public double getExpAmount() {
        return expAmount;
    }

    public void setExpAmount(double expAmount) {
        this.expAmount = expAmount;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
