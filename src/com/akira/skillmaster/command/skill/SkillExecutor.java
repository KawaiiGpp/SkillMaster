package com.akira.skillmaster.command.skill;

import com.akira.skillmaster.base.ImprovedExecutor;
import com.akira.skillmaster.base.SubCommand;
import com.akira.skillmaster.command.SenderLimit;
import com.akira.skillmaster.core.skill.Skill;
import com.akira.skillmaster.core.skill.SkillData;
import com.akira.skillmaster.core.skill.SkillEntry;
import com.akira.skillmaster.manager.SkillPlayerManager;
import com.akira.skillmaster.utils.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SkillExecutor extends ImprovedExecutor {
    public SkillExecutor() {
        super("skill");
        this.registerSubCommands();
    }

    private void registerSubCommands() {
        handler.register(new SkillShow());
    }

    private class SkillShow extends SubCommand {
        SkillShow() {
            super(SkillExecutor.this.name, null, null, SenderLimit.PLAYER, "显示当前个人的技能信息");
        }

        public boolean execute(CommandSender sender, String[] input) {
            Player player = (Player) sender;
            SkillData data = SkillPlayerManager.getInstance().fromPlayer(player).getSkillData();

            sender.sendMessage("§8§m-----------------------");
            for (Skill skill : Skill.getManager().copySet()) {
                ChatColor color = skill.getColor();
                SkillEntry entry = data.getEntry(skill);

                double exp = NumberUtils.simplify(entry.getExp());
                double max = NumberUtils.simplify(entry.getExpRequiredToLevelup());
                String percent = entry.isLevelMaxed() ? "已满级" : NumberUtils.toPercent(exp, max);

                sender.sendMessage("技能 " + skill.getDisplayName(true, true));
                sender.sendMessage("§8- §7当前等级: " + color + "Lvl " + entry.getLevel());
                sender.sendMessage("§8- §7经验值: " + color + exp + "/" + max + " §7§o" + percent);

                sender.sendMessage("§8§m-----------------------");
            }

            return true;
        }
    }
}
