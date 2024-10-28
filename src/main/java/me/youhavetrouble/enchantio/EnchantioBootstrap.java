package me.youhavetrouble.enchantio;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.event.RegistryEvents;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import me.youhavetrouble.enchantio.enchants.EnchantioEnchant;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import java.util.logging.Logger;

@SuppressWarnings("UnstableApiUsage")
public class EnchantioBootstrap implements PluginBootstrap {

    private final Logger logger = Logger.getLogger("enchantio");

    @Override
    public void bootstrap(@NotNull BootstrapContext context) {
        try {
            new EnchantioConfig(context.getDataDirectory(), logger);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Collection<EnchantioEnchant> enchantioEnchants = EnchantioConfig.ENCHANTS.values();

        logger.fine("Registering supported item tags");
        context.getLifecycleManager().registerEventHandler(LifecycleEvents.TAGS.preFlatten(RegistryKey.ITEM).newHandler((event) -> {
            for (EnchantioEnchant enchant : enchantioEnchants) {
                logger.fine("Registering item tag " + enchant.getTagForSupportedItems().key());
                event.registrar().addToTag(
                        ItemTypeTagKeys.create(enchant.getTagForSupportedItems().key()),
                        enchant.getSupportedItems()
                );
            }
        }));

        context.getLifecycleManager().registerEventHandler(RegistryEvents.ENCHANTMENT.freeze().newHandler(event -> {
            for (EnchantioEnchant enchant : enchantioEnchants) {
                logger.fine("Registering enchantment " + enchant.getKey());
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
            for (EnchantioEnchant enchant : enchantioEnchants) {
                enchant.getEnchantTagKeys().forEach(enchantmentTagKey -> {
                    logger.fine("Registering enchantment tag " + enchantmentTagKey.key());
                    event.registrar().addToTag(enchantmentTagKey, Set.of(enchant.getTagEntry()));
                });
            }
        }));

    }

}
