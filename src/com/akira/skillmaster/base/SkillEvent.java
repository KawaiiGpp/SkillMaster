package com.akira.skillmaster.base;

import com.akira.skillmaster.core.SkillPlayer;
import com.akira.skillmaster.core.SkillProfile;
import org.apache.commons.lang3.Validate;
import org.bukkit.event.Event;

public abstract class SkillEvent extends Event {
    protected final SkillProfile profile;

    protected SkillEvent(SkillProfile profile) {
        this.profile = profile;
    }

    public final SkillProfile getProfile() {
        return profile;
    }

    public final boolean canBeCastToPlayer() {
        return profile instanceof SkillPlayer;
    }

    public final SkillPlayer getProfileAsPlayer() {
        Validate.isTrue(canBeCastToPlayer(), "This profile cannot be cast to a skill player.");
        return (SkillPlayer) profile;
    }
}
