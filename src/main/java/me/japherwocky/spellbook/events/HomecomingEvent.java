package me.japherwocky.spellbook.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class HomecomingEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private boolean cancelled = false;
    private Location location;

    public HomecomingEvent(@NotNull Player who, @NotNull Location location) {
        super(who);
        this.location = location;
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

    /**
     * Get the location the player is being teleported to.
     * @return the location the player is being teleported to
     */
    public @NotNull Location getLocation() {
        return location;
    }

    /**
     * Set the location the player is being teleported to.
     * @param location the location the player is being teleported to
     */
    public void setLocation(@NotNull Location location) {
        this.location = location;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
