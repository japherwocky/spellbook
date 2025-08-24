package me.japherwocky.spellbook.enchants;

import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.tag.TagKey;
import io.papermc.paper.tag.TagEntry;
import me.japherwocky.spellbook.SpellbookConfig;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@SuppressWarnings("UnstableApiUsage")
public class AirbagEnchant implements SpellbookEnchant {

    public static final Key KEY = Key.key("spellbook:airbag");

    private final int anvilCost, weight, maxLevel;
    private final EnchantmentRegistryEntry.EnchantmentCost minimumCost;
    private final EnchantmentRegistryEntry.EnchantmentCost maximumCost;
    private final Set<TagEntry<ItemType>> supportedItemTags = new HashSet<>();
    private final Set<TagKey<Enchantment>> enchantTagKeys = new HashSet<>();
    private final Set<EquipmentSlotGroup> activeSlots = new HashSet<>();
    private final double damageReductionPerLevel;

    private AirbagEnchant(
            int anvilCost,
            int weight,
            EnchantmentRegistryEntry.EnchantmentCost minimumCost,
            EnchantmentRegistryEntry.EnchantmentCost maximumCost,
            Collection<TagKey<Enchantment>> enchantTagKeys,
            Collection<TagEntry<ItemType>> supportedItemTags,
            Collection<EquipmentSlotGroup> activeSlots,
            int maxLevel,
            double damageReductionPerLevel
    ) {
        this.anvilCost = anvilCost;
        this.weight = weight;
        this.maxLevel = maxLevel;
        this.minimumCost = minimumCost;
        this.maximumCost = maximumCost;
        this.supportedItemTags.addAll(supportedItemTags);
        this.damageReductionPerLevel = damageReductionPerLevel;
        this.activeSlots.addAll(activeSlots);
        this.enchantTagKeys.addAll(enchantTagKeys);
    }

    @Override
    public @NotNull Key getKey() {
        return KEY;
    }

    @Override
    public @NotNull Component getDescription() {
        return Component.translatable("spellbook.enchant.airbag", "Airbag");
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
        return activeSlots;
    }

    @Override
    public @NotNull Set<TagEntry<ItemType>> getSupportedItems() {
        return supportedItemTags;
    }

    @Override
    public @NotNull Set<TagKey<Enchantment>> getEnchantTagKeys() {
        return Collections.unmodifiableSet(enchantTagKeys);
    }

    public double getDamageReductionPerLevel() {
        return damageReductionPerLevel;
    }

    public static AirbagEnchant create(ConfigurationSection configurationSection) {
        AirbagEnchant airbagEnchant = new AirbagEnchant(
                SpellbookConfig.getInt(configurationSection, "anvilCost", 1),
                SpellbookConfig.getInt(configurationSection, "weight", 10),
                EnchantmentRegistryEntry.EnchantmentCost.of(
                        SpellbookConfig.getInt(configurationSection, "minimumCost.base", 40),
                        SpellbookConfig.getInt(configurationSection, "minimumCost.additionalPerLevel", 3)
                ),
                EnchantmentRegistryEntry.EnchantmentCost.of(
                        SpellbookConfig.getInt(configurationSection, "maximumCost.base", 65),
                        SpellbookConfig.getInt(configurationSection, "maximumCost.additionalPerLevel", 1)
                ),
                SpellbookConfig.getEnchantmentTagKeysFromList(SpellbookConfig.getStringList(
                        configurationSection,
                        "enchantmentTags",
                        List.of("#in_enchanting_table")
                )),
                SpellbookConfig.getItemTagEntriesFromList(SpellbookConfig.getStringList(
                        configurationSection,
                        "supportedItemTags",
                        List.of(
                                "minecraft:elytra"
                        )
                )),
                SpellbookConfig.getEquipmentSlotGroups(SpellbookConfig.getStringList(
                        configurationSection,
                        "activeSlots",
                        List.of(
                                "CHEST"
                        )
                )),
                SpellbookConfig.getInt(configurationSection, "maxLevel", 4),
                SpellbookConfig.getDouble(configurationSection, "damageReductionPerLevel", 0.2)
        );

        if (SpellbookConfig.getBoolean(configurationSection, "enabled", true)) {
            SpellbookConfig.ENCHANTS.put(airbagEnchant.getKey(), airbagEnchant);
        }

        return airbagEnchant;
    }

}
