package me.japherwocky.spellbook.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Listener that prevents netherite gear from taking durability damage.
 * This feature is configurable via config.yml under unbreakableNetherite.enabled
 */
public class UnbreakableNetheriteListener implements Listener {

    private static final boolean ENABLED_BY_DEFAULT = true;

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerItemDamage(PlayerItemDamageEvent event) {
        ItemStack item = event.getItem();
        
        if (!isNetheriteGear(item)) {
            return;
        }

        // Set damage to 0 - netherite gear takes no damage
        // Using setDamage(0) instead of setCancelled(true) for better client sync
        event.setDamage(0);
    }

    /**
     * Checks if the given item is netherite armor or tools.
     */
    private boolean isNetheriteGear(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
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
