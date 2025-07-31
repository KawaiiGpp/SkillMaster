package com.akira.skillmaster.command.perk;

import com.akira.skillmaster.base.ImprovedExecutor;
import com.akira.skillmaster.base.SubCommand;
import com.akira.skillmaster.command.SenderLimit;
import com.akira.skillmaster.core.SkillPlayer;
import com.akira.skillmaster.core.perk.Perk;
import com.akira.skillmaster.manager.SkillPlayerManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PerkExecutor extends ImprovedExecutor {
    public PerkExecutor() {
        super("perk");
        this.registerSubCommands();
    }

    private void registerSubCommands() {
        handler.register(new PerkShow());
    }

    public class PerkShow extends SubCommand {
        PerkShow() {
            super(PerkExecutor.this.name, null, null, SenderLimit.PLAYER, "显示当前的增益数值");
        }

        public boolean execute(CommandSender sender, String[] input) {
            Player player = (Player) sender;
            SkillPlayer sp = SkillPlayerManager.getInstance().fromPlayer(player);
            player.sendMessage("当前你的增益信息如下:");

            for (Perk perk : Perk.getManager().copySet()) {
                double value = sp.getPerkData().get(perk);

                String formattedValue = perk.toStatLine(value, true, true);
                String desc = perk.getDescription();

                player.sendMessage("§8-> §7" + formattedValue + " §8(" + desc + ")");
            }

            return true;
        }
    }
}
