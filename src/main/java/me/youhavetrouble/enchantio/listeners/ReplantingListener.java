package me.youhavetrouble.enchantio.listeners;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import me.youhavetrouble.enchantio.enchants.ReplantingEnchant;
import org.bukkit.*;
import org.bukkit.block.BlockState;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class ReplantingListener implements Listener {

    private final Registry<Enchantment> enchantmentRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT);
    private final Tag<Material> cropTag = Tag.CROPS;
    private final Enchantment replanting = enchantmentRegistry.get(ReplantingEnchant.KEY);

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onReplantingTool(BlockDropItemEvent event) {
        if (replanting == null) return;

        ItemStack tool = event.getPlayer().getInventory().getItemInMainHand();
        if (!tool.containsEnchantment(replanting)) return;

        BlockState block = event.getBlockState();

        if (cropTag == null) return;
        if (!cropTag.isTagged(block.getType())) return;

        Material placementMaterial = block.getBlockData().getPlacementMaterial();

        Player player = event.getPlayer();
        PlayerInventory inventory = player.getInventory();

        // If the player is in creative mode, skip the inventory check
        boolean shouldReplant = player.getGameMode().equals(GameMode.CREATIVE);

        if (!shouldReplant) {
            // try to remove seed from the player's inventory
            for (ItemStack item : inventory.getContents()) {
                if (item == null) continue;
                if (item.getType() == placementMaterial) {
                    item.setAmount(item.getAmount() - 1);
                    shouldReplant = true;
                    break;
                }
            }
        }

        if (!shouldReplant) {
            // Try to remove the seed from the items dropped by the block
            for (Item item : event.getItems()) {
                ItemStack itemStack = item.getItemStack();
                if (itemStack.getType().equals(placementMaterial)) {
                    itemStack.setAmount(itemStack.getAmount() - 1);
                    shouldReplant = true;
                    break;
                }
            }
        }

        if (!shouldReplant) return;

        // Replant the crop
        event.getBlock().setType(block.getType());
    }

}
