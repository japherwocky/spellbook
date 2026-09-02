package me.japherwocky.spellbook.listeners;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import me.japherwocky.spellbook.enchants.VeinMinerEnchant;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import java.util.*;

@SuppressWarnings("UnstableApiUsage")
public class VeinMinerListener implements Listener {

    private final Registry<Enchantment> enchantmentRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT);
    private final Enchantment veinMiner = enchantmentRegistry.get(VeinMinerEnchant.KEY);

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent event) {
        if (veinMiner == null) return;

        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();
        int enchantLevel = tool.getEnchantmentLevel(veinMiner);

        if (enchantLevel == 0) return;

        Block sourceBlock = event.getBlock();
        Material sourceMaterial = sourceBlock.getType();

        // Check if the block type is whitelisted (if whitelist is enabled)
        if (VeinMinerEnchant.USE_WHITELIST && !VeinMinerEnchant.WHITELISTED_BLOCKS.contains(sourceMaterial)) return;

        // Check if the tool can actually mine this block
        if (VeinMinerEnchant.REQUIRE_CORRECT_TOOL) {
            if (tool.isEmpty() || tool.getType().isAir()) return;
            if (sourceBlock.getDrops(tool).isEmpty()) return;
        }

        // Calculate max blocks based on enchantment level
        int maxBlocks = VeinMinerEnchant.MAX_BLOCKS_BASE + (VeinMinerEnchant.MAX_BLOCKS_PER_LEVEL * (enchantLevel - 1));

        // Find all connected blocks using BFS
        Set<Block> blocksToBreak = findConnectedBlocks(sourceBlock, maxBlocks);

        // Remove the source block from the set (it's already being broken)
        blocksToBreak.remove(sourceBlock);

        if (blocksToBreak.isEmpty()) return;

        // Process each additional block
        int blocksBroken = 0;
        boolean survivalPlayer = player.getGameMode() != GameMode.CREATIVE;
        for (Block block : blocksToBreak) {
            // Break the block and handle drops
            breakBlock(player, block, tool);

            // Drain durability (survival only — vanilla does not damage tools in creative)
            if (survivalPlayer && VeinMinerEnchant.RESPECT_DURABILITY) {
                if (tool.getItemMeta() instanceof Damageable damageable
                        && damageable.getDamage() + 1 >= tool.getType().getMaxDurability()) {
                    // Tool is about to break, stop vein mining
                    break;
                }
                // Damage through the vanilla path (Item#damage with an entity) so Unbreaking
                // applies and PlayerItemDamageEvent fires — which also keeps the
                // unbreakableNetherite feature effective for vein-mined blocks.
                tool.damage(1, player);
            }

            // Drain hunger (survival only)
            if (survivalPlayer && VeinMinerEnchant.RESPECT_HUNGER) {
                player.setFoodLevel(Math.max(0, player.getFoodLevel() - VeinMinerEnchant.HUNGER_COST_PER_BLOCK));
            }

            blocksBroken++;
        }

        // Write the mutated tool stack back to the inventory (no-op if the stack is a mirror)
        if (survivalPlayer && VeinMinerEnchant.RESPECT_DURABILITY && blocksBroken > 0) {
            player.getInventory().setItemInMainHand(tool);
        }

        // Play sound and particles for feedback
        if (blocksBroken > 0) {
            player.playSound(sourceBlock.getLocation(), Sound.BLOCK_STONE_BREAK, SoundCategory.BLOCKS, 1.0f, 1.0f);
        }
    }

    /**
     * Finds all connected blocks of the same type using BFS.
     * @param sourceBlock The starting block
     * @param maxBlocks Maximum number of blocks to find
     * @return Set of connected blocks (including source)
     */
    private Set<Block> findConnectedBlocks(Block sourceBlock, int maxBlocks) {
        Set<Block> result = new HashSet<>();
        Queue<Block> queue = new LinkedList<>();
        Material sourceMaterial = sourceBlock.getType();

        queue.add(sourceBlock);
        result.add(sourceBlock);

        while (!queue.isEmpty() && result.size() < maxBlocks) {
            Block current = queue.poll();

            // Search in a cube around the current block
            for (int x = -VeinMinerEnchant.SEARCH_RADIUS; x <= VeinMinerEnchant.SEARCH_RADIUS; x++) {
                for (int y = -VeinMinerEnchant.SEARCH_RADIUS; y <= VeinMinerEnchant.SEARCH_RADIUS; y++) {
                    for (int z = -VeinMinerEnchant.SEARCH_RADIUS; z <= VeinMinerEnchant.SEARCH_RADIUS; z++) {
                        if (x == 0 && y == 0 && z == 0) continue;

                        Block neighbor = current.getRelative(x, y, z);

                        if (result.contains(neighbor)) continue;
                        if (neighbor.getType() != sourceMaterial) continue;

                        result.add(neighbor);
                        queue.add(neighbor);

                        if (result.size() >= maxBlocks) break;
                    }
                    if (result.size() >= maxBlocks) break;
                }
                if (result.size() >= maxBlocks) break;
            }
        }

        return result;
    }

    /**
     * Breaks a block and drops its items, respecting Fortune and Silk Touch.
     * @param player The player breaking the block
     * @param block The block to break
     * @param tool The tool used to break the block
     */
    private void breakBlock(Player player, Block block, ItemStack tool) {
        // Get drops with tool context (respects Fortune, Silk Touch)
        Collection<ItemStack> drops = block.getDrops(tool, player);

        // Spawn drops at block location
        Location dropLocation = block.getLocation().add(0.5, 0.5, 0.5);
        for (ItemStack drop : drops) {
            Item item = block.getWorld().dropItem(dropLocation, drop);
            item.setVelocity(item.getVelocity().multiply(0.1)); // Reduce velocity for cleaner drops
        }

        // Play break effect
        block.getWorld().playSound(block.getLocation(), block.getBlockSoundGroup().getBreakSound(), SoundCategory.BLOCKS, 0.5f, 1.0f);
        block.getWorld().spawnParticle(Particle.BLOCK, dropLocation, 10, block.getBlockData());

        // Set block to air
        block.setType(Material.AIR);
    }
}