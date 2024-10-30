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
import org.jetbrains.annotations.NotNull;

import java.util.Set;

@SuppressWarnings("UnstableApiUsage")
public interface EnchantioEnchant {

    @NotNull
    Key getKey();

    @NotNull
    Component getDescription();

    int getAnvilCost();

    int getMaxLevel();

    int getWeight();

    @NotNull
    EnchantmentRegistryEntry.EnchantmentCost getMinimumCost();

    @NotNull
    EnchantmentRegistryEntry.EnchantmentCost getMaximumCost();

    @NotNull
    Iterable<EquipmentSlotGroup> getActiveSlots();

    @NotNull
    Set<TagEntry<ItemType>> getSupportedItems();

    @NotNull
    Set<TagKey<Enchantment>> getEnchantTagKeys();

    @NotNull
    default TagKey<ItemType> getTagForSupportedItems() {
        return TagKey.create(RegistryKey.ITEM, Key.key( getKey().asString() + "_enchantable"));
    }

    @NotNull
    default TagEntry<Enchantment> getTagEntry() {
        return TagEntry.valueEntry(TypedKey.create(RegistryKey.ENCHANTMENT, getKey()));
    }

}
