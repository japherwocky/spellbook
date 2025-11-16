package me.japherwocky.spellbook;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.event.RegistryEvents;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import me.japherwocky.spellbook.enchants.SpellbookEnchant;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

@SuppressWarnings("UnstableApiUsage")
public class SpellbookBootstrap implements PluginBootstrap {

    private final Logger logger = LoggerFactory.getLogger("Spellbook");

    @Override
    public void bootstrap(@NotNull BootstrapContext context) {
        try {
            SpellbookConfig.init(context.getDataDirectory());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Collection<SpellbookEnchant> spellbookEnchants = SpellbookConfig.ENCHANTS.values();

        logger.info("Registering supported item tags");
        context.getLifecycleManager().registerEventHandler(LifecycleEvents.TAGS.preFlatten(RegistryKey.ITEM).newHandler((event) -> {
            for (SpellbookEnchant enchant : spellbookEnchants) {
                logger.info("Registering item tag {}", enchant.getTagForSupportedItems().key());
                event.registrar().addToTag(
                        ItemTypeTagKeys.create(enchant.getTagForSupportedItems().key()),
                        enchant.getSupportedItems()
                );
            }
        }));

        context.getLifecycleManager().registerEventHandler(RegistryEvents.ENCHANTMENT.newHandler(event -> {
            for (SpellbookEnchant enchant : spellbookEnchants) {
                logger.info("Registering enchantment {}", enchant.getKey());
                event.registry().register(TypedKey.create(RegistryKey.ENCHANTMENT, enchant.getKey()), enchantment -> {
                    enchantment.description(enchant.getDescription());
                    enchantment.anvilCost(enchant.getAnvilCost());
                    enchantment.maxLevel(enchant.getMaxLevel());
                    enchantment.weight(enchant.getWeight());
                    enchantment.minimumCost(enchant.getMinimumCost());
                    enchantment.maximumCost(enchant.getMaximumCost());
                    enchantment.activeSlots(enchant.getActiveSlotGroups());
                    enchantment.supportedItems(event.getOrCreateTag(enchant.getTagForSupportedItems()));
                });
            }
        }));

        context.getLifecycleManager().registerEventHandler(LifecycleEvents.TAGS.preFlatten(RegistryKey.ENCHANTMENT).newHandler((event) -> {
            for (SpellbookEnchant enchant : spellbookEnchants) {
                enchant.getEnchantTagKeys().forEach(enchantmentTagKey -> {
                    event.registrar().addToTag(enchantmentTagKey, Set.of(enchant.getTagEntry()));
                });
            }
        }));

    }

}

