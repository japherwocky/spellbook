package me.japherwocky.spellbook.listeners;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import me.japherwocky.spellbook.Spellbook;
import me.japherwocky.spellbook.SpellbookConfig;
import me.japherwocky.spellbook.enchants.SpellbookEnchant;
import me.japherwocky.spellbook.enchants.PanicEnchant;
import me.japherwocky.spellbook.events.PlayerPanicEvent;
import org.bukkit.Bukkit;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EntityEquipment;
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

        SpellbookEnchant enchant = SpellbookConfig.ENCHANTS.get(PanicEnchant.KEY);
        if (!(enchant instanceof PanicEnchant panicEnchant)) return;

        EntityEquipment equipment = player.getEquipment();

        int level = Spellbook.getHighestEnchantLevel(equipment, panic);
        if (level == 0) return;
        double chance = level * panicEnchant.getPanicChancePerLevel();

        if (ThreadLocalRandom.current().nextDouble() > chance) return;

        PlayerInventory inventory = player.getInventory();
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
