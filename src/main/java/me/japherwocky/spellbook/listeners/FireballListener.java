package me.japherwocky.spellbook.listeners;

import io.papermc.paper.registry.RegistryAccess;
import org.bukkit.Registry;
import io.papermc.paper.registry.RegistryKey;
import me.japherwocky.spellbook.Spellbook;
import me.japherwocky.spellbook.enchants.FireballEnchant;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FireballListener implements Listener {

    private final Enchantment fireball = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).get(FireballEnchant.KEY);
    private final Map<UUID, Long> cooldowns = new HashMap<>();
    private final long COOLDOWN_TICKS = 20; // 1 second cooldown
    
    private final Spellbook plugin;
    
    public FireballListener(Spellbook plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (fireball == null) return;
        
        // Only trigger on right-click with main hand
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        
        // Check if the item has the fireball enchantment
        if (item.containsEnchantment(fireball)) {
            // Check cooldown
            UUID playerId = player.getUniqueId();
            long currentTime = plugin.getServer().getCurrentTick();
            
            if (cooldowns.containsKey(playerId) && currentTime - cooldowns.get(playerId) < COOLDOWN_TICKS) {
                return; // Still on cooldown
            }
            
            // Update cooldown
            cooldowns.put(playerId, currentTime);
            
            // Get enchantment level
            int level = item.getEnchantmentLevel(fireball);
            
            // Create and shoot fireball
            FireballEnchant.createFireball(player, level * 0.5f); // Scale power with enchantment level
            
            // Add exhaustion based on level - 10x hunger drain (level 1 = 20 exhaustion, level 2 = 30)
            player.setExhaustion(player.getExhaustion() + (10.0f + (level * 10.0f)));
            
            // Trigger sword swing animation
            player.swingMainHand();
            
            // Play sound effect
            player.playSound(player.getLocation(), Sound.ENTITY_GHAST_SHOOT, 0.6f, 1.0f);
            
            // Cancel the event to prevent normal item use
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event) {
        cooldowns.remove(event.getPlayer().getUniqueId());
    }
}
