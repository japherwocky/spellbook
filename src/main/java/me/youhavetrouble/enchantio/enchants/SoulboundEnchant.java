package me.youhavetrouble.enchantio.enchants;

import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.EquipmentSlotGroup;
import java.util.Set;

public class SoulboundEnchant extends EnchantioEnchant {

    public static final Key KEY = Key.key("enchantio:soulbound");

    public SoulboundEnchant() {
        super(
                KEY,
                Component.translatable("enchantio.enchant.soulbound","Soulbound"),
                1,
                1,
                10,
                EnchantmentRegistryEntry.EnchantmentCost.of(10, 1),
                EnchantmentRegistryEntry.EnchantmentCost.of(30, 1),
                ItemTypeTagKeys.ENCHANTABLE_ARMOR,
                ItemTypeTagKeys.ENCHANTABLE_ARMOR,
                Set.of(EquipmentSlotGroup.ANY),
                true
        );
        registerEnchant(this);
    }
}
