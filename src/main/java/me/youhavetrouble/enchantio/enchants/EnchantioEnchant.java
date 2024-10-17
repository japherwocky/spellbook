package me.youhavetrouble.enchantio.enchants;

import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
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
    private final Set<TagKey<ItemType>> supportedItems;
    private final Set<EquipmentSlotGroup> activeSlots;

    public EnchantioEnchant(
            Key key,
            Component description,
            int anvilCost,
            int maxLevel,
            int weight,
            EnchantmentRegistryEntry.EnchantmentCost minimumCost,
            EnchantmentRegistryEntry.EnchantmentCost maximumCost,
            Set<TagKey<ItemType>> supportedItems,
            Set<EquipmentSlotGroup> activeSlots
    ) {
        this.key = key;
        this.description = description;
        this.anvilCost = anvilCost;
        this.maxLevel = maxLevel;
        this.weight = weight;
        this.minimumCost = minimumCost;
        this.maximumCost = maximumCost;
        this.supportedItems = supportedItems;
        this.activeSlots = activeSlots;
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

    public Set<TagKey<ItemType>> getSupportedItems() {
        return supportedItems;
    }

    public Iterable<EquipmentSlotGroup> getActiveSlots() {
        return activeSlots;
    }

    protected static void registerEnchant(EnchantioEnchant enchant) {
        enchantioEnchants.put(enchant.getKey().asString(), enchant);
    }

    public static Map<String, EnchantioEnchant> getEnchants() {
        return Collections.unmodifiableMap(enchantioEnchants);
    }

}
