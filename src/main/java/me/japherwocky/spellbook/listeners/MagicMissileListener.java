package me.japherwocky.spellbook.listeners;

import io.papermc.paper.registry.RegistryAccess;
import org.bukkit.Registry;
import io.papermc.paper.registry.RegistryKey;
import me.japherwocky.spellbook.Spellbook;
import me.japherwocky.spellbook.enchants.MagicMissileEnchant;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MagicMissileListener implements Listener {

    private final Enchantment magicMissile = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).get(MagicMissileEnchant.KEY);
    private final Map<UUID, Long> cooldowns = new HashMap<>();
    private final Map<UUID, BukkitRunnable> chargeTasks = new HashMap<>(); // Active charging tasks
    private final Map<UUID, Long> chargeStartTimes = new HashMap<>(); // When charging started
    private final long COOLDOWN_TICKS = 20; // 1 second cooldown
    private static final long MAX_CHARGE_TICKS = 20; // 1 second to fully charge (like bow)
    private static final long MIN_CHARGE_TICKS = 5; // Minimum charge to fire (0.25 seconds)
    
    // Arrow velocity (40% faster than regular bow)
    private static final double MIN_VELOCITY = 0.56; // 0.4 * 1.4
    private static final double MAX_VELOCITY = 4.2; // 3.0 * 1.4
    
    private final Spellbook plugin;
    
    public MagicMissileListener(Spellbook plugin) {
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
        if (!item.containsEnchantment(magicMissile)) return;
        
        UUID playerId = player.getUniqueId();
        
        // Check cooldown
        long currentTime = plugin.getServer().getCurrentTick();
        if (cooldowns.containsKey(playerId) && currentTime - cooldowns.get(playerId) < COOLDOWN_TICKS) {
            return; // Still on cooldown
        }
        
        // Check if already charging
        if (chargeStartTimes.containsKey(playerId)) {
            return; // Already charging
        }
        
        // Start charging
        chargeStartTimes.put(playerId, currentTime);
        event.setCancelled(true);
        
        // Play charge start sound
        player.playSound(player.getLocation(), Sound.ITEM_CROSSBOW_LOADING_START, 0.6f, 1.2f);
        
        // Start a task to monitor charging and fire on release
        BukkitRunnable chargeTask = new BukkitRunnable() {
            private boolean fired = false;
            private int ticks = 0;
            
            @Override
            public void run() {
                if (fired) return;
                
                // Display charging particles
                displayChargeParticles(player, ticks);
                ticks++;
                
                // Check if player is still holding right-click (hand raised)
                if (!player.isHandRaised()) {
                    // Player released - fire the missile
                    fired = true;
                    fireMissile(player, item);
                    this.cancel();
                }
            }
        };
        
        chargeTask.runTaskTimer(plugin, 1L, 1L);
        chargeTasks.put(playerId, chargeTask);
    }
    
    /**
     * Displays particles while the player is charging.
     */
    private void displayChargeParticles(Player player, int ticks) {
        // Calculate charge progress (0.0 to 1.0)
        long chargeStart = chargeStartTimes.getOrDefault(player.getUniqueId(), 0L);
        long currentTime = plugin.getServer().getCurrentTick();
        long chargeTime = currentTime - chargeStart;
        double progress = Math.min(1.0, (double) chargeTime / MAX_CHARGE_TICKS);
        
        Location eyeLoc = player.getEyeLocation();
        Vector direction = eyeLoc.getDirection();
        
        // Spawn particles in front of player, spiraling inward as charge increases
        double distance = 1.5 - progress * 0.5; // Particles get closer as charge increases
        double angle = ticks * 0.3; // Rotation speed
        
        Location particleLoc = eyeLoc.clone()
                .add(direction.clone().multiply(distance))
                .add(Math.cos(angle) * 0.3, Math.sin(angle) * 0.3, 0);
        
        // Rotate the offset to be perpendicular to the look direction
        Vector right = new Vector(-direction.getZ(), 0, direction.getX()).normalize();
        Vector up = direction.clone().crossProduct(right).normalize();
        
        particleLoc = eyeLoc.clone()
                .add(direction.clone().multiply(distance))
                .add(right.clone().multiply(Math.cos(angle) * 0.3))
                .add(up.clone().multiply(Math.sin(angle) * 0.3));
        
        // Color shifts from purple to bright magenta as charge increases
        int red = (int) (75 + progress * 180); // 75 -> 255
        int green = (int) (progress * 50); // 0 -> 50
        int blue = (int) (130 + progress * 125); // 130 -> 255
        
        Particle.DustOptions dustOptions = new Particle.DustOptions(
                Color.fromRGB(red, green, blue),
                0.6f + (float) progress * 0.4f // Size increases with charge
        );
        
        player.getWorld().spawnParticle(
                Particle.DUST,
                particleLoc,
                2,
                0.05, 0.05, 0.05,
                0,
                dustOptions
        );
        
        // Add sparkle at full charge
        if (progress >= 1.0 && ticks % 4 == 0) {
            player.getWorld().spawnParticle(
                    Particle.INSTANT_EFFECT,
                    particleLoc,
                    1,
                    0.1, 0.1, 0.1,
                    0
            );
        }
    }
    
    /**
     * Fires the magic missile when the player releases their charge.
     */
    private void fireMissile(Player player, ItemStack item) {
        UUID playerId = player.getUniqueId();
        
        // Get charge time
        long chargeStart = chargeStartTimes.getOrDefault(playerId, 0L);
        long currentTime = plugin.getServer().getCurrentTick();
        long chargeTime = currentTime - chargeStart;
        
        // Clean up tracking
        chargeStartTimes.remove(playerId);
        chargeTasks.remove(playerId);
        
        // Check minimum charge
        if (chargeTime < MIN_CHARGE_TICKS) {
            // Not charged enough - play failure sound
            player.playSound(player.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 0.5f, 1.5f);
            return;
        }
        
        // Cap charge time at max
        chargeTime = Math.min(chargeTime, MAX_CHARGE_TICKS);
        
        // Calculate charge ratio (0.0 to 1.0)
        double chargeRatio = (double) (chargeTime - MIN_CHARGE_TICKS) / (MAX_CHARGE_TICKS - MIN_CHARGE_TICKS);
        chargeRatio = Math.max(0.0, Math.min(1.0, chargeRatio));
        
        // Get enchantment level
        int level = item.getEnchantmentLevel(magicMissile);
        
        // Set cooldown
        cooldowns.put(playerId, currentTime);
        
        // Spawn the arrow
        spawnMagicMissile(player, level, chargeRatio);
        
        // Swing hand animation
        player.swingMainHand();
        
        // Play launch sound
        player.playSound(player.getLocation(), Sound.ENTITY_ARROW_SHOOT, 0.8f, 1.2f);
        
        // Add exhaustion (less than bow, but scales with charge)
        player.setExhaustion(player.getExhaustion() + (float) (2.0 + chargeRatio * 2.0));
    }
    
    /**
     * Spawns a magic missile (arrow) with particle trail.
     */
    private void spawnMagicMissile(Player player, int level, double chargeRatio) {
        Location eyeLocation = player.getEyeLocation();
        Vector direction = eyeLocation.getDirection();
        
        // Spawn arrow slightly in front of player
        Location spawnLoc = eyeLocation.clone().add(direction.clone().multiply(0.5));
        
        Arrow arrow = player.getWorld().spawn(spawnLoc, Arrow.class);
        arrow.setShooter(player);
        arrow.setPickupStatus(Arrow.PickupStatus.DISALLOWED); // Can't be picked up
        
        // Calculate velocity based on charge (40% faster than bow)
        double velocity = MIN_VELOCITY + chargeRatio * (MAX_VELOCITY - MIN_VELOCITY);
        
        arrow.setVelocity(direction.clone().multiply(velocity));
        
        // Set damage based on level and charge
        // Base damage: 2.0 + (level * 2.0), scaled by charge ratio
        double baseDamage = 2.0 + (level * 2.0); // 4/6/8 for levels 1/2/3
        double damage = baseDamage * (0.5 + chargeRatio * 0.5); // 50-100% damage based on charge
        arrow.setDamage(damage);
        
        // Make arrow glow for visibility
        arrow.setGlowing(true);
        
        // Add particle trail
        new BukkitRunnable() {
            private int ticks = 0;
            private static final int MAX_TICKS = 100; // 5 seconds max flight time
            
            @Override
            public void run() {
                // Stop if arrow is no longer valid
                if (arrow.isDead() || !arrow.isValid() || arrow.isInBlock() || ticks >= MAX_TICKS) {
                    arrow.remove();
                    this.cancel();
                    return;
                }
                
                // Display particle trail
                Particle.DustOptions dustOptions = new Particle.DustOptions(
                        Color.fromRGB(75, 0, 130), // Indigo color
                        0.8f // Size
                );
                
                arrow.getWorld().spawnParticle(
                        Particle.DUST,
                        arrow.getLocation(),
                        3, // Count
                        0.05, 0.05, 0.05, // Offset
                        0, // Speed
                        dustOptions
                );
                
                // Add sparkle effect occasionally
                if (ticks % 3 == 0) {
                    arrow.getWorld().spawnParticle(
                            Particle.INSTANT_EFFECT,
                            arrow.getLocation(),
                            1,
                            0.1, 0.1, 0.1,
                            0
                    );
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 1L, 1L);
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        chargeStartTimes.remove(playerId);
        BukkitRunnable task = chargeTasks.remove(playerId);
        if (task != null) {
            task.cancel();
        }
    }
    
    @EventHandler
    public void onItemHeld(PlayerItemHeldEvent event) {
        // Cancel charging if player switches items
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        
        if (chargeStartTimes.containsKey(playerId)) {
            chargeStartTimes.remove(playerId);
            BukkitRunnable task = chargeTasks.remove(playerId);
            if (task != null) {
                task.cancel();
            }
            // Play cancel sound
            player.playSound(player.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 0.3f, 1.2f);
        }
    }
}