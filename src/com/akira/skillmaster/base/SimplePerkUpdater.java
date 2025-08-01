package com.akira.skillmaster.base;

import com.akira.skillmaster.config.instances.ConfigSettings;
import com.akira.skillmaster.func.PerkUpdater;
import com.akira.skillmaster.manager.ConfigManager;
import com.akira.skillmaster.utils.AttributeUtils;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import java.util.function.Function;

public abstract class SimplePerkUpdater implements PerkUpdater {
    protected static final ConfigSettings settings = (ConfigSettings) ConfigManager.getInstance().fromString("settings");

    protected final Attribute attribute;
    protected final String identifier;
    protected final Function<Double, Double> function;

    protected SimplePerkUpdater(Attribute attribute, String identifier, Function<Double, Double> function) {
        this.attribute = attribute;
        this.identifier = identifier;
        this.function = function;
    }

    public void onUpdate(Player player, double value) {
        AttributeUtils.replace(player, attribute, identifier, function.apply(value));
    }
}
