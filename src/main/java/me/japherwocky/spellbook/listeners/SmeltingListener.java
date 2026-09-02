package me.japherwocky.spellbook.listeners;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import me.japherwocky.spellbook.enchants.SmeltingEnchant;
import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.bukkit.Registry;
import org.bukkit.block.BlockState;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SmeltingListener implements Listener {

    private final Registry<Enchantment> registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT);
    private final Enchantment smelting = registry.get(SmeltingEnchant.KEY);
    private final Enchantment silkTouch = registry.get(Key.key("minecraft:silk_touch"));
    private final Map<ItemStack, ItemStack> smeltingCache = new HashMap<>();

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onSmeltingEnchantSmelt(BlockDropItemEvent event) {
        if (smelting == null) return;
        ItemStack tool = event.getPlayer().getInventory().getItemInMainHand();
        if (!tool.containsEnchantment(smelting)) return;
        // Silk Touch wins: converting silk-touched ore drops to ingots would destroy
        // the silk touch reward. Vanilla's exclusive-set system cannot fully enforce
        // this (a plugin cannot modify vanilla Silk Touch's exclusive set symmetrically),
        // so it is enforced here at runtime instead.
        if (silkTouch != null && tool.containsEnchantment(silkTouch)) return;

        BlockState block = event.getBlockState();
        if (block instanceof BlockInventoryHolder) return;

        for (Item item : event.getItems()) {
            int amount = item.getItemStack().getAmount();
            ItemStack smeltedItem = getSmeltedItem(item.getItemStack());
            if (smeltedItem == null) continue;
            item.setItemStack(smeltedItem.asQuantity(amount));
        }
    }

    /**
     * Gets the smelted result for the given item stack, or null if it is not smeltable.
     * Both positive and negative lookups are cached to avoid re-iterating recipes.
     */
    private ItemStack getSmeltedItem(@NotNull ItemStack itemStack) {
        ItemStack singleItem = itemStack.asOne();
        if (smeltingCache.containsKey(singleItem)) return smeltingCache.get(singleItem);

        ItemStack result = null;
        for (@NotNull Iterator<Recipe> it = Bukkit.recipeIterator(); it.hasNext(); ) {
            Recipe recipe = it.next();
            if (!(recipe instanceof FurnaceRecipe furnaceRecipe)) continue;
            if (!furnaceRecipe.getInputChoice().test(singleItem)) continue;
            result = furnaceRecipe.getResult();
            break;
        }
        smeltingCache.put(singleItem, result);
        return result;
    }

}
