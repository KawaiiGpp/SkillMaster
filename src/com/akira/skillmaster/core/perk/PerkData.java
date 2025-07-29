package com.akira.skillmaster.core.perk;

import org.apache.commons.lang3.Validate;

import java.util.HashMap;
import java.util.Map;

public class PerkData {
    private final Map<Perk, Double> map;

    public PerkData() {
        this.map = new HashMap<>();
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

    private void initialize() {
        Perk.getManager().copySet().forEach(p -> map.put(p, 0.0));
    }

    private void checkPerkType(Perk perk) {
        Validate.isTrue(map.containsKey(perk), "Unsupported perk.");
    }
}
