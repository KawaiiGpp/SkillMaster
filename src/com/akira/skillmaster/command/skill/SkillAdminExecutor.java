package com.akira.skillmaster.command.skill;

import com.akira.skillmaster.base.ImprovedExecutor;
import com.akira.skillmaster.base.SubCommand;
import com.akira.skillmaster.command.SenderLimit;
import com.akira.skillmaster.config.instances.ConfigProfile;
import com.akira.skillmaster.config.instances.ConfigSettings;
import com.akira.skillmaster.core.SkillPlayer;
import com.akira.skillmaster.core.SkillProfile;
import com.akira.skillmaster.core.skill.Skill;
import com.akira.skillmaster.core.skill.SkillEntry;
import com.akira.skillmaster.manager.ConfigManager;
import com.akira.skillmaster.manager.SkillPlayerManager;
import com.akira.skillmaster.utils.BukkitUtils;
import com.akira.skillmaster.utils.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SkillAdminExecutor extends ImprovedExecutor {
    public SkillAdminExecutor() {
        super("skilladmin");
        this.registerSubCommands();
    }

    private void registerSubCommands() {
        handler.register(new SkillCheck());
        handler.register(new SkillLevelSet());
        handler.register(new SkillExpSet());
    }

    private class SkillCheck extends SubCommand {
        SkillCheck() {
            super(SkillAdminExecutor.this.name, "check", "player", SenderLimit.UNLIMITED, "查询指定玩家的技能信息");
        }

        public boolean execute(CommandSender sender, String[] input) {
            Player onlinePlayer = Bukkit.getPlayer(input[0]);

            if (onlinePlayer == null) {
                sender.sendMessage("正在查找名为 §e" + input[0] + " §f的玩家...");

                BukkitUtils.getUniqueId(input[0], uuid -> {
                    if (this.getConfig().hasAnyData(uuid)) {
                        SkillProfile profile = new SkillProfile(uuid);
                        this.sendSkillDetails(sender, input[0], profile);
                    } else sender.sendMessage("§c未在配置文件中找到其存档信息。");
                });
            } else {
                SkillPlayer sp = SkillPlayerManager.getInstance().fromPlayer(onlinePlayer);
                this.sendSkillDetails(sender, onlinePlayer.getName(), sp);
            }

            return true;
        }

        private void sendSkillDetails(CommandSender sender, String target, SkillProfile profile) {
            sender.sendMessage("以下是关于 §e" + target + " §f的技能信息:");
            sender.sendMessage("§8§m-----------------------------------");

            for (Skill skill : Skill.getManager().copySet()) {
                ChatColor color = skill.getColor();
                SkillEntry entry = profile.getSkillData().getEntry(skill);

                double exp = NumberUtils.simplify(entry.getExp());
                double max = NumberUtils.simplify(entry.getExpRequiredToLevelup());

                String percent;
                if (entry.isLevelMaxed()) percent = "已满级";
                else percent = NumberUtils.toPercent(exp, max);

                sender.sendMessage("技能 " + skill.getDisplayName(true, true));
                sender.sendMessage("§8- §7等级: " + color + entry.getLevel());
                sender.sendMessage("§8- §7经验值: " + color + exp + "/" + max);
                sender.sendMessage("§8- §7进度: " + color + percent);
                sender.sendMessage("§8§m-----------------------------------");
            }
        }

        private ConfigProfile getConfig() {
            return (ConfigProfile) ConfigManager.getInstance().fromString("profile");
        }
    }

    private class SkillLevelSet extends SkillSet {
        SkillLevelSet() {
            super("level", "等级");
        }

        boolean onInputJudgement(CommandSender sender, String raw) {
            Integer result = NumberUtils.parseInteger(raw);
            return result != null && result > 0;
        }

        @SuppressWarnings("DataFlowIssue")
        void onSkillSet(CommandSender sender, String target, SkillProfile profile, Skill skill, String raw) {
            ConfigSettings settings = (ConfigSettings) ConfigManager.getInstance().fromString("settings");
            int level = NumberUtils.parseInteger(raw);
            int levelMax = settings.getGrindingMaxLevel();

            if (level <= levelMax) {
                String skillName = skill.getDisplayName(true, true);
                SkillEntry entry = profile.getSkillData().getEntry(skill);

                if (level != levelMax) {
                    double targetLevelExpRequired = SkillEntry.calculateExpRequiredToLevelup(level);
                    double exp = entry.getExp();
                    double max = entry.getExpRequiredToLevelup();
                    double ratio = Math.min(exp / max, 0.9999);

                    entry.setExp(entry.isLevelMaxed() ? 0 : ratio * targetLevelExpRequired);
                } else entry.setExp(0);

                entry.setLevel(level);
                sender.sendMessage("已设置 §a" + target + " §f的 " + skillName + " §f的等级为 §a" + level);
            } else sender.sendMessage("§c无法设置，等级超过最大等级限制。");
        }
    }

    private class SkillExpSet extends SkillSet {
        SkillExpSet() {
            super("exp", "经验");
        }

        boolean onInputJudgement(CommandSender sender, String raw) {
            Double result = NumberUtils.parseDouble(raw);
            return result != null && result >= 0;
        }

        @SuppressWarnings("DataFlowIssue")
        void onSkillSet(CommandSender sender, String target, SkillProfile profile, Skill skill, String raw) {
            double exp = NumberUtils.parseDouble(raw);
            SkillEntry entry = profile.getSkillData().getEntry(skill);
            String skillName = skill.getDisplayName(true, true);

            if (entry.isLevelMaxed()) {
                sender.sendMessage("§c已经满级，无法设置其经验值。");
                return;
            }

            double required = entry.getExpRequiredToLevelup();
            if (exp >= required) {
                sender.sendMessage("§c设置的经验值达到或超过上限。当前上限为 " + required + " 经验。");
                return;
            }

            sender.sendMessage("已设置 §a" + target + " §f的 " + skillName + " §f的经验为 §a" + exp);
            entry.setExp(exp);
        }
    }

    private abstract class SkillSet extends SubCommand {
        SkillSet(String arg, String translation) {
            super(SkillAdminExecutor.this.name, "set." + arg, "player.skill." + arg, SenderLimit.UNLIMITED, "设置指定玩家的技能" + translation);
        }

        public boolean execute(CommandSender sender, String[] input) {
            if (!this.onInputJudgement(sender, input[2])) {
                sender.sendMessage("§c输入的数值必须是一个合法的数字。");
                return true;
            }

            Skill skill = Skill.getManager().fromString(input[1]);
            if (skill == null) {
                sender.sendMessage("§c未找到名为 " + input[1] + " 的技能。");
                return true;
            }

            Player onlinePlayer = Bukkit.getPlayer(input[0]);
            if (onlinePlayer == null) {
                sender.sendMessage("正在查找名为 §e" + input[0] + " §f的玩家...");

                BukkitUtils.getUniqueId(input[0], uuid -> {
                    ConfigProfile config = (ConfigProfile) ConfigManager.getInstance().fromString("profile");

                    if (config.hasAnyData(uuid)) {
                        SkillProfile profile = new SkillProfile(uuid);
                        this.onSkillSet(sender, input[0], profile, skill, input[2]);
                        profile.save();
                    } else sender.sendMessage("§c未在配置文件中找到其相关存档。");
                });
            } else {
                SkillPlayer sp = SkillPlayerManager.getInstance().fromPlayer(onlinePlayer);
                this.onSkillSet(sender, onlinePlayer.getName(), sp, skill, input[2]);
            }

            return true;
        }

        abstract void onSkillSet(CommandSender sender, String target, SkillProfile profile, Skill skill, String raw);

        abstract boolean onInputJudgement(CommandSender sender, String raw);
    }
}
