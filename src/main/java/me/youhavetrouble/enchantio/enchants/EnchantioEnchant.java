package me.youhavetrouble.enchantio.enchants;

import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.tag.TagKey;
import io.papermc.paper.tag.TagEntry;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("all")
public abstract class EnchantioEnchant {

    private static final Map<String, EnchantioEnchant> enchantioEnchants = new HashMap();

    private final Key key;
    private final int anvilCost;
    private final int maxLevel;
    private final int weight;
    private final Component description;
    private final EnchantmentRegistryEntry.EnchantmentCost minimumCost;
    private final EnchantmentRegistryEntry.EnchantmentCost maximumCost;
    private final TagKey<ItemType> supportedItems;
    private final TagKey<ItemType> primaryItems;
    private final Set<EquipmentSlotGroup> activeSlots;
    private final boolean canGetFromEnchantingTable;

    public EnchantioEnchant(
            Key key,
            Component description,
            int anvilCost,
            int maxLevel,
            int weight,
            EnchantmentRegistryEntry.EnchantmentCost minimumCost,
            EnchantmentRegistryEntry.EnchantmentCost maximumCost,
            TagKey<ItemType> primaryItems,
            TagKey<ItemType> supportedItems,
            Set<EquipmentSlotGroup> activeSlots,
            boolean canGetFromEnchantingTable
    ) {
        this.key = key;
        this.description = description;
        this.anvilCost = anvilCost;
        this.maxLevel = maxLevel;
        this.weight = weight;
        this.minimumCost = minimumCost;
        this.maximumCost = maximumCost;
        this.primaryItems = primaryItems;
        this.supportedItems = supportedItems;
        this.activeSlots = activeSlots;
        this.canGetFromEnchantingTable = canGetFromEnchantingTable;
    }

    public Key getKey() {
        return key;
    }

    public Component getDescription() {
        return description;
    }

    public int getAnvilCost() {
        return anvilCost;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public int getWeight() {
        return weight;
    }

    public EnchantmentRegistryEntry.EnchantmentCost getMinimumCost() {
        return minimumCost;
    }

    public EnchantmentRegistryEntry.EnchantmentCost getMaximumCost() {
        return maximumCost;
    }

    public TagKey<ItemType> getSupportedItems() {
        return supportedItems;
    }

    public TagKey<ItemType> getPrimaryItems() {
        return primaryItems;
    }

    public Iterable<EquipmentSlotGroup> getActiveSlots() {
        return activeSlots;
    }

    public boolean canGetFromEnchantingTable() {
        return canGetFromEnchantingTable;
    }

    public TagEntry<Enchantment> getTagEntry() {
        return TagEntry.valueEntry(TypedKey.create(RegistryKey.ENCHANTMENT, getKey()));
    }

    protected static void registerEnchant(EnchantioEnchant enchant) {
        enchantioEnchants.put(enchant.getKey().asString(), enchant);
    }

    public static Map<String, EnchantioEnchant> getEnchants() {
        return Collections.unmodifiableMap(enchantioEnchants);
    }

}
