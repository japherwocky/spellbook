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
import java.util.Map;
import java.util.Set;

@SuppressWarnings("all")
public interface EnchantioEnchant {

    public Key getKey();

    public Component getDescription();

    public int getAnvilCost();

    public int getMaxLevel();

    public int getWeight();

    public EnchantmentRegistryEntry.EnchantmentCost getMinimumCost();

    public EnchantmentRegistryEntry.EnchantmentCost getMaximumCost();

    public Iterable<EquipmentSlotGroup> getActiveSlots();

    public boolean canGetFromEnchantingTable();

    public default TagEntry<Enchantment> getTagEntry() {
        return TagEntry.valueEntry(TypedKey.create(RegistryKey.ENCHANTMENT, getKey()));
    }

    public TagKey<ItemType> getTagForSupportedItems();

    public Set<TagEntry<ItemType>> getSupportedItems();

}
