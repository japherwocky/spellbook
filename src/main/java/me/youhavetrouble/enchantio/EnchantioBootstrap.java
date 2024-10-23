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
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

@SuppressWarnings("UnstableApiUsage")
public class EnchantioBootstrap implements PluginBootstrap {

    private final Logger logger = Logger.getLogger("enchantio");

    @Override
    public void bootstrap(@NotNull BootstrapContext context) {
        EnchantioConfig config;
        try {
            config = new EnchantioConfig(context.getDataDirectory(), logger);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Set<EnchantioEnchant> enchantioEnchants = config.enchants;

        logger.info("Registering supported item tags");
        context.getLifecycleManager().registerEventHandler(LifecycleEvents.TAGS.preFlatten(RegistryKey.ITEM).newHandler((event) -> {
            for (EnchantioEnchant enchant : enchantioEnchants) {
                logger.info("Registering item tag " + enchant.getTagForSupportedItems().key());
                event.registrar().addToTag(
                        ItemTypeTagKeys.create(enchant.getTagForSupportedItems().key()),
                        enchant.getSupportedItems()
                );
            }
        }));

        context.getLifecycleManager().registerEventHandler(RegistryEvents.ENCHANTMENT.freeze().newHandler(event -> {
            for (EnchantioEnchant enchant : enchantioEnchants) {
                logger.info("Registering enchantment " + enchant.getKey());
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
                logger.info("Registering enchantment " + enchant.getKey() + " to enchanting table possibilities");
                enchantTags.add(enchant.getTagEntry());
            }
            event.registrar().addToTag(EnchantmentTagKeys.IN_ENCHANTING_TABLE, enchantTags);
        }));

    }

}
