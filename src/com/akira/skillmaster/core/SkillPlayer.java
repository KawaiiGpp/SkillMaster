package com.akira.skillmaster.core;

import com.akira.skillmaster.config.instances.ConfigSettings;
import com.akira.skillmaster.core.perk.Perk;
import com.akira.skillmaster.manager.ConfigManager;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.Player;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.UUID;

@SuppressWarnings({"DataFlowIssue", "SameParameterValue"})
public class SkillPlayer extends SkillProfile {
    private static final String speedPerkIndentifier = "skillmaster.perk.speed";

    private final Player player;

    public SkillPlayer(Player player) {
        super(player.getUniqueId());
        this.player = player;
    }

    public void applyPerks() {
        this.applySpeedPerk();
    }

    public Player getPlayer() {
        return player;
    }

    private ConfigSettings getConfig() {
        return (ConfigSettings) ConfigManager.getInstance().fromString("settings");
    }

    private void applySpeedPerk() {
        double value = this.getConfig().getVanillaValuePerSpeed() * perkData.get(Perk.SPEED);

        replaceModifier(Attribute.GENERIC_MOVEMENT_SPEED, speedPerkIndentifier, value);
    }

    private void replaceModifier(Attribute attribute, String identifier, double value) {
        replaceModifier(attribute, identifier, value, Operation.ADD_NUMBER);
    }

    private void replaceModifier(Attribute attribute, String identifier, double value, Operation operation) {
        UUID uuid = UUID.nameUUIDFromBytes(identifier.getBytes(StandardCharsets.UTF_8));
        AttributeInstance instance = player.getAttribute(attribute);

        Collection<AttributeModifier> modifiers = instance.getModifiers();
        modifiers.removeIf(m -> !uuid.equals(m.getUniqueId()));
        modifiers.forEach(instance::removeModifier);

        instance.addModifier(new AttributeModifier(uuid, identifier, value, operation));
    }
}
