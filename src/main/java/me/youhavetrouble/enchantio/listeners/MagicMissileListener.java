package me.youhavetrouble.enchantio.listeners;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.Registry;
import io.papermc.paper.registry.RegistryKey;
import me.youhavetrouble.enchantio.Enchantio;
import me.youhavetrouble.enchantio.enchants.MagicMissileEnchant;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MagicMissileListener implements Listener {

    private final Registry<Enchantment> registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT);
    private final Enchantment magicMissile = registry.get(MagicMissileEnchant.KEY);
    private final Map<UUID, Long> cooldowns = new HashMap<>();
    private final long COOLDOWN_TICKS = 40; // 2 second cooldown
    
    private final Enchantio plugin;
    
    public MagicMissileListener(Enchantio plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (magicMissile == null) return;
        
        // Only trigger on right-click with main hand
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        
        // Check if the item has the magic missile enchantment
        if (item.containsEnchantment(magicMissile)) {
            // Check cooldown
            UUID playerId = player.getUniqueId();
            long currentTime = plugin.getServer().getCurrentTick();
            
            if (cooldowns.containsKey(playerId) && currentTime - cooldowns.get(playerId) < COOLDOWN_TICKS) {
                return; // Still on cooldown
            }
            
            // Update cooldown
            cooldowns.put(playerId, currentTime);
            
            // Get enchantment level
            int level = item.getEnchantmentLevel(magicMissile);
            
            // Find target
            LivingEntity target = MagicMissileEnchant.findTarget(player, 20 + (level * 5)); // Range increases with level
            
            if (target != null) {
                // Launch magic missile
                launchMagicMissile(player, target, level);
                
                // Add exhaustion based on level (higher level = less exhaustion)
                player.setExhaustion(player.getExhaustion() + (4.0f - level));
                
                // Play sound effect
                player.playSound(player.getLocation(), Sound.ENTITY_ILLUSIONER_CAST_SPELL, 0.6f, 1.0f);
                
                // Cancel the event to prevent normal item use
                event.setCancelled(true);
            } else {
                // No target found - play failure sound
                player.playSound(player.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 0.6f, 1.5f);
            }
        }
    }
    
    /**
     * Launches a magic missile from the player to the target.
     * 
     * @param player The player casting the magic missile
     * @param target The target entity
     * @param level The enchantment level
     */
    private void launchMagicMissile(Player player, LivingEntity target, int level) {
        // Calculate damage based on level
        double damage = 2.0 + (level * 2.0); // 4/6/8 damage for levels 1/2/3
        
        // Create particle trail
        new BukkitRunnable() {
            private final Location startLoc = player.getEyeLocation();
            private final Location targetLoc = target.getEyeLocation();
            private final Vector direction = targetLoc.clone().subtract(startLoc).toVector().normalize();
            private final double distance = startLoc.distance(targetLoc);
            private final double speed = 0.5; // blocks per tick
            private final int maxTicks = (int) Math.ceil(distance / speed);
            
            private int ticks = 0;
            private Location currentLoc = startLoc.clone();
            
            @Override
            public void run() {
                if (ticks >= maxTicks) {
                    // Hit target
                    target.damage(damage, player);
                    target.getWorld().spawnParticle(
                            Particle.EXPLOSION_NORMAL, 
                            target.getEyeLocation(), 
                            10, 
                            0.2, 0.2, 0.2, 
                            0.05
                    );
                    
                    // Play hit sound
                    target.getWorld().playSound(
                            target.getLocation(),
                            Sound.ENTITY_GENERIC_HURT,
                            1.0f,
                            1.0f
                    );
                    
                    this.cancel();
                    return;
                }
                
                // Move along path
                currentLoc.add(direction.clone().multiply(speed));
                
                // Display particles
                Particle.DustOptions dustOptions = new Particle.DustOptions(
                        Color.fromRGB(75, 0, 130), // Indigo color
                        1.0f // Size
                );
                
                currentLoc.getWorld().spawnParticle(
                        Particle.REDSTONE,
                        currentLoc,
                        5, // Count
                        0.1, 0.1, 0.1, // Offset
                        0, // Speed
                        dustOptions
                );
                
                // Add some sparkle
                if (ticks % 2 == 0) {
                    currentLoc.getWorld().spawnParticle(
                            Particle.SPELL_INSTANT,
                            currentLoc,
                            2,
                            0.1, 0.1, 0.1,
                            0
                    );
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
}
