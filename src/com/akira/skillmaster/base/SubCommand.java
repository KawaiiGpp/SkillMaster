package com.akira.skillmaster.base;

import com.akira.skillmaster.command.SenderLimit;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Objects;

public abstract class SubCommand {
    protected final String name;
    protected final String[] path;
    protected final String[] input;
    protected final int length;
    protected final SenderLimit limit;
    protected final String usage;
    protected final String description;

    protected SubCommand(String name, String path, String input, SenderLimit limit, String desc) {
        this.name = name;
        this.path = path == null ? new String[0] : path.toLowerCase().split("\\.");
        this.input = input == null ? new String[0] : input.split("\\.");
        this.length = this.path.length + this.input.length;
        this.limit = limit;
        this.usage = this.generateUsage();
        this.description = desc;
    }

    public final String getName() {
        return name;
    }

    public final String[] getPath() {
        return path;
    }

    public final String[] getInput() {
        return input;
    }

    public final int getLength() {
        return length;
    }

    public final SenderLimit getLimit() {
        return limit;
    }

    public final String getUsage() {
        return usage;
    }

    public final String getDescription() {
        return description;
    }

    public final boolean equals(Object o) {
        if (!(o instanceof SubCommand that)) return false;
        return length == that.length && Objects.equals(name, that.name) && Objects.deepEquals(path, that.path);
    }

    public final int hashCode() {
        return Objects.hash(name, Arrays.hashCode(path), length);
    }

    public abstract boolean execute(CommandSender sender, String[] input);

    private String generateUsage() {
        StringBuilder builder = new StringBuilder();
        builder.append('/').append(name);

        for (String element : path) {
            builder.append(' ');
            builder.append(element);
        }

        for (String element : input) {
            builder.append(' ');
            builder.append('<');
            builder.append(element);
            builder.append('>');
        }

        return builder.toString();
    }
}
