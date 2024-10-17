package me.youhavetrouble.enchantio.listeners;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import me.youhavetrouble.enchantio.Enchantio;
import net.kyori.adventure.key.Key;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class SoulboundListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onSoulboundEnchantDeath(PlayerDeathEvent event) {
        Registry<Enchantment> registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT);
        Enchantment soulbound = registry.get(Key.key("enchantio:soulbound"));
        Enchantio.getPlugin(Enchantio.class).getLogger().info("Soulbound enchantment: " + soulbound);

        event.getPlayer().getInventory().forEach(itemStack -> {
            if (itemStack != null && itemStack.getEnchantments().containsKey(soulbound)) {
                event.getItemsToKeep().add(itemStack);
                event.getDrops().remove(itemStack);
            }
        });

    }

}
