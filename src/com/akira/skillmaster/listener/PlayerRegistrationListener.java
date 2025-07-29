package com.akira.skillmaster.listener;

import com.akira.skillmaster.core.SkillPlayer;
import com.akira.skillmaster.manager.SkillPlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerRegistrationListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        SkillPlayer sp = new SkillPlayer(p);

        this.getManager().register(sp);
        sp.applyPerks();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        SkillPlayerManager man = this.getManager();
        SkillPlayer sp = man.fromPlayer(e.getPlayer());

        man.unregister(sp);
        sp.save();
    }

    private SkillPlayerManager getManager() {
        return SkillPlayerManager.getInstance();
    }
}
