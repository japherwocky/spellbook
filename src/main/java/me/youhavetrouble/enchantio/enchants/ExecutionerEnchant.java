package me.youhavetrouble.enchantio.enchants;

import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.tag.TagKey;
import io.papermc.paper.tag.TagEntry;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemType;

import java.util.Set;

@SuppressWarnings("UnstableApiUsage")
public class ExecutionerEnchant implements EnchantioEnchant {

    public static final Key KEY = Key.key("enchantio:executioner");

    private final int anvilCost, weight, maxLevel;
    private final EnchantmentRegistryEntry.EnchantmentCost minimumCost;
    private final EnchantmentRegistryEntry.EnchantmentCost maximumCost;
    private final boolean canGetFromEnchantingTable;
    private final Set<TagEntry<ItemType>> supportedItemTags;
    private final double damageMultiplierPerLevel, maxDamageHpThreshold;


    public ExecutionerEnchant(
            int anvilCost,
            int weight,
            EnchantmentRegistryEntry.EnchantmentCost minimumCost,
            EnchantmentRegistryEntry.EnchantmentCost maximumCost,
            boolean canGetFromEnchantingTable,
            Set<TagEntry<ItemType>> supportedItemTags,
            int maxLevel,
            double damageMultiplierPerLevel,
            double maxDamageHpThreshold
    ) {
        this.anvilCost = anvilCost;
        this.weight = weight;
        this.minimumCost = minimumCost;
        this.maximumCost = maximumCost;
        this.canGetFromEnchantingTable = canGetFromEnchantingTable;
        this.supportedItemTags = supportedItemTags;
        this.maxLevel = maxLevel;
        this.damageMultiplierPerLevel = damageMultiplierPerLevel;
        this.maxDamageHpThreshold = maxDamageHpThreshold;
    }

    @Override
    public Key getKey() {
        return KEY;
    }

    @Override
    public Component getDescription() {
        return Component.translatable("enchantio.enchant.executioner","Executioner");
    }

    @Override
    public int getAnvilCost() {
        return anvilCost;
    }

    @Override
    public int getMaxLevel() {
        return maxLevel;
    }

    public double getDamageMultiplierPerLevel() {
        return damageMultiplierPerLevel;
    }

    public double getMaxDamageHpThreshold() {
        return maxDamageHpThreshold;
    }

    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public EnchantmentRegistryEntry.EnchantmentCost getMinimumCost() {
        return minimumCost;
    }

    @Override
    public EnchantmentRegistryEntry.EnchantmentCost getMaximumCost() {
        return maximumCost;
    }

    @Override
    public Iterable<EquipmentSlotGroup> getActiveSlots() {
        return Set.of(EquipmentSlotGroup.HAND);
    }

    @Override
    public boolean canGetFromEnchantingTable() {
        return canGetFromEnchantingTable;
    }

    @Override
    public TagKey<ItemType> getTagForSupportedItems() {
        return TagKey.create(RegistryKey.ITEM, Key.key("enchantio:executioner_enchantable"));
    }

    @Override
    public Set<TagEntry<ItemType>> getSupportedItems() {
        return supportedItemTags;
    }

}
