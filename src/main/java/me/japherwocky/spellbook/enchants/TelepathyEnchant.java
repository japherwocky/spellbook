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
public class TelepathyEnchant implements SpellbookEnchant {

    public static final Key KEY = Key.key("spellbook:telepathy");

    private final int anvilCost, weight;
    private final EnchantmentRegistryEntry.EnchantmentCost minimumCost;
    private final EnchantmentRegistryEntry.EnchantmentCost maximumCost;
    private final Set<TagEntry<ItemType>> supportedItemTags = new HashSet<>();
    private final Set<TagKey<Enchantment>> enchantTagKeys = new HashSet<>();
    private final Set<EquipmentSlotGroup> activeSlots = new HashSet<>();
    private final boolean onlyUserCanPickupItems;

    public TelepathyEnchant(
            int anvilCost,
            int weight,
            EnchantmentRegistryEntry.EnchantmentCost minimumCost,
            EnchantmentRegistryEntry.EnchantmentCost maximumCost,
            Collection<TagKey<Enchantment>> enchantTagKeys,
            Collection<TagEntry<ItemType>> supportedItemTags,
            Collection<EquipmentSlotGroup> activeSlots,
            boolean onlyUserCanPickupItems
    ) {
        this.anvilCost = anvilCost;
        this.weight = weight;
        this.minimumCost = minimumCost;
        this.maximumCost = maximumCost;
        this.supportedItemTags.addAll(supportedItemTags);
        this.activeSlots.addAll(activeSlots);
        this.enchantTagKeys.addAll(enchantTagKeys);
        this.onlyUserCanPickupItems = onlyUserCanPickupItems;
    }

    @Override
    public @NotNull Key getKey() {
        return KEY;
    }

    @Override
    public @NotNull Component getDescription() {
        return Component.translatable("spellbook.enchant.telepathy", "Telepathy");
    }

    @Override
    public int getAnvilCost() {
        return anvilCost;
    }

    @Override
    public int getMaxLevel() {
        return 1;
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

    public boolean isOnlyUserCanPickupItems() {
        return onlyUserCanPickupItems;
    }

    public static TelepathyEnchant create(ConfigurationSection configurationSection) {
        TelepathyEnchant telepathyEnchant = new TelepathyEnchant(
                SpellbookConfig.getInt(configurationSection, "anvilCost", 1),
                SpellbookConfig.getInt(configurationSection, "weight", 5),
                EnchantmentRegistryEntry.EnchantmentCost.of(
                        SpellbookConfig.getInt(configurationSection, "minimumCost.base", 15),
                        SpellbookConfig.getInt(configurationSection, "minimumCost.additionalPerLevel", 1)
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
                                "#minecraft:enchantable/mining"
                        )
                )),
                SpellbookConfig.getEquipmentSlotGroups(SpellbookConfig.getStringList(
                        configurationSection,
                        "activeSlots",
                        List.of(
                                "MAINHAND"
                        )
                )),
                SpellbookConfig.getBoolean(configurationSection, "onlyUserCanPickupItems", false)
        );

        if (SpellbookConfig.getBoolean(configurationSection, "enabled", true)) {
            ENCHANTS.put(TelepathyEnchant.KEY, telepathyEnchant);
        }

        return telepathyEnchant;
    }

}
