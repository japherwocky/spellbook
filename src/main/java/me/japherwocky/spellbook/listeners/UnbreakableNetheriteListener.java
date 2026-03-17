package me.japherwocky.spellbook.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

/**
 * Listener that prevents netherite gear from taking durability damage.
 * This feature is configurable via config.yml under unbreakableNetherite.enabled
 */
public class UnbreakableNetheriteListener implements Listener {

    private final Plugin plugin;

    public UnbreakableNetheriteListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerItemDamage(PlayerItemDamageEvent event) {
        ItemStack item = event.getItem();
        
        if (!isNetheriteGear(item)) {
            return;
        }

        // Cancel the damage event entirely
        event.setCancelled(true);
        
        // Schedule a task to ensure durability is reset (handles any visual desync)
        // This runs after the event is fully processed
        ItemStack itemSnapshot = item.clone();
        Bukkit.getScheduler().runTask(plugin, () -> {
            // If the item has any durability damage, reset it to 0 (full)
            ItemMeta meta = itemSnapshot.getItemMeta();
            if (meta instanceof Damageable damageable && damageable.hasDamage()) {
                damageable.setDamage(0);
                itemSnapshot.setItemMeta(meta);
            }
        });
    }

    /**
     * Checks if the given item is netherite armor or tools.
     */
    private boolean isNetheriteGear(ItemStack item) {
        if (item == null) {
            return false;
        }

        Material type = item.getType();
        
        return switch (type) {
            // Armor
            case NETHERITE_HELMET,
                 NETHERITE_CHESTPLATE,
                 NETHERITE_LEGGINGS,
                 NETHERITE_BOOTS,
                 // Tools
                 NETHERITE_SWORD,
                 NETHERITE_PICKAXE,
                 NETHERITE_AXE,
                 NETHERITE_SHOVEL,
                 NETHERITE_HOE -> true;
            default -> false;
        };
    }
}
