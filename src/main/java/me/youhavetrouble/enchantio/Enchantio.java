package me.youhavetrouble.enchantio;

import me.youhavetrouble.enchantio.enchants.ExecutionerEnchant;
import me.youhavetrouble.enchantio.enchants.ReplantingEnchant;
import me.youhavetrouble.enchantio.enchants.SoulboundEnchant;
import me.youhavetrouble.enchantio.enchants.TelepathyEnchant;
import me.youhavetrouble.enchantio.listeners.ExecutionerListener;
import me.youhavetrouble.enchantio.listeners.ReplantingListener;
import me.youhavetrouble.enchantio.listeners.SoulboundListener;
import me.youhavetrouble.enchantio.listeners.TelepathyListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Enchantio extends JavaPlugin {

    @Override
    public void onEnable() {
        if (EnchantioConfig.ENCHANTS.containsKey(SoulboundEnchant.KEY)) {
            getServer().getPluginManager().registerEvents(new SoulboundListener(), this);
        }
        if (EnchantioConfig.ENCHANTS.containsKey(TelepathyEnchant.KEY)) {
            getServer().getPluginManager().registerEvents(new TelepathyListener(), this);
        }
        if (EnchantioConfig.ENCHANTS.containsKey(ReplantingEnchant.KEY)) {
            getServer().getPluginManager().registerEvents(new ReplantingListener(), this);
        }
        if (EnchantioConfig.ENCHANTS.containsKey(ExecutionerEnchant.KEY)) {
            getServer().getPluginManager().registerEvents(new ExecutionerListener(), this);
        }
    }

    @Override
    public void onDisable() {
        if (getServer().isStopping()) return;
        getLogger().severe("Enchantio is being disabled without a server shutdown. Server will be shut down to prevent issues.");
        getServer().shutdown();
    }
}
