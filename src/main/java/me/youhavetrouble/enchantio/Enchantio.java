package me.youhavetrouble.enchantio;


import me.youhavetrouble.enchantio.listeners.SoulboundListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Enchantio extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new SoulboundListener(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
