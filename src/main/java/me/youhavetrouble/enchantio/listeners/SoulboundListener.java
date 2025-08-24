package me.youhavetrouble.enchantio.listeners;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import me.japherwocky.spellbook.enchants.SoulboundEnchant;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class SoulboundListener implements Listener {

    private final Registry<Enchantment> registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT);
    private final Enchantment soulbound = registry.get(SoulboundEnchant.KEY);

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onSoulboundEnchantDeath(PlayerDeathEvent event) {
        if (soulbound == null) return;
        event.getPlayer().getInventory().forEach(itemStack -> {
            if (itemStack != null && itemStack.containsEnchantment(soulbound)) {
                event.getItemsToKeep().add(itemStack);
                event.getDrops().remove(itemStack);
            }
        });
    }

}
