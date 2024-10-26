package me.youhavetrouble.enchantio.events;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Called when an entity is beheaded by a weapon with the Beheading enchantment.
 */
public class EntityBeheadEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private ItemStack headToDrop;
    private boolean cancelled = false;

    public EntityBeheadEvent(@NotNull Entity entity, @NotNull ItemStack headToDrop) {
        super(entity);
        this.headToDrop = headToDrop;
    }

    public @NotNull ItemStack getHeadToDrop() {
        return headToDrop;
    }

    public void setHeadToDrop(@NotNull ItemStack headToDrop) {
        this.headToDrop = headToDrop;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
