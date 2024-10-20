package me.youhavetrouble.enchantio;

import me.youhavetrouble.enchantio.listeners.ReplantingListener;
import me.youhavetrouble.enchantio.listeners.SoulboundListener;
import me.youhavetrouble.enchantio.listeners.TelepathyListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Enchantio extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new SoulboundListener(), this);
        getServer().getPluginManager().registerEvents(new TelepathyListener(), this);
        getServer().getPluginManager().registerEvents(new ReplantingListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
