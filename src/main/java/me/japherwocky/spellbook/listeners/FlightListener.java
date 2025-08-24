package me.japherwocky.spellbook.listeners;

import io.papermc.paper.registry.RegistryAccess;
import org.bukkit.Registry;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.GameMode;
import me.japherwocky.spellbook.Enchantio;
import me.japherwocky.spellbook.enchants.FlightEnchant;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FlightListener implements Listener {

    private final Enchantment flight = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).get(FlightEnchant.KEY);
    private final Map<UUID, Boolean> previousFlightStates = new HashMap<>();
    
    private final Spellbook plugin;
    
    public FlightListener(Enchantio plugin) {
        this.plugin = plugin;
        
        // Start a repeating task to check for the flight enchantment
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    updateFlightAbility(player);
                }
            }
        }.runTaskTimer(plugin, 20L, 20L); // Check every second
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        // Store the player's original flight ability
        previousFlightStates.put(player.getUniqueId(), player.getAllowFlight());
        // Update flight ability based on enchantment
        updateFlightAbility(player);
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        
        // Restore original flight state if we have it stored
        if (previousFlightStates.containsKey(playerId)) {
            boolean originalState = previousFlightStates.get(playerId);
            // Only change if the player isn't in creative or spectator mode
            GameMode gameMode = player.getGameMode();
            if (gameMode != GameMode.CREATIVE && gameMode != GameMode.SPECTATOR) {
                player.setAllowFlight(originalState);
                if (!originalState) {
                    player.setFlying(false);
                }
            }
            previousFlightStates.remove(playerId);
        }
    }
    
    /**
     * Updates a player's flight ability based on whether they have the Flight enchantment
     * on their boots.
     * 
     * @param player The player to update
     */
    private void updateFlightAbility(Player player) {
        if (flight == null) return;
        
        // Don't modify flight for creative or spectator players
        GameMode gameMode = player.getGameMode();
        if (gameMode == GameMode.CREATIVE || gameMode == GameMode.SPECTATOR) return;
        
        // Check if player has boots with the flight enchantment
        ItemStack boots = player.getInventory().getBoots();
        boolean hasFlightEnchant = boots != null && boots.containsEnchantment(flight);
        
        // Update flight ability
        if (hasFlightEnchant) {
            if (!player.getAllowFlight()) {
                player.setAllowFlight(true);
            }
            
            // Add exhaustion when flying (similar to the original mod)
            if (player.isFlying()) {
                player.setExhaustion(player.getExhaustion() + 0.13F);
            }
        } else {
            // Only disable flight if we previously enabled it
            if (player.getAllowFlight() && !previousFlightStates.getOrDefault(player.getUniqueId(), false)) {
                player.setAllowFlight(false);
                player.setFlying(false);
            }
        }
    }
}
