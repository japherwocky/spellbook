package me.youhavetrouble.enchantio.enchants;

import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.EquipmentSlotGroup;
import java.util.Set;

public class SoulboundEnchant extends EnchantioEnchant {
    public SoulboundEnchant() {
        super(
                Key.key("enchantio:soulbound"),
                Component.translatable("enchantio.enchant.soulbound","Soulbound"),
                1,
                1,
                10,
                EnchantmentRegistryEntry.EnchantmentCost.of(1, 1),
                EnchantmentRegistryEntry.EnchantmentCost.of(3, 1),
                Set.of(
                        ItemTypeTagKeys.AXES,
                        ItemTypeTagKeys.PICKAXES,
                        ItemTypeTagKeys.SWORDS,
                        ItemTypeTagKeys.HOES,
                        ItemTypeTagKeys.SHOVELS,
                        ItemTypeTagKeys.ENCHANTABLE_BOW,
                        ItemTypeTagKeys.ENCHANTABLE_CROSSBOW,
                        ItemTypeTagKeys.ENCHANTABLE_MACE,
                        ItemTypeTagKeys.ENCHANTABLE_WEAPON,
                        ItemTypeTagKeys.ENCHANTABLE_ARMOR
                ),
                Set.of(EquipmentSlotGroup.ANY)
        );
        registerEnchant(this);
    }
}
