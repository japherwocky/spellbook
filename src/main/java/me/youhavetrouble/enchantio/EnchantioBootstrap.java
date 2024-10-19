package me.youhavetrouble.enchantio;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.event.RegistryEvents;
import io.papermc.paper.registry.keys.tags.EnchantmentTagKeys;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.tag.TagEntry;
import me.youhavetrouble.enchantio.enchants.EnchantioEnchant;
import me.youhavetrouble.enchantio.enchants.SoulboundEnchant;
import me.youhavetrouble.enchantio.enchants.TelepathyEnchant;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("UnstableApiUsage")
public class EnchantioBootstrap implements PluginBootstrap {
    @Override
    public void bootstrap(@NotNull BootstrapContext context) {

        Set<EnchantioEnchant> enchantioEnchants = Set.of(
                new SoulboundEnchant(),
                new TelepathyEnchant()
        );

        context.getLifecycleManager().registerEventHandler(LifecycleEvents.TAGS.preFlatten(RegistryKey.ITEM).newHandler((event) -> {
            for (EnchantioEnchant enchant : enchantioEnchants) {
                event.registrar().addToTag(
                        ItemTypeTagKeys.create(enchant.getTagForSupportedItems().key()),
                        enchant.getSupportedItems()
                );
            }
        }));

        context.getLifecycleManager().registerEventHandler(RegistryEvents.ENCHANTMENT.freeze().newHandler(event -> {
            for (EnchantioEnchant enchant : enchantioEnchants) {
                event.registry().register(TypedKey.create(RegistryKey.ENCHANTMENT, enchant.getKey()), enchantment -> {
                    enchantment.description(enchant.getDescription());
                    enchantment.anvilCost(enchant.getAnvilCost());
                    enchantment.maxLevel(enchant.getMaxLevel());
                    enchantment.weight(enchant.getWeight());
                    enchantment.minimumCost(enchant.getMinimumCost());
                    enchantment.maximumCost(enchant.getMaximumCost());
                    enchantment.activeSlots(enchant.getActiveSlots());
                    enchantment.supportedItems(event.getOrCreateTag(enchant.getTagForSupportedItems()));
                });
            }
        }));

        context.getLifecycleManager().registerEventHandler(LifecycleEvents.TAGS.preFlatten(RegistryKey.ENCHANTMENT).newHandler((event) -> {
            Set<TagEntry<Enchantment>> enchantTags = new HashSet<>(enchantioEnchants.size());
            for (EnchantioEnchant enchant : enchantioEnchants) {
                if (!enchant.canGetFromEnchantingTable()) continue;
                enchantTags.add(enchant.getTagEntry());
            }
            event.registrar().addToTag(EnchantmentTagKeys.IN_ENCHANTING_TABLE, enchantTags);
        }));

    }

}
