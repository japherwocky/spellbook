package me.japherwocky.spellbook.listeners;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import me.japherwocky.spellbook.enchants.HomecomingEnchant;
import me.japherwocky.spellbook.events.HomecomingEvent;
import org.bukkit.Location;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class HomecomingListener implements Listener {

    private final Registry<Enchantment> registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT);
    private final Enchantment homecoming = registry.get(HomecomingEnchant.KEY);

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onHomecoming(EntityResurrectEvent event) {
        if (homecoming == null) return;

        if (!(event.getEntity() instanceof Player player)) return;

        EquipmentSlot equipmentSlot = event.getHand();
        if (equipmentSlot == null) return;

        EntityEquipment entityEquipment = event.getEntity().getEquipment();
        if (entityEquipment == null) return;

        ItemStack item = entityEquipment.getItem(equipmentSlot);

        if (!item.containsEnchantment(homecoming)) return;

        Location location = player.getRespawnLocation();

        if (location == null) {
            location = player.getWorld().getSpawnLocation();
        }

        HomecomingEvent homecomingEvent = new HomecomingEvent(player, location);
        player.getServer().getPluginManager().callEvent(homecomingEvent);
        if (homecomingEvent.isCancelled()) return;

        player.teleportAsync(homecomingEvent.getLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);

    }

}
