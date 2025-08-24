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

import static me.youhavetrouble.enchantio.SpellbookConfig.ENCHANTS;

@SuppressWarnings("UnstableApiUsage")
public class VolleyEnchant implements SpellbookEnchant {

    public static final Key KEY = Key.key("spellbook:volley");

    private final int anvilCost, weight, maxLevel, additionalArrowsPerLevel;
    private final EnchantmentRegistryEntry.EnchantmentCost minimumCost;
    private final EnchantmentRegistryEntry.EnchantmentCost maximumCost;
    private final Set<TagEntry<ItemType>> supportedItemTags = new HashSet<>();
    private final Set<TagKey<Enchantment>> enchantTagKeys = new HashSet<>();
    private final Set<EquipmentSlotGroup> activeSlots = new HashSet<>();
    private final double spread;

    public VolleyEnchant(
            int anvilCost,
            int weight,
            EnchantmentRegistryEntry.EnchantmentCost minimumCost,
            EnchantmentRegistryEntry.EnchantmentCost maximumCost,
            Collection<TagKey<Enchantment>> enchantTagKeys,
            Collection<TagEntry<ItemType>> supportedItemTags,
            Collection<EquipmentSlotGroup> activeSlots,
            int maxLevel,
            int additionalArrowsPerLevel,
            double spread
    ) {
        this.anvilCost = anvilCost;
        this.weight = weight;
        this.minimumCost = minimumCost;
        this.maximumCost = maximumCost;
        this.supportedItemTags.addAll(supportedItemTags);
        this.maxLevel = maxLevel;
        this.activeSlots.addAll(activeSlots);
        this.additionalArrowsPerLevel = additionalArrowsPerLevel;
        this.spread = spread;
        this.enchantTagKeys.addAll(enchantTagKeys);
    }

    @Override
    public @NotNull Key getKey() {
        return KEY;
    }

    @Override
    public @NotNull Component getDescription() {
        return Component.translatable("spellbook.enchant.volley","Volley");
    }

    @Override
    public int getAnvilCost() {
        return anvilCost;
    }

    @Override
    public int getMaxLevel() {
        return maxLevel;
    }

    public int getAdditionalArrowsPerLevel() {
        return additionalArrowsPerLevel;
    }

    public double getSpread() {
        return spread;
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

    public static VolleyEnchant create(ConfigurationSection configurationSection) {
        VolleyEnchant executionerEnchant = new VolleyEnchant(
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
                                "minecraft:bow"
                        )
                )),
                SpellbookConfig.getEquipmentSlotGroups(SpellbookConfig.getStringList(
                        configurationSection,
                        "activeSlots",
                        List.of(
                                "MAINHAND"
                        )
                )),
                SpellbookConfig.getInt(configurationSection, "maxLevel", 3),
                SpellbookConfig.getInt(configurationSection, "additionalArrowsPerLevel", 1),
                SpellbookConfig.getDouble(configurationSection, "spread", 0.5)
        );

        if (SpellbookConfig.getBoolean(configurationSection, "enabled", true)) {
            ENCHANTS.put(VolleyEnchant.KEY, executionerEnchant);
        }

        return executionerEnchant;
    }

}
