package me.youhavetrouble.enchantio;

public class EnchantioConfig {

    private final Enchantio plugin;



    protected EnchantioConfig(Enchantio plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
        plugin.reloadConfig();

    }

}
