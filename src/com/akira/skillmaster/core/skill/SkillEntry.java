package com.akira.skillmaster.core.skill;

import com.akira.skillmaster.config.instances.ConfigSettings;
import com.akira.skillmaster.core.SkillProfile;
import com.akira.skillmaster.core.perk.PerkData;
import com.akira.skillmaster.event.SkillExpGainedEvent;
import com.akira.skillmaster.event.SkillLevelledUpEvent;
import com.akira.skillmaster.manager.ConfigManager;
import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;

public class SkillEntry {
    private static final ConfigSettings settings = (ConfigSettings) ConfigManager.getInstance().fromString("settings");

    private final Skill skillType;
    private final SkillProfile profile;

    private int level;
    private double exp;

    public SkillEntry(SkillProfile profile, Skill skillType, int level, double exp) {
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
        this.skillType = skillType;
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

    public Skill getSkillType() {
        return skillType;
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

    public void gainExp(double exp) {
        double levelBooster = settings.getExpBoosterMultiplierPerLevelup() * (level - 1);
        double expGained = exp * (1 + levelBooster);

        SkillExpGainedEvent event = new SkillExpGainedEvent(profile, skillType, expGained);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled() || this.isLevelMaxed()) return;

        this.setExp(this.getExp() + event.getExpAmount());
        this.performLevelup();
    }

    private void performLevelup() {
        while (true) {
            if (this.isLevelMaxed()) {
                this.exp = 0;
                break;
            }

            double required = this.getExpRequiredToLevelup();
            if (this.exp >= required) {
                exp -= required;
                level++;

                Bukkit.getPluginManager().callEvent(new SkillLevelledUpEvent(profile, skillType, level - 1, level));
                this.onLevellingReward();
            } else break;
        }
    }

    private void onLevellingReward() {
        double base = settings.getPerkBaseBonusOnLevelup();
        double bonus = settings.getPerkMultiplierForLevelBonus() * level;

        PerkData perkData = profile.getPerkData();
        perkData.gain(skillType.getRewardType(), base + bonus);
    }

    public static double calculateExpRequiredToLevelup(int level) {
        if (level == settings.getGrindingMaxLevel()) return 0;

        double raw = Math.pow(level + 1, 3) * 10 / 4;
        double rounded = ((int) (raw / 5)) * 5;
        return rounded * settings.getExpToLevelupMultiplier();
    }
}
