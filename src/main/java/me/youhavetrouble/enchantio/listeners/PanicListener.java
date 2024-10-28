package me.youhavetrouble.enchantio.listeners;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import me.youhavetrouble.enchantio.EnchantioConfig;
import me.youhavetrouble.enchantio.enchants.BeheadingEnchant;
import me.youhavetrouble.enchantio.enchants.EnchantioEnchant;
import me.youhavetrouble.enchantio.enchants.PanicEnchant;
import me.youhavetrouble.enchantio.events.PlayerPanicEvent;
import org.bukkit.Bukkit;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class PanicListener implements Listener {

    private final Registry<Enchantment> registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT);
    private final Enchantment panic = registry.get(PanicEnchant.KEY);

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onPlayerPanic(EntityDamageEvent event) {
        if (panic == null) return;

        if (!(event.getEntity() instanceof Player player)) return;

        EnchantioEnchant enchant = EnchantioConfig.ENCHANTS.get(PanicEnchant.KEY);
        if (!(enchant instanceof PanicEnchant panicEnchant)) return;

        PlayerInventory inventory = player.getInventory();
        ItemStack highestPanicEnchantItem = null;

        for (ItemStack item : inventory.getArmorContents()) {
            if (item == null) continue;
            if (item.containsEnchantment(panic)) {
                if (highestPanicEnchantItem == null) {
                    highestPanicEnchantItem = item;
                } else if (item.getEnchantmentLevel(panic) > highestPanicEnchantItem.getEnchantmentLevel(panic)) {
                    highestPanicEnchantItem = item;
                }
            }
        }

        if (highestPanicEnchantItem == null) return;

        double chance = highestPanicEnchantItem.getEnchantmentLevel(panic) * panicEnchant.getPanicChancePerLevel();

        if (ThreadLocalRandom.current().nextDouble() > chance) return;

        List<ItemStack> hotbarItems = new ArrayList<>(Arrays.stream(inventory.getContents()).toList().subList(0, 9));
        Collections.shuffle(hotbarItems, ThreadLocalRandom.current());

        PlayerPanicEvent playerPanicEvent = new PlayerPanicEvent(player, hotbarItems);
        Bukkit.getPluginManager().callEvent(playerPanicEvent);
        if (playerPanicEvent.isCancelled()) return;

        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, playerPanicEvent.getScrambledItems().get(i));
        }

    }

}
