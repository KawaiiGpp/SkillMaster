package com.akira.skillmaster.core.skill;

import com.akira.skillmaster.core.SkillProfile;
import org.apache.commons.lang3.Validate;

import java.util.HashMap;
import java.util.Map;

public class SkillData {
    private final SkillProfile profile;
    private final Map<Skill, SkillEntry> map;

    public SkillData(SkillProfile profile) {
        this.map = new HashMap<>();
        this.profile = profile;

        this.initialize();
    }

    public void setEntry(Skill skill, int level, double exp) {
        this.checkSkillType(skill);

        SkillEntry entry = this.getEntry(skill);
        entry.setLevel(level);
        entry.setExp(exp);
    }

    public SkillEntry getEntry(Skill skill) {
        this.checkSkillType(skill);
        return this.map.get(skill);
    }

    public SkillProfile getProfile() {
        return profile;
    }

    private void initialize() {
        Skill.getManager().copySet().forEach(s -> map.put(s, new SkillEntry(profile, 1, 0)));
    }

    private void checkSkillType(Skill skill) {
        Validate.isTrue(map.containsKey(skill), "Unsupported skill.");
    }
}
