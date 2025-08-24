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

import static me.japherwocky.spellbook.SpellbookConfig.ENCHANTS;

@SuppressWarnings("UnstableApiUsage")
public class PanicEnchant implements SpellbookEnchant {

    public static final Key KEY = Key.key("spellbook:panic_curse");

    private final int anvilCost, weight, maxLevel;
    private final EnchantmentRegistryEntry.EnchantmentCost minimumCost;
    private final EnchantmentRegistryEntry.EnchantmentCost maximumCost;
    private final double panicChancePerLevel;
    private final Set<TagEntry<ItemType>> supportedItemTags = new HashSet<>();
    private final Set<TagKey<Enchantment>> enchantTagKeys = new HashSet<>();
    private final Set<EquipmentSlotGroup> activeSlots = new HashSet<>();

    public PanicEnchant(
            int anvilCost,
            int weight,
            EnchantmentRegistryEntry.EnchantmentCost minimumCost,
            EnchantmentRegistryEntry.EnchantmentCost maximumCost,
            Collection<TagKey<Enchantment>> enchantTagKeys,
            Collection<TagEntry<ItemType>> supportedItemTags,
            Collection<EquipmentSlotGroup> activeSlots,
            int maxLevel,
            double panicChancePerLevel
    ) {
        this.anvilCost = anvilCost;
        this.weight = weight;
        this.maxLevel = maxLevel;
        this.minimumCost = minimumCost;
        this.maximumCost = maximumCost;
        this.supportedItemTags.addAll(supportedItemTags);
        this.panicChancePerLevel = panicChancePerLevel;
        this.activeSlots.addAll(activeSlots);
        this.enchantTagKeys.addAll(enchantTagKeys);
    }

    @Override
    public @NotNull Key getKey() {
        return KEY;
    }

    @Override
    public @NotNull Component getDescription() {
        return Component.translatable("spellbook.enchant.panic_curse", "Curse of Panic");
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

    public double getPanicChancePerLevel() {
        return panicChancePerLevel;
    }

    public static PanicEnchant create(ConfigurationSection configurationSection) {
        PanicEnchant panicEnchant = new PanicEnchant(
                SpellbookConfig.getInt(configurationSection, "anvilCost", 1),
                SpellbookConfig.getInt(configurationSection, "weight", 2),
                EnchantmentRegistryEntry.EnchantmentCost.of(
                        SpellbookConfig.getInt(configurationSection, "minimumCost.base", 0),
                        SpellbookConfig.getInt(configurationSection, "minimumCost.additionalPerLevel", 3)
                ),
                EnchantmentRegistryEntry.EnchantmentCost.of(
                        SpellbookConfig.getInt(configurationSection, "maximumCost.base", 20),
                        SpellbookConfig.getInt(configurationSection, "maximumCost.additionalPerLevel", 1)
                ),
                SpellbookConfig.getEnchantmentTagKeysFromList(SpellbookConfig.getStringList(
                        configurationSection,
                        "enchantmentTags",
                        List.of("#in_enchanting_table", "#curse")
                )),
                SpellbookConfig.getItemTagEntriesFromList(SpellbookConfig.getStringList(
                        configurationSection,
                        "supportedItemTags",
                        List.of(
                                "#minecraft:enchantable/armor"
                        )
                )),
                SpellbookConfig.getEquipmentSlotGroups(SpellbookConfig.getStringList(
                        configurationSection,
                        "activeSlots",
                        List.of(
                                "ARMOR"
                        )
                )),
                SpellbookConfig.getInt(configurationSection, "maxLevel", 1),
                SpellbookConfig.getDouble(configurationSection, "panicChancePerLevel", 0.025)
        );

        if (SpellbookConfig.getBoolean(configurationSection, "enabled", true)) {
            ENCHANTS.put(PanicEnchant.KEY, panicEnchant);
        }

        return panicEnchant;
    }

}
