package com.akira.skillmaster.listener;

import com.akira.skillmaster.config.instances.ConfigSettings;
import com.akira.skillmaster.core.SkillPlayer;
import com.akira.skillmaster.core.skill.Skill;
import com.akira.skillmaster.core.skill.SkillEntry;
import com.akira.skillmaster.event.SkillExpGainedEvent;
import com.akira.skillmaster.event.SkillLevelledUpEvent;
import com.akira.skillmaster.manager.ConfigManager;
import com.akira.skillmaster.utils.BukkitUtils;
import com.akira.skillmaster.utils.NumberUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class NotificationListener implements Listener {
    private final ConfigSettings settings = (ConfigSettings) ConfigManager.getInstance().fromString("settings");

    @EventHandler
    public void onLevelup(SkillLevelledUpEvent e) {
        if (!settings.shouldSendLevelledupNotification()) return;
        if (!e.canBeCastToPlayer()) return;

        Player player = e.getProfileAsPlayer().getPlayer();
        Skill skill = e.getSkill();
        ChatColor color = skill.getColor();
        String displayName = skill.getDisplayName(true, true);

        player.sendMessage("§6恭喜升级！§f你的" + displayName + "§f已升级至" + color + e.getLevelTo() + "§f级。");
        BukkitUtils.playSound(player, Sound.ENTITY_PLAYER_LEVELUP);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onExpGained(SkillExpGainedEvent e) {
        if (e.isCancelled()) return;
        if (!settings.shouldSendExpGainedNotification()) return;
        if (!e.canBeCastToPlayer()) return;

        Skill skill = e.getSkill();
        SkillPlayer sp = e.getProfileAsPlayer();
        Player player = sp.getPlayer();
        SkillEntry entry = sp.getSkillData().getEntry(skill);

        StringBuilder builder = new StringBuilder();
        builder.append("§3经验 +")
                .append(NumberUtils.format(e.getExpAmount()))
                .append(" §7| §3")
                .append(skill.getDisplayName(false, true))
                .append(' ');

        if (!entry.isLevelMaxed()) {
            double required = entry.getExpRequiredToLevelup();
            double exp = Math.min(e.getExpAmount() + entry.getExp(), required);

            builder.append(NumberUtils.toPercent(exp, required));
        } else builder.append("已满级");

        BaseComponent[] component = TextComponent.fromLegacyText(builder.toString());
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component);
    }
}
