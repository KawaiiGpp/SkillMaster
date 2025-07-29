package com.akira.skillmaster.core.skill;

import org.apache.commons.lang3.Validate;

import java.util.HashMap;
import java.util.Map;

public class SkillData {
    private final Map<Skill, SkillEntry> map;

    public SkillData() {
        this.map = new HashMap<>();
        this.initialize();
    }

    public void setEntry(Skill skill, SkillEntry entry) {
        this.checkSkillType(skill);
        this.map.put(skill, entry);
    }

    public SkillEntry getEntry(Skill skill) {
        this.checkSkillType(skill);
        return this.map.get(skill);
    }

    private void initialize() {
        Skill.getManager().copySet().forEach(s -> map.put(s, new SkillEntry(1, 0)));
    }

    private void checkSkillType(Skill skill) {
        Validate.isTrue(map.containsKey(skill), "Unsupported skill.");
    }
}
