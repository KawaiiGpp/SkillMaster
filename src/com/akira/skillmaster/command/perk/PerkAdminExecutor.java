package com.akira.skillmaster.command.perk;

import com.akira.skillmaster.base.ImprovedExecutor;
import com.akira.skillmaster.base.SubCommand;
import com.akira.skillmaster.command.SenderLimit;
import com.akira.skillmaster.config.instances.ConfigProfile;
import com.akira.skillmaster.core.SkillPlayer;
import com.akira.skillmaster.core.SkillProfile;
import com.akira.skillmaster.core.perk.Perk;
import com.akira.skillmaster.core.perk.PerkData;
import com.akira.skillmaster.manager.ConfigManager;
import com.akira.skillmaster.manager.SkillPlayerManager;
import com.akira.skillmaster.utils.BukkitUtils;
import com.akira.skillmaster.utils.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PerkAdminExecutor extends ImprovedExecutor {
    public PerkAdminExecutor() {
        super("perkadmin");
        this.registerSubCommands();
    }

    private void registerSubCommands() {
        handler.register(new PerkCheck());
        handler.register(new PerkSet());
    }

    private class PerkCheck extends SubCommand {
        PerkCheck() {
            super(PerkAdminExecutor.this.name, "check", "player", SenderLimit.UNLIMITED, "获取指定玩家的增益信息");
        }

        public boolean execute(CommandSender sender, String[] input) {
            Player onlinePlayer = Bukkit.getPlayer(input[0]);
            if (onlinePlayer == null) {
                sender.sendMessage("正在查找名为 §e" + input[0] + " §f的玩家...");

                BukkitUtils.getUniqueId(input[0], uuid -> {
                    if (this.getConfig().hasAnyData(uuid)) {
                        SkillProfile profile = new SkillProfile(uuid);
                        this.sendPerkDetails(sender, profile.getPerkData());
                    } else sender.sendMessage("§c未能在配置文件中找到其存档信息。");
                });
            } else {
                SkillPlayer sp = SkillPlayerManager.getInstance().fromPlayer(onlinePlayer);
                sender.sendMessage("以下是关于 §e" + onlinePlayer.getName() + " §f的增益信息:");

                this.sendPerkDetails(sender, sp.getPerkData());
            }

            return true;
        }

        private void sendPerkDetails(CommandSender recipient, PerkData data) {
            for (Perk perk : Perk.getManager().copySet()) {
                double value = data.get(perk);
                recipient.sendMessage("§8-> §7" + perk.toStatLine(value, true, true));
            }
        }

        private ConfigProfile getConfig() {
            return (ConfigProfile) ConfigManager.getInstance().fromString("profile");
        }
    }

    private class PerkSet extends SubCommand {
        PerkSet() {
            super(PerkAdminExecutor.this.name, "set", "player.perk.value", SenderLimit.UNLIMITED, "手动设置玩家的增益值");
        }

        public boolean execute(CommandSender sender, String[] input) {
            Double value = NumberUtils.parseDouble(input[2]);
            if (value == null || value < 0) {
                sender.sendMessage("§c设置的增益值必须是一个合法的数字。");
                return true;
            }

            Perk perk = Perk.getManager().fromString(input[1]);
            if (perk == null) {
                sender.sendMessage("§c不存在该增益值: " + input[1]);
                return true;
            }

            Player onlinePlayer = Bukkit.getPlayer(input[0]);
            if (onlinePlayer == null) {
                sender.sendMessage("正在查找名为 §e" + input[0] + " §f的玩家...");

                BukkitUtils.getUniqueId(input[0], uuid -> {
                    if (this.getConfig().hasAnyData(uuid)) {
                        SkillProfile profile = new SkillProfile(uuid);

                        profile.getPerkData().set(perk, value);
                        profile.save();
                        this.sendFeedback(sender, input[0], perk, value);
                    } else sender.sendMessage("§c未能在配置文件中找到其存档信息。");
                });
            } else {
                SkillPlayer sp = SkillPlayerManager.getInstance().fromPlayer(onlinePlayer);
                sp.getPerkData().set(perk, value);
                sp.applyPerks();

                this.sendFeedback(sender, onlinePlayer.getName(), perk, value);
            }

            return true;
        }

        private void sendFeedback(CommandSender sender, String target, Perk perk, double value) {
            sender.sendMessage("已把 §a" + target + " §f的 " + perk.getDisplayName(true, true) + " §f设为 §a" + value);
        }

        private ConfigProfile getConfig() {
            return (ConfigProfile) ConfigManager.getInstance().fromString("profile");
        }
    }
}
