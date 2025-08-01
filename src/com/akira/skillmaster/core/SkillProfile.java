package com.akira.skillmaster.core;

import com.akira.skillmaster.config.instances.ConfigProfile;
import com.akira.skillmaster.core.perk.Perk;
import com.akira.skillmaster.core.perk.PerkData;
import com.akira.skillmaster.core.skill.Skill;
import com.akira.skillmaster.core.skill.SkillData;
import com.akira.skillmaster.manager.ConfigManager;

import java.util.Set;
import java.util.UUID;

public class SkillProfile {
    protected final UUID uniqueId;
    protected final SkillData skillData;
    protected final PerkData perkData;

    public SkillProfile(UUID uniqueId) {
        this.uniqueId = uniqueId;
        this.skillData = new SkillData(this);
        this.perkData = new PerkData(this);

        load();
    }

    public final void load() {
        ConfigProfile config = this.getConfig();

        Set<Skill> skillSet = Skill.getManager().copySet();
        Set<Perk> perkSet = Perk.getManager().copySet();

        if (config.hasAnyData(uniqueId)) {
            skillSet.forEach(s -> config.loadSkillEntry(uniqueId, s, skillData.getEntry(s)));
            perkSet.forEach(p -> perkData.set(p, config.getPerkValue(uniqueId, p), true));
        } else {
            skillSet.forEach(s -> skillData.setEntry(s, 1, 0));
            perkSet.forEach(p -> perkData.set(p, 0, true));
        }
    }

    public final void save() {
        ConfigProfile config = this.getConfig();

        Skill.getManager().copySet().forEach(s -> config.setSkillEntry(uniqueId, s, skillData.getEntry(s)));
        Perk.getManager().copySet().forEach(p -> config.setPerkValue(uniqueId, p, perkData.get(p)));

        config.save();
    }

    public final UUID getUniqueId() {
        return uniqueId;
    }

    public final SkillData getSkillData() {
        return skillData;
    }

    public final PerkData getPerkData() {
        return perkData;
    }

    private ConfigProfile getConfig() {
        return (ConfigProfile) ConfigManager.getInstance().fromString("profile");
    }
}
