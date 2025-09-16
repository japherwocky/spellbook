package me.japherwocky.spellbook.listeners;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;

import me.japherwocky.spellbook.SpellbookConfig;
import me.japherwocky.spellbook.enchants.TelekinesisEnchant;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class TelekinesisListener implements Listener {

    private final Registry<@NotNull Enchantment> registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT);
    private final Enchantment telekinesis = registry.get(TelekinesisEnchant.KEY);

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onTelekinesisTool(BlockDropItemEvent event) {
        if (telekinesis == null) return;
        if (!(SpellbookConfig.ENCHANTS.get(TelekinesisEnchant.KEY) instanceof TelekinesisEnchant telekinesisEnchant)) return;
        ItemStack tool = event.getPlayer().getInventory().getItemInMainHand();
        if (!tool.containsEnchantment(telekinesis)) return;
        for (Item item : event.getItems()) {
            item.teleport(event.getPlayer(), PlayerTeleportEvent.TeleportCause.PLUGIN);
            item.setPickupDelay(0);
            if (!telekinesisEnchant.isOnlyUserCanPickupItems()) continue;
            item.setOwner(event.getPlayer().getUniqueId());
        }
    }

}
