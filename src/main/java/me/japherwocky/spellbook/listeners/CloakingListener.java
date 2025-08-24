package me.japherwocky.spellbook.listeners;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import me.japherwocky.spellbook.Spellbook;
import me.japherwocky.spellbook.SpellbookConfig;
import me.japherwocky.spellbook.enchants.CloakingEnchant;
import me.japherwocky.spellbook.enchants.ExecutionerEnchant;
import org.bukkit.Bukkit;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.UUID;

public class CloakingListener implements Listener {

    private final HashMap<UUID, Long> ticksSinceLastMovement = new HashMap<>();

    private final CloakingEnchant cloakingEnchant = (CloakingEnchant) SpellbookConfig.ENCHANTS.get(CloakingEnchant.KEY);
    private final PotionEffect cloakingEffect = new PotionEffect(PotionEffectType.INVISIBILITY,  3, 0, false, false, false);

    private final Registry<Enchantment> registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT);
    private final Enchantment cloaking = registry.get(CloakingEnchant.KEY);

    public CloakingListener() {
        if (cloaking == null) return;
        Spellbook spellbook = Spellbook.getPlugin(Spellbook.class);
        Bukkit.getGlobalRegionScheduler().runAtFixedRate(enchantio, (task) -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!player.isSneaking()) {
                    ticksSinceLastMovement.put(player.getUniqueId(), 0L);
                    continue;
                }
                int cloakingLevel = Spellbook.getSumOfEnchantLevels(player.getEquipment(), cloaking);
                if (cloakingLevel == 0) continue;
                ticksSinceLastMovement.computeIfPresent(player.getUniqueId(), (uuid, ticks) -> ticks + 1);
                if (ticksSinceLastMovement.getOrDefault(player.getUniqueId(), 0L) < cloakingEnchant.getTicksToActivate()) continue;
                player.getScheduler().execute(enchantio, () -> player.addPotionEffect(cloakingEffect), () -> {}, 1);
            }
        }, 1, 1);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        ticksSinceLastMovement.put(event.getPlayer().getUniqueId(), 0L);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        ticksSinceLastMovement.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!event.hasChangedPosition()) return;
        ticksSinceLastMovement.put(event.getPlayer().getUniqueId(), 0L);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerJump(PlayerJumpEvent event) {
        ticksSinceLastMovement.put(event.getPlayer().getUniqueId(), 0L);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerInteract(PlayerInteractEvent event) {
        ticksSinceLastMovement.put(event.getPlayer().getUniqueId(), 0L);
    }





}
