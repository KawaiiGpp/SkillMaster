package com.akira.skillmaster.core.skill;

import com.akira.skillmaster.config.instances.ConfigSettings;
import com.akira.skillmaster.core.SkillProfile;
import com.akira.skillmaster.event.SkillExpGainedEvent;
import com.akira.skillmaster.event.SkillLevelledUpEvent;
import com.akira.skillmaster.manager.ConfigManager;
import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;

public class SkillEntry {
    private static final ConfigSettings settings = (ConfigSettings) ConfigManager.getInstance().fromString("settings");

    private final SkillProfile profile;

    private int level;
    private double exp;

    public SkillEntry(SkillProfile profile, int level, double exp) {
        Validate.isTrue(level > 0, "Level must be greater than 0 while creating Skill Entry.");
        Validate.isTrue(exp >= 0, "Exp cannot be lower than 0 while creating Skill Entry.");

        int maxLevel = this.getMaxLevel();

        if (level <= maxLevel) {
            this.level = level;
            this.exp = exp;
        } else {
            this.level = maxLevel;
            this.exp = 0;
        }

        this.profile = profile;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        Validate.isTrue(level > 0, "Level must be greater than 0.");
        Validate.isTrue(level <= this.getMaxLevel(), "Level cannot be greater than the max level.");

        this.level = level;
    }

    public double getExp() {
        return exp;
    }

    public void setExp(double exp) {
        Validate.isTrue(exp >= 0, "Exp cannot be lower than 0.");

        this.exp = exp;
    }

    public SkillProfile getProfile() {
        return profile;
    }

    public boolean isLevelMaxed() {
        return level == this.getMaxLevel();
    }

    public int getMaxLevel() {
        return settings.getGrindingMaxLevel();
    }

    public double getExpRequiredToLevelup() {
        return calculateExpRequiredToLevelup(this.level);
    }

    public void performLevelup() {
        while (true) {
            if (this.isLevelMaxed()) {
                this.exp = 0;
                break;
            }

            double required = this.getExpRequiredToLevelup();
            if (this.exp >= required) {
                exp -= required;
                Bukkit.getPluginManager().callEvent(new SkillLevelledUpEvent(profile, level, ++level));
            } else break;
        }
    }

    public void gainExp(double exp) {
        double expGained = exp * (settings.getExpBoosterMultiplierPerLevelup() * level);
        SkillExpGainedEvent event = new SkillExpGainedEvent(profile, expGained);

        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;

        this.setExp(this.getExp() + event.getExpAmount());
        this.performLevelup();
    }

    public static double calculateExpRequiredToLevelup(int level) {
        if (level == settings.getGrindingMaxLevel()) return 0;

        double raw = Math.pow(level + 1, 3) * 10 / 4;
        double rounded = ((int) (raw / 5)) * 5;
        return rounded * settings.getExpToLevelupMultiplier();
    }
}
