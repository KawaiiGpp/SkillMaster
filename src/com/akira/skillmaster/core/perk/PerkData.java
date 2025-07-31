package com.akira.skillmaster.core.perk;

import com.akira.skillmaster.core.SkillProfile;
import org.apache.commons.lang3.Validate;

import java.util.HashMap;
import java.util.Map;

public class PerkData {
    private final SkillProfile profile;
    private final Map<Perk, Double> map;

    public PerkData(SkillProfile profile) {
        this.map = new HashMap<>();
        this.profile = profile;

        this.initialize();
    }

    public void set(Perk perk, double value) {
        this.checkPerkType(perk);
        Validate.isTrue(value >= 0, "Perk value cannot be lower than 0.");
        this.map.put(perk, value);
    }

    public double get(Perk perk) {
        this.checkPerkType(perk);
        return this.map.get(perk);
    }

    public void gain(Perk perk, double value) {
        this.checkPerkType(perk);
        Validate.isTrue(value > 0, "Perk value gained must be greater than 0.");

        this.set(perk, this.get(perk) + value);
    }

    public SkillProfile getProfile() {
        return profile;
    }

    private void initialize() {
        Perk.getManager().copySet().forEach(p -> map.put(p, 0.0));
    }

    private void checkPerkType(Perk perk) {
        Validate.isTrue(map.containsKey(perk), "Unsupported perk.");
    }
}
