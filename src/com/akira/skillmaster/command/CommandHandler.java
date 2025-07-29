package com.akira.skillmaster.command;

import com.akira.skillmaster.base.SubCommand;
import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CommandHandler {
    private final String name;
    private final List<SubCommand> subCommands;

    public CommandHandler(String name) {
        this.name = name;
        this.subCommands = new ArrayList<>();
        this.registerHelpCommand();
    }

    public void execute(CommandSender sender, String[] raw) {
        SubCommand command = this.search(raw);
        if (command == null) {
            sender.sendMessage("§c未知指令，请使用 /" + name + " help 查看可用指令。");
            return;
        }

        SenderLimit limit = command.getLimit();
        if (!limit.checkSender(sender)) {
            sender.sendMessage("§c无法执行指令: " + limit.getDescription());
            return;
        }

        String[] input = new String[command.getInput().length];
        System.arraycopy(raw, command.getPath().length, input, 0, input.length);

        boolean success = command.execute(sender, input);
        if (!success) sender.sendMessage("§c用法: " + command.getUsage());
    }

    public List<String> complete(String[] raw) {
        List<String> result = this.infer(raw).stream()
                .map(c -> c.getPath()[raw.length - 1])
                .collect(Collectors.toList());

        if (result.isEmpty()) {
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .collect(Collectors.toList());
        } else return result;
    }

    public void register(SubCommand command) {
        Validate.isTrue(!subCommands.contains(command), "Sub command already registered.");
        subCommands.add(command);
    }

    public String getName() {
        return name;
    }

    public List<SubCommand> getSubCommands() {
        return subCommands;
    }

    private SubCommand search(String[] raw) {
        return subCommands.stream()
                .filter(c -> raw.length == c.getLength())
                .filter(c -> this.isFullMatched(c.getPath(), raw))
                .findFirst().orElse(null);
    }

    private List<SubCommand> infer(String[] raw) {
        return subCommands.stream()
                .filter(c -> c.getPath().length >= raw.length)
                .filter(c -> this.isPrefixMatched(c.getPath(), raw))
                .collect(Collectors.toList());
    }

    private boolean isFullMatched(String[] path, String[] raw) {
        for (int i = 0; i < path.length; i++) {
            if (!path[i].equals(raw[i].toLowerCase())) return false;
        }
        return true;
    }

    private boolean isPrefixMatched(String[] path, String[] raw) {
        for (int i = 0; i < raw.length; i++) {
            String currentRaw = raw[i].toLowerCase();

            if (i == raw.length - 1) {
                if (!path[i].startsWith(currentRaw)) return false;
            } else {
                if (!path[i].equals(currentRaw)) return false;
            }
        }
        return true;
    }

    private void registerHelpCommand() {
        SubCommand helpCommand = new SubCommand(name,
                "help",
                null,
                SenderLimit.UNLIMITED,
                "显示所有可用的子指令") {
            public boolean execute(CommandSender sender, String[] input) {
                sender.sendMessage("§f关于 §e/" + name + " §f的全部指令:");

                for (SubCommand element : subCommands) {
                    sender.sendMessage("§8- §2" + element.getUsage());
                    sender.sendMessage("§8- §7" + element.getDescription());
                }

                return true;
            }
        };
        this.register(helpCommand);
    }
}
