package com.akira.skillmaster.listener;

import com.akira.skillmaster.SkillMaster;
import com.akira.skillmaster.config.instances.ConfigSettings;
import com.akira.skillmaster.core.SkillPlayer;
import com.akira.skillmaster.core.skill.Skill;
import com.akira.skillmaster.manager.ConfigManager;
import com.akira.skillmaster.manager.SkillPlayerManager;
import org.apache.commons.lang3.Validate;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.*;

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public class SkillListener implements Listener {
    private final ConfigSettings settings = (ConfigSettings) ConfigManager.getInstance().fromString("settings");
    private final String foragingBlockIndentifier = "skillmaster.built_foraging_block";
    private final Set<Material> ageableCropMaterials = new HashSet<>();

    public SkillListener() {
        this.initializeCropMaterials();
    }

    @EventHandler
    public void onCombat(EntityDeathEvent e) {
        LivingEntity entity = e.getEntity();
        EntityDamageEvent lastDamage = entity.getLastDamageCause();

        AttributeInstance attribute = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (attribute == null) return;

        if (!(lastDamage instanceof EntityDamageByEntityEvent event)) return;
        if (!(event.getDamager() instanceof Player damager)) return;

        SkillPlayer sp = SkillPlayerManager.getInstance().fromPlayer(damager);
        double base = settings.getCombatExpPerHealth() * attribute.getBaseValue();

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
        OreMaterial ore = OreMaterial.fromRaw(material);

        if (!Tag.BASE_STONE_OVERWORLD.isTagged(material) &&
                (ore == null || ore.isFurnaceRequired())) return;

        double multiplier = ore != null ?
                this.getRarityMultiplier(ore) :
                settings.getMiningBaseMultiplier();
        this.gainMiningExp(player, settings.getMiningExpAmount() * multiplier);
    }

    @EventHandler
    public void onFurnace(InventoryClickEvent e) {
        if (e.getSlot() != 2) return;

        if (!(e.getClickedInventory() instanceof FurnaceInventory)) return;
        if (!(e.getWhoClicked() instanceof Player player)) return;

        ItemStack item = e.getCurrentItem();
        if (item == null) return;
        OreMaterial ore = OreMaterial.fromProduct(item.getType());
        if (ore == null) return;

        double exp = settings.getMiningExpAmount() * this.getRarityMultiplier(ore);
        this.gainMiningExp(player, exp);
    }

    @EventHandler
    public void onForaging(BlockPlaceEvent e) {
        Block block = e.getBlock();
        if (!this.checkForagingBlock(block)) return;
        FixedMetadataValue value = new FixedMetadataValue(SkillMaster.getInstance(), true);
        block.setMetadata(foragingBlockIndentifier, value);
    }

    @EventHandler
    public void onForaging(BlockBreakEvent e) {
        Block block = e.getBlock();
        if (!this.checkForagingBlock(block)) return;
        if (block.hasMetadata(foragingBlockIndentifier)) return;

        double base = settings.getForagingExpAmount();
        double multiplier = this.checkNetherForagingBlock(block) ?
                settings.getForagingNetherMultiplier() :
                settings.getForagingBaseMultiplier();

        SkillPlayer sp = SkillPlayerManager.getInstance().fromPlayer(e.getPlayer());
        sp.getSkillData().getEntry(Skill.FORAGING).gainExp(base * multiplier);
    }

    @EventHandler
    public void onFarming(BlockBreakEvent e) {
        Block block = e.getBlock();

        if (!ageableCropMaterials.contains(block.getType())) return;
        if (!this.checkMaxAgeReached(block)) return;

        this.gainFarmingExp(e.getPlayer(), false);
    }

    @EventHandler
    public void onFarming(PlayerInteractEvent e) {
        Block block = e.getClickedBlock();

        if (block == null) return;
        if (!block.getType().equals(Material.SWEET_BERRY_BUSH)) return;
        if (!this.checkMaxAgeReached(block)) return;

        this.gainFarmingExp(e.getPlayer(), false);
    }

    @EventHandler
    public void onBreeding(EntityBreedEvent e) {
        if (!(e.getBreeder() instanceof Player player))
            return;

        this.gainFarmingExp(player, true);
    }

    private void initializeCropMaterials() {
        ageableCropMaterials.add(Material.WHEAT);
        ageableCropMaterials.add(Material.BEETROOTS);
        ageableCropMaterials.add(Material.CARROTS);
        ageableCropMaterials.add(Material.POTATOES);
        ageableCropMaterials.add(Material.NETHER_WART);
        ageableCropMaterials.add(Material.COCOA);
        ageableCropMaterials.add(Material.SWEET_BERRY_BUSH);

        ageableCropMaterials.forEach(material -> Validate.isTrue(material.createBlockData() instanceof Ageable,
                "Failed initializing. Material " + material.name() + " is not an instance of Ageable."));
    }

    private boolean checkForagingBlock(Block block) {
        Material material = block.getType();
        return Tag.LOGS.isTagged(material) || Tag.LEAVES.isTagged(material);
    }

    private boolean checkNetherForagingBlock(Block block) {
        return !Tag.LOGS_THAT_BURN.isTagged(block.getType());
    }

    private boolean checkMaxAgeReached(Block block) {
        if (!(block.getBlockData() instanceof Ageable ageable))
            return false;

        return ageable.getAge() == ageable.getMaximumAge();
    }

    private double getRarityMultiplier(OreMaterial ore) {
        return ore.isRare() ?
                settings.getMiningRareOreMultiplier() :
                settings.getMiningCommonOreMultiplier();
    }

    private void gainMiningExp(Player player, double exp) {
        SkillPlayerManager
                .getInstance()
                .fromPlayer(player)
                .getSkillData()
                .getEntry(Skill.MINING)
                .gainExp(exp);
    }

    private void gainFarmingExp(Player player, boolean breeding) {
        double multiplier = breeding ?
                settings.getFarmingBreedingMultiplier() :
                settings.getFarmingBaseMultiplier();
        double exp = settings.getFarmingExpAmount() * multiplier;

        SkillPlayer sp = SkillPlayerManager.getInstance().fromPlayer(player);
        sp.getSkillData().getEntry(Skill.FARMING).gainExp(exp);
    }

    private static class OreMaterial {
        public static final Set<OreMaterial> VALUES = new HashSet<>();

        public static final OreMaterial COAL = new OreMaterial(false, false, Material.COAL, Material.COAL_ORE, Material.DEEPSLATE_COAL_ORE);
        public static final OreMaterial IRON = new OreMaterial(false, true, Material.IRON_INGOT, Material.IRON_ORE, Material.DEEPSLATE_IRON_ORE);
        public static final OreMaterial COPPER = new OreMaterial(false, true, Material.COPPER_INGOT, Material.COPPER_ORE, Material.DEEPSLATE_COPPER_ORE);
        public static final OreMaterial GOLD = new OreMaterial(false, true, Material.GOLD_INGOT, Material.GOLD_ORE, Material.DEEPSLATE_GOLD_ORE);
        public static final OreMaterial REDSTONE = new OreMaterial(false, false, Material.REDSTONE, Material.REDSTONE_ORE, Material.DEEPSLATE_REDSTONE_ORE);
        public static final OreMaterial LAPIS = new OreMaterial(false, false, Material.LAPIS_LAZULI, Material.LAPIS_ORE, Material.DEEPSLATE_LAPIS_ORE);
        public static final OreMaterial DIAMOND = new OreMaterial(true, false, Material.DIAMOND, Material.DIAMOND_ORE, Material.DEEPSLATE_DIAMOND_ORE);
        public static final OreMaterial EMERALD = new OreMaterial(true, false, Material.EMERALD, Material.EMERALD_ORE, Material.DEEPSLATE_EMERALD_ORE);
        public static final OreMaterial QUARTZ = new OreMaterial(false, false, Material.QUARTZ, Material.NETHER_QUARTZ_ORE);
        public static final OreMaterial NETHER_GOLD = new OreMaterial(false, false, Material.GOLD_INGOT, Material.NETHER_GOLD_ORE);
        public static final OreMaterial SCARP = new OreMaterial(true, true, Material.NETHERITE_SCRAP, Material.ANCIENT_DEBRIS);

        private final boolean rare;
        private final boolean furnaceRequired;
        private final Material product;
        private final List<Material> raws;

        public OreMaterial(boolean rare, boolean furnaceRequired, Material product, Material... raws) {
            this.rare = rare;
            this.furnaceRequired = furnaceRequired;
            this.product = product;
            this.raws = new ArrayList<>(Arrays.asList(raws));

            VALUES.add(this);
        }

        public boolean isRare() {
            return rare;
        }

        public boolean isFurnaceRequired() {
            return furnaceRequired;
        }

        public boolean includesRaw(Material raw) {
            return this.raws.contains(raw);
        }

        public boolean matchesProduct(Material product) {
            return this.product.equals(product);
        }

        public static OreMaterial fromRaw(Material material) {
            return VALUES.stream()
                    .filter(ore -> ore.includesRaw(material))
                    .findFirst()
                    .orElse(null);
        }

        public static OreMaterial fromProduct(Material material) {
            return VALUES.stream()
                    .filter(ore -> ore.matchesProduct(material))
                    .findFirst()
                    .orElse(null);
        }
    }
}
