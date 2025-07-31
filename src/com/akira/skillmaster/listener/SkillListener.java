package com.akira.skillmaster.listener;

import com.akira.skillmaster.config.instances.ConfigSettings;
import com.akira.skillmaster.core.SkillPlayer;
import com.akira.skillmaster.core.skill.Skill;
import com.akira.skillmaster.core.skill.SkillEntry;
import com.akira.skillmaster.manager.ConfigManager;
import com.akira.skillmaster.manager.SkillPlayerManager;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class SkillListener implements Listener {
    private final ConfigSettings settings = (ConfigSettings) ConfigManager.getInstance().fromString("settings");

    @EventHandler
    public void onCombat(EntityDeathEvent e) {
        LivingEntity entity = e.getEntity();
        EntityDamageEvent lastDamage = entity.getLastDamageCause();

        AttributeInstance attribute = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (attribute == null) return;

        if (!(lastDamage instanceof EntityDamageByEntityEvent event)) return;
        if (!(event.getDamager() instanceof Player damager)) return;

        SkillPlayer sp = SkillPlayerManager.getInstance().fromPlayer(damager);
        double base = settings.getCombatExpPerHealth() * attribute.getValue();

        double multiplier = entity instanceof Monster ?
                settings.getCombatMonsterMultiplier() :
                settings.getCombatBaseMultiplier();
        sp.getSkillData().getEntry(Skill.COMBAT).gainExp(base * multiplier);
    }

    @EventHandler
    public void onMining(BlockBreakEvent e) {
        Player player = e.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType().equals(Material.AIR)) return;
        if (item.containsEnchantment(Enchantment.SILK_TOUCH)) return;

        Material material = e.getBlock().getType();
        OreMaterial ore = OreMaterial.fromMaterial(material);

        if (!Tag.BASE_STONE_OVERWORLD.isTagged(material) &&
                (ore == null || ore.isFurnaceRequired())) return;

        SkillPlayer sp = SkillPlayerManager.getInstance().fromPlayer(player);
        SkillEntry entry = sp.getSkillData().getEntry(Skill.MINING);
        double multiplier;
        double base = settings.getMiningExpAmount();

        if (ore != null) {
            multiplier = ore.isRare() ?
                    settings.getMiningRareOreMultiplier() :
                    settings.getMiningCommonOreMultiplier();
        } else
            multiplier = settings.getMiningBaseMultiplier();

        entry.gainExp(base * multiplier);
    }

    @EventHandler
    public void onFurnace(InventoryClickEvent e) {
        //for mining skill. iron/gold etc.
    }

    private static class OreMaterial {
        public static final Set<OreMaterial> VALUES = new HashSet<>(Arrays.asList(
                new OreMaterial(false, false, Material.COAL_ORE, Material.DEEPSLATE_COAL_ORE),
                new OreMaterial(false, true, Material.IRON_ORE, Material.DEEPSLATE_IRON_ORE),
                new OreMaterial(false, true, Material.COPPER_ORE, Material.DEEPSLATE_COPPER_ORE),
                new OreMaterial(false, true, Material.GOLD_ORE, Material.DEEPSLATE_GOLD_ORE),
                new OreMaterial(false, false, Material.REDSTONE_ORE, Material.DEEPSLATE_REDSTONE_ORE),
                new OreMaterial(false, false, Material.LAPIS_ORE, Material.DEEPSLATE_LAPIS_ORE),
                new OreMaterial(true, false, Material.DIAMOND_ORE, Material.DEEPSLATE_DIAMOND_ORE),
                new OreMaterial(true, false, Material.EMERALD_ORE, Material.DEEPSLATE_EMERALD_ORE),
                new OreMaterial(false, false, Material.NETHER_QUARTZ_ORE),
                new OreMaterial(false, false, Material.NETHER_GOLD_ORE),
                new OreMaterial(true, true, Material.ANCIENT_DEBRIS))
        );

        private final boolean rare;
        private final boolean furnaceRequired;
        private final List<Material> materials;

        public OreMaterial(boolean rare, boolean furnaceRequired, Material... materials) {
            this.rare = rare;
            this.furnaceRequired = furnaceRequired;
            this.materials = new ArrayList<>(Arrays.asList(materials));
        }

        public boolean isRare() {
            return rare;
        }

        public boolean isFurnaceRequired() {
            return furnaceRequired;
        }

        public boolean includes(Material material) {
            return materials.contains(material);
        }

        public static OreMaterial fromMaterial(Material material) {
            return VALUES.stream()
                    .filter(ore -> ore.includes(material))
                    .findFirst()
                    .orElse(null);
        }
    }
}
