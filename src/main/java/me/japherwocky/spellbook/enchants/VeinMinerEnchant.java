package me.japherwocky.spellbook.enchants;

import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.tag.TagKey;
import io.papermc.paper.tag.TagEntry;
import me.japherwocky.spellbook.SpellbookConfig;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static me.japherwocky.spellbook.SpellbookConfig.ENCHANTS;

@SuppressWarnings("UnstableApiUsage")
public class VeinMinerEnchant implements SpellbookEnchant {

    public static final Key KEY = Key.key("spellbook:veinminer");

    // Configuration values (static, set during creation)
    public static int MAX_BLOCKS_BASE = 8;
    public static int MAX_BLOCKS_PER_LEVEL = 8;
    public static int HUNGER_COST_PER_BLOCK = 1;
    public static int SEARCH_RADIUS = 1;
    public static boolean RESPECT_DURABILITY = true;
    public static boolean RESPECT_HUNGER = true;
    public static boolean REQUIRE_CORRECT_TOOL = true;
    public static Set<Material> WHITELISTED_BLOCKS = new HashSet<>();
    public static boolean USE_WHITELIST = false;

    private final int anvilCost, weight, maxLevel;
    private final EnchantmentRegistryEntry.EnchantmentCost minimumCost;
    private final EnchantmentRegistryEntry.EnchantmentCost maximumCost;
    private final Set<TagEntry<ItemType>> supportedItemTags = new HashSet<>();
    private final Set<TagKey<Enchantment>> enchantTagKeys = new HashSet<>();

    public VeinMinerEnchant(
            int anvilCost,
            int weight,
            int maxLevel,
            EnchantmentRegistryEntry.EnchantmentCost minimumCost,
            EnchantmentRegistryEntry.EnchantmentCost maximumCost,
            Collection<TagKey<Enchantment>> enchantTagKeys,
            Collection<TagEntry<ItemType>> supportedItemTags
    ) {
        this.anvilCost = anvilCost;
        this.weight = weight;
        this.maxLevel = maxLevel;
        this.minimumCost = minimumCost;
        this.maximumCost = maximumCost;
        this.supportedItemTags.addAll(supportedItemTags);
        this.enchantTagKeys.addAll(enchantTagKeys);
    }

    @Override
    public @NotNull Key getKey() {
        return KEY;
    }

    @Override
    public @NotNull Component getDescription() {
        return Component.translatable("spellbook.enchant.veinminer", "Vein Miner");
    }

    @Override
    public int getAnvilCost() {
        return anvilCost;
    }

    @Override
    public int getMaxLevel() {
        return maxLevel;
    }

    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public EnchantmentRegistryEntry.@NotNull EnchantmentCost getMinimumCost() {
        return minimumCost;
    }

    @Override
    public EnchantmentRegistryEntry.@NotNull EnchantmentCost getMaximumCost() {
        return maximumCost;
    }

    @Override
    public @NotNull Set<EquipmentSlotGroup> getActiveSlotGroups() {
        return Set.of(EquipmentSlotGroup.MAINHAND);
    }

    @Override
    public @NotNull Set<TagEntry<ItemType>> getSupportedItems() {
        return supportedItemTags;
    }

    @Override
    public @NotNull Set<TagKey<Enchantment>> getEnchantTagKeys() {
        return Collections.unmodifiableSet(enchantTagKeys);
    }

    public static VeinMinerEnchant create(ConfigurationSection config) {
        // Load configuration values
        MAX_BLOCKS_BASE = SpellbookConfig.getInt(config, "maxBlocksBase", 8);
        MAX_BLOCKS_PER_LEVEL = SpellbookConfig.getInt(config, "maxBlocksPerLevel", 8);
        HUNGER_COST_PER_BLOCK = SpellbookConfig.getInt(config, "hungerCostPerBlock", 1);
        SEARCH_RADIUS = SpellbookConfig.getInt(config, "searchRadius", 1);
        RESPECT_DURABILITY = SpellbookConfig.getBoolean(config, "respectDurability", true);
        RESPECT_HUNGER = SpellbookConfig.getBoolean(config, "respectHunger", true);
        REQUIRE_CORRECT_TOOL = SpellbookConfig.getBoolean(config, "requireCorrectTool", true);
        USE_WHITELIST = SpellbookConfig.getBoolean(config, "useWhitelist", false);

        // Load whitelisted blocks
        WHITELISTED_BLOCKS.clear();
        List<String> blockList = SpellbookConfig.getStringList(config, "whitelistedBlocks", getDefaultWhitelistedBlocks());
        for (String blockName : blockList) {
            try {
                Material material = Material.valueOf(blockName.toUpperCase());
                WHITELISTED_BLOCKS.add(material);
            } catch (IllegalArgumentException ignored) {
                // Invalid material name, skip
            }
        }

        VeinMinerEnchant veinMinerEnchant = new VeinMinerEnchant(
                SpellbookConfig.getInt(config, "anvilCost", 4),
                SpellbookConfig.getInt(config, "weight", 5),
                SpellbookConfig.getInt(config, "maxLevel", 3),
                EnchantmentRegistryEntry.EnchantmentCost.of(
                        SpellbookConfig.getInt(config, "minimumCost.base", 15),
                        SpellbookConfig.getInt(config, "minimumCost.additionalPerLevel", 8)
                ),
                EnchantmentRegistryEntry.EnchantmentCost.of(
                        SpellbookConfig.getInt(config, "maximumCost.base", 50),
                        SpellbookConfig.getInt(config, "maximumCost.additionalPerLevel", 10)
                ),
                SpellbookConfig.getEnchantmentTagKeysFromList(SpellbookConfig.getStringList(
                        config,
                        "enchantmentTags",
                        List.of("#in_enchanting_table")
                )),
                SpellbookConfig.getItemTagEntriesFromList(SpellbookConfig.getStringList(
                        config,
                        "supportedItemTags",
                        List.of("#minecraft:enchantable/mining")
                ))
        );

        if (SpellbookConfig.getBoolean(config, "enabled", false)) {
            ENCHANTS.put(VeinMinerEnchant.KEY, veinMinerEnchant);
        }

        return veinMinerEnchant;
    }

    private static List<String> getDefaultWhitelistedBlocks() {
        // Default to common ores that make sense for vein mining
        return List.of(
                "COAL_ORE", "DEEPSLATE_COAL_ORE",
                "IRON_ORE", "DEEPSLATE_IRON_ORE",
                "COPPER_ORE", "DEEPSLATE_COPPER_ORE",
                "GOLD_ORE", "DEEPSLATE_GOLD_ORE",
                "REDSTONE_ORE", "DEEPSLATE_REDSTONE_ORE",
                "LAPIS_ORE", "DEEPSLATE_LAPIS_ORE",
                "DIAMOND_ORE", "DEEPSLATE_DIAMOND_ORE",
                "EMERALD_ORE", "DEEPSLATE_EMERALD_ORE",
                "NETHER_GOLD_ORE", "NETHER_QUARTZ_ORE",
                "COPPER_ORE", "DEEPSLATE_COPPER_ORE"
        );
    }

}