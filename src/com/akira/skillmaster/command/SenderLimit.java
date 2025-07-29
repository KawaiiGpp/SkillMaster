package com.akira.skillmaster.command;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public enum SenderLimit {
    PLAYER(Player.class, "该操作必须由一名玩家完成"),
    CONSOLE(ConsoleCommandSender.class, "该操作只能由控制台完成"),
    UNLIMITED(Object.class, "该操作没有对执行者的身份限制");

    private final Class<?> typeClass;
    private final String description;

    SenderLimit(Class<?> typeClass, String description) {
        this.typeClass = typeClass;
        this.description = description;
    }

    public Class<?> getTypeClass() {
        return typeClass;
    }

    public String getDescription() {
        return description;
    }

    public boolean checkSender(CommandSender sender) {
        return typeClass.isAssignableFrom(sender.getClass());
    }
}
