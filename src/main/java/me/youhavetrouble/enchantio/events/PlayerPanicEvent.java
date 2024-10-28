package me.youhavetrouble.enchantio.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * Called when a player with the Panic enchantment takes damage and their hotbar is scrambled.
 */
public class PlayerPanicEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private boolean cancelled = false;
    private final List<ItemStack> scrambledItems;

    public PlayerPanicEvent(@NotNull Player player, @NotNull List<ItemStack> scrambledItems) {
        super(player);
        this.scrambledItems = scrambledItems;
    }

    /**
     * Get the hotbar items that will be set for the player. List order will be preserved.
     * @return The unmodifiable list of hotbar items
     */
    public List<ItemStack> getScrambledItems() {
        return Collections.unmodifiableList(scrambledItems);
    }

    /**
     * Set the new player hotbar items. List order will be preserved. List must have a size of 9.
     * @param scrambledItems The new hotbar items
     */
    public void setScrambledItems(@NotNull List<ItemStack> scrambledItems) {
        if (scrambledItems.size() != 9) {
            throw new IllegalArgumentException("Scrambled items must have a size of 9");
        }
        this.scrambledItems.clear();
        this.scrambledItems.addAll(scrambledItems);
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
