package com.akira.skillmaster.base;

import com.akira.skillmaster.utils.NumberUtils;
import org.bukkit.ChatColor;

import java.util.Objects;

public abstract class AttributeType {
    protected final String registerName;
    protected final String displayName;
    protected final ChatColor color;
    protected final char icon;
    protected final String description;

    protected AttributeType(String regName, String dispName, ChatColor color, char icon, String description) {
        this.registerName = regName;
        this.displayName = dispName;
        this.color = color;
        this.icon = icon;
        this.description = description;
    }

    public final String getRegisterName() {
        return registerName;
    }

    public final String getDisplayName() {
        return displayName;
    }

    public final String getDisplayName(boolean applyColor, boolean applyIcon) {
        StringBuilder builder = new StringBuilder();

        if (applyColor) builder.append(color);
        if (applyIcon) builder.append(icon);
        builder.append(displayName);

        return builder.toString();
    }

    public final ChatColor getColor() {
        return color;
    }

    public final char getIcon() {
        return icon;
    }

    public final String toStatLine(double value, boolean applyColor, boolean applyIcon) {
        return toStatLine(value, applyColor, applyIcon, true);
    }

    public final String toStatLine(double value, boolean applyColor, boolean applyIcon, boolean simplify) {
        if (simplify) value = NumberUtils.simplify(value);

        String valueAsString = NumberUtils.toSignedNumber(value);
        StringBuilder builder = new StringBuilder();

        builder.append(displayName);
        builder.append(": ");
        if (applyColor) builder.append(color);
        builder.append(valueAsString);
        if (applyIcon) builder.append(icon);

        return builder.toString();
    }

    public final String getDescription() {
        return description;
    }

    public final boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AttributeType that = (AttributeType) o;
        return Objects.equals(registerName, that.registerName);
    }

    public final int hashCode() {
        return Objects.hashCode(registerName);
    }
}
