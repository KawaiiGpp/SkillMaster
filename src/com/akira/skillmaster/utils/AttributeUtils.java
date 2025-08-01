package com.akira.skillmaster.utils;

import org.apache.commons.lang3.Validate;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.Player;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.UUID;

public class AttributeUtils {
    public static void replace(Player player, Attribute attribute, String identifier, double value) {
        replace(player, attribute, identifier, value, AttributeModifier.Operation.ADD_NUMBER);
    }

    public static void replace(Player player, Attribute attribute, String identifier, double value, Operation operation) {
        AttributeInstance instance = player.getAttribute(attribute);
        Validate.notNull(instance, "Failed getting " + attribute.name() + " from player " + player.getName());

        Collection<AttributeModifier> modifiers = instance.getModifiers();
        UUID uuid = UUID.nameUUIDFromBytes(identifier.getBytes(StandardCharsets.UTF_8));
        modifiers.removeIf(m -> !uuid.equals(m.getUniqueId()));
        modifiers.forEach(instance::removeModifier);

        instance.addModifier(new AttributeModifier(uuid, identifier, value, operation));
    }
}
