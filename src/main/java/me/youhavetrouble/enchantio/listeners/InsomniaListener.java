package me.youhavetrouble.enchantio.listeners;

import io.papermc.paper.event.player.PlayerDeepSleepEvent;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import me.youhavetrouble.enchantio.Enchantio;
import me.youhavetrouble.enchantio.enchants.InsomniaEnchant;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EntityEquipment;

public class InsomniaListener implements Listener {

    private final Registry<Enchantment> registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT);
    private final Enchantment insomnia = registry.get(InsomniaEnchant.KEY);

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onPlayerSufferingFromInsomnia(PlayerDeepSleepEvent event) {
        if (insomnia == null) return;
        Player player = event.getPlayer();
        EntityEquipment damagerEquipment = player.getEquipment();
        int level = Enchantio.getSumOfEnchantLevels(damagerEquipment, insomnia);
        if (level == 0) return;
        event.setCancelled(true);
    }

}
