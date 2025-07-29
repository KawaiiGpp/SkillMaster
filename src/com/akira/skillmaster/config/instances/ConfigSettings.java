package com.akira.skillmaster.config.instances;

import com.akira.skillmaster.base.ConfigFile;

public class ConfigSettings extends ConfigFile {
    public ConfigSettings() {
        super("settings");
    }

    // === exp.amount ===
    public double getCombatExpPerHealth() {
        return config.getDouble("exp.amount.combat_per_health");
    }

    public void setCombatExpPerHealth(double value) {
        config.set("exp.amount.combat_per_health", value);
    }

    public double getMiningExpAmount() {
        return config.getDouble("exp.amount.mining");
    }

    public void setMiningExpAmount(double value) {
        config.set("exp.amount.mining", value);
    }

    public double getFarmingExpAmount() {
        return config.getDouble("exp.amount.farming");
    }

    public void setFarmingExpAmount(double value) {
        config.set("exp.amount.farming", value);
    }

    public double getForagingExpAmount() {
        return config.getDouble("exp.amount.foraging");
    }

    public void setForagingExpAmount(double value) {
        config.set("exp.amount.foraging", value);
    }

    // === exp.multiplier.combat ===
    public double getCombatBaseMultiplier() {
        return config.getDouble("exp.multiplier.combat.base");
    }

    public void setCombatBaseMultiplier(double value) {
        config.set("exp.multiplier.combat.base", value);
    }

    public double getCombatMonsterMultiplier() {
        return config.getDouble("exp.multiplier.combat.monster");
    }

    public void setCombatMonsterMultiplier(double value) {
        config.set("exp.multiplier.combat.monster", value);
    }

    // === exp.multiplier.mining ===
    public double getMiningBaseMultiplier() {
        return config.getDouble("exp.multiplier.mining.base");
    }

    public void setMiningBaseMultiplier(double value) {
        config.set("exp.multiplier.mining.base", value);
    }

    public double getMiningCommonOreMultiplier() {
        return config.getDouble("exp.multiplier.mining.common_ore");
    }

    public void setMiningCommonOreMultiplier(double value) {
        config.set("exp.multiplier.mining.common_ore", value);
    }

    public double getMiningRareOreMultiplier() {
        return config.getDouble("exp.multiplier.mining.rare_ore");
    }

    public void setMiningRareOreMultiplier(double value) {
        config.set("exp.multiplier.mining.rare_ore", value);
    }

    // === exp.multiplier.farming ===
    public double getFarmingBaseMultiplier() {
        return config.getDouble("exp.multiplier.farming.base");
    }

    public void setFarmingBaseMultiplier(double value) {
        config.set("exp.multiplier.farming.base", value);
    }

    public double getFarmingBreedingMultiplier() {
        return config.getDouble("exp.multiplier.farming.breeding");
    }

    public void setFarmingBreedingMultiplier(double value) {
        config.set("exp.multiplier.farming.breeding", value);
    }

    // === exp.multiplier.foraging ===
    public double getForagingBaseMultiplier() {
        return config.getDouble("exp.multiplier.foraging.base");
    }

    public void setForagingBaseMultiplier(double value) {
        config.set("exp.multiplier.foraging.base", value);
    }

    public double getForagingNetherMultiplier() {
        return config.getDouble("exp.multiplier.foraging.nether");
    }

    public void setForagingNetherMultiplier(double value) {
        config.set("exp.multiplier.foraging.nether", value);
    }

    // === grinding ===
    public int getGrindingMaxLevel() {
        return config.getInt("grinding.max_level");
    }

    public void setGrindingMaxLevel(int value) {
        config.set("grinding.max_level", value);
    }

    public double getExpToLevelupMultiplier() {
        return config.getDouble("grinding.exp_to_levelup_multiplier");
    }

    public void setExpToLevelupMultiplier(double value) {
        config.set("grinding.exp_to_levelup_multiplier", value);
    }

    public double getExpBoosterMultiplierPerLevelup() {
        return config.getDouble("grinding.exp_booster_multiplier_per_levelup");
    }

    public void setExpBoosterMultiplierPerLevelup(double value) {
        config.set("grinding.exp_booster_multiplier_per_levelup", value);
    }

    public double getPerkBaseBonusOnLevelup() {
        return config.getDouble("grinding.perk_bonus_on_levelup.base");
    }

    public void setPerkBaseBonusOnLevelup(double value) {
        config.set("grinding.perk_bonus_on_levelup.base", value);
    }

    public double getPerkMultiplierForLevelBonus() {
        return config.getDouble("grinding.perk_bonus_on_levelup.multiplier_for_level_bonus");
    }

    public void setPerkMultiplierForLevelBonus(double value) {
        config.set("grinding.perk_bonus_on_levelup.multiplier_for_level_bonus", value);
    }

    // === perks ===
    public double getPerkMultiplierPerStrength() {
        return config.getDouble("perks.multiplier_per_strength");
    }

    public void setPerkMultiplierPerStrength(double value) {
        config.set("perks.multiplier_per_strength", value);
    }

    public int getPerkDefenseScalingFactor() {
        return config.getInt("perks.defense_scaling_factor");
    }

    public void setPerkDefenseScalingFactor(int value) {
        config.set("perks.defense_scaling_factor", value);
    }

    public double getVanillaValuePerSpeed() {
        return config.getDouble("perks.vanilla_value_per_speed");
    }

    public void setVanillaValuePerSpeed(double value) {
        config.set("perks.vanilla_value_per_speed", value);
    }
}
