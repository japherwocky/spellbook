package me.youhavetrouble.enchantio.enchants;

import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.tag.TagKey;
import io.papermc.paper.tag.TagEntry;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemType;

import java.util.Set;

@SuppressWarnings("UnstableApiUsage")
public class SoulboundEnchant implements EnchantioEnchant {

    public static final Key KEY = Key.key("enchantio:soulbound");

    @Override
    public Key getKey() {
        return KEY;
    }

    @Override
    public Component getDescription() {
        return Component.translatable("enchantio.enchant.soulbound", "Soulbound");
    }

    @Override
    public int getAnvilCost() {
        return 1;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getWeight() {
        return 10;
    }

    @Override
    public EnchantmentRegistryEntry.EnchantmentCost getMinimumCost() {
        return EnchantmentRegistryEntry.EnchantmentCost.of(10, 1);
    }

    @Override
    public EnchantmentRegistryEntry.EnchantmentCost getMaximumCost() {
        return EnchantmentRegistryEntry.EnchantmentCost.of(65, 1);
    }

    @Override
    public Iterable<EquipmentSlotGroup> getActiveSlots() {
        return Set.of(EquipmentSlotGroup.ANY);
    }

    @Override
    public boolean canGetFromEnchantingTable() {
        return true;
    }

    @Override
    public TagKey<ItemType> getTagForSupportedItems() {
        return TagKey.create(RegistryKey.ITEM, Key.key("enchantio:soulbound_enchantable"));
    }

    @Override
    public Set<TagEntry<ItemType>> getSupportedItems() {
        return Set.of(
                TagEntry.tagEntry(ItemTypeTagKeys.ENCHANTABLE_ARMOR),
                TagEntry.tagEntry(ItemTypeTagKeys.ENCHANTABLE_WEAPON),
                TagEntry.tagEntry(ItemTypeTagKeys.ENCHANTABLE_MINING)
        );
    }

}
