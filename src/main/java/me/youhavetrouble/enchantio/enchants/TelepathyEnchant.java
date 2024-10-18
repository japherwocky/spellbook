package me.youhavetrouble.enchantio.enchants;

import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.EquipmentSlotGroup;

import java.util.Set;

public class TelepathyEnchant extends EnchantioEnchant {

    public static final Key KEY = Key.key("enchantio:telepathy");

    public TelepathyEnchant() {
        super(
                KEY,
                Component.translatable("enchantio.enchant.telepathy","Telepathy"),
                1,
                1,
                4,
                EnchantmentRegistryEntry.EnchantmentCost.of(1, 1),
                EnchantmentRegistryEntry.EnchantmentCost.of(3, 1),
                ItemTypeTagKeys.ENCHANTABLE_MINING,
                ItemTypeTagKeys.ENCHANTABLE_MINING,
                Set.of(EquipmentSlotGroup.ANY)
        );
        registerEnchant(this);
    }

}
