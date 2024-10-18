package me.youhavetrouble.enchantio;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.event.RegistryEvents;
import me.youhavetrouble.enchantio.enchants.EnchantioEnchant;
import me.youhavetrouble.enchantio.enchants.SoulboundEnchant;
import me.youhavetrouble.enchantio.enchants.TelepathyEnchant;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("all")
public class EnchantioBootstrap implements PluginBootstrap {
    @Override
    public void bootstrap(@NotNull BootstrapContext context) {

        EnchantioEnchant soulbound = new SoulboundEnchant();
        EnchantioEnchant telepathy = new TelepathyEnchant();

        context.getLifecycleManager().registerEventHandler(RegistryEvents.ENCHANTMENT.freeze().newHandler(event -> {
            for (EnchantioEnchant enchant : EnchantioEnchant.getEnchants().values()) {
                event.registry().register(TypedKey.create(RegistryKey.ENCHANTMENT, enchant.getKey()), enchantment -> {
                    enchantment.description(enchant.getDescription());
                    enchantment.anvilCost(enchant.getAnvilCost());
                    enchantment.maxLevel(enchant.getMaxLevel());
                    enchantment.weight(enchant.getWeight());
                    enchantment.minimumCost(enchant.getMinimumCost());
                    enchantment.maximumCost(enchant.getMaximumCost());
                    enchantment.activeSlots(enchant.getActiveSlots());
                    enchantment.supportedItems(event.getOrCreateTag(enchant.getSupportedItems()));
                    enchantment.primaryItems(event.getOrCreateTag(enchant.getPrimaryItems()));
                });
            }
        }));

    }

    @Override
    public @NotNull JavaPlugin createPlugin(@NotNull PluginProviderContext context) {
        return PluginBootstrap.super.createPlugin(context);
    }
}
