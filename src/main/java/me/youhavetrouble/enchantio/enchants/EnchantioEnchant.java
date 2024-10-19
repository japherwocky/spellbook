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

import java.util.Set;

@SuppressWarnings("UnstableApiUsage")
public interface EnchantioEnchant {

    Key getKey();

    Component getDescription();

    int getAnvilCost();

    int getMaxLevel();

    int getWeight();

    EnchantmentRegistryEntry.EnchantmentCost getMinimumCost();

    EnchantmentRegistryEntry.EnchantmentCost getMaximumCost();

    Iterable<EquipmentSlotGroup> getActiveSlots();

    boolean canGetFromEnchantingTable();

    TagKey<ItemType> getTagForSupportedItems();

    Set<TagEntry<ItemType>> getSupportedItems();

    default TagEntry<Enchantment> getTagEntry() {
        return TagEntry.valueEntry(TypedKey.create(RegistryKey.ENCHANTMENT, getKey()));
    }

}
