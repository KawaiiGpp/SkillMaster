package com.akira.skillmaster.config.instances;

import com.akira.skillmaster.base.ConfigFile;
import com.akira.skillmaster.core.perk.Perk;
import com.akira.skillmaster.core.skill.Skill;
import com.akira.skillmaster.core.skill.SkillEntry;

import java.util.UUID;

public class ConfigProfile extends ConfigFile {
    public ConfigProfile() {
        super("profile");
    }

    public double getPerkValue(UUID uniqueId, Perk perk) {
        return config.getDouble(this.toPerkPath(uniqueId, perk));
    }

    public void setPerkValue(UUID uniqueId, Perk perk, double value) {
        config.set(this.toPerkPath(uniqueId, perk), value);
    }

    public void loadSkillEntry(UUID uniqueId, Skill skill, SkillEntry entry) {
        entry.setLevel(this.config.getInt(this.toSkillPath(uniqueId, skill, "level")));
        entry.setExp(this.config.getDouble(this.toSkillPath(uniqueId, skill, "exp")));
    }

    public void setSkillEntry(UUID uniqueId, Skill skill, SkillEntry entry) {
        config.set(this.toSkillPath(uniqueId, skill, "level"), entry.getLevel());
        config.set(this.toSkillPath(uniqueId, skill, "exp"), entry.getExp());
    }

    public boolean hasAnyData(UUID uniqueId) {
        return this.config.get(uniqueId.toString()) != null;
    }

    private String toPerkPath(UUID uniqueId, Perk perk) {
        return uniqueId + ".perk." + perk.getRegisterName();
    }

    private String toSkillPath(UUID uniqueId, Skill skill, String section) {
        return uniqueId + ".skill." + skill.getRegisterName() + "." + section;
    }
}
