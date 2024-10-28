package me.youhavetrouble.enchantio;

import me.youhavetrouble.enchantio.enchants.*;
import me.youhavetrouble.enchantio.listeners.*;
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
        if (EnchantioConfig.ENCHANTS.containsKey(BeheadingEnchant.KEY)) {
            getServer().getPluginManager().registerEvents(new BeheadingListener(), this);
        }
        if (EnchantioConfig.ENCHANTS.containsKey(SmeltingEnchant.KEY)) {
            getServer().getPluginManager().registerEvents(new SmeltingListener(), this);
        }
        if (EnchantioConfig.ENCHANTS.containsKey(PanicEnchant.KEY)) {
            getServer().getPluginManager().registerEvents(new PanicListener(), this);
        }
    }

    @Override
    public void onDisable() {
        if (getServer().isStopping()) return;
        getLogger().severe("Enchantio is being disabled without a server shutdown. Server will be shut down to prevent issues.");
        getServer().shutdown();
    }
}
