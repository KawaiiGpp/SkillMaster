package com.akira.skillmaster.base;

import com.akira.skillmaster.command.CommandHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.List;

public abstract class ImprovedExecutor implements TabExecutor {
    protected final String name;
    protected final CommandHandler handler;

    protected ImprovedExecutor(String name) {
        this.name = name;
        this.handler = new CommandHandler(name);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        handler.execute(sender, args);
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        return handler.complete(args);
    }

    public final String getName() {
        return name;
    }

    public final CommandHandler getHandler() {
        return handler;
    }
}
