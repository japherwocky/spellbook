package me.youhavetrouble.enchantio;

import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.tag.TagKey;
import io.papermc.paper.tag.TagEntry;
import me.youhavetrouble.enchantio.enchants.*;
import net.kyori.adventure.key.Key;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.*;
import java.util.logging.Logger;

@SuppressWarnings("UnstableApiUsage")
public class EnchantioConfig {

    public static final Map<Key, EnchantioEnchant> ENCHANTS = new HashMap<>();
    private final Logger logger;

    protected EnchantioConfig(Path filePath, Logger logger) throws IOException {
        this.logger = logger;

        File file = filePath.toFile();
        if (!file.exists()) {
            file.mkdirs();
        }

        File configFile = new File(filePath.toFile(), "config.yml");
        if (!configFile.exists()) {
            try (InputStream in = getClass().getResourceAsStream("/config.yml")) {
                if (in == null) {
                    throw new IOException("Failed to load config.yml from resources");
                }
                try (FileOutputStream out = new FileOutputStream(configFile)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                }
            } catch (IOException e) {
                throw new IOException("Failed saving default config", e);
            }
        }

        FileConfiguration configuration = YamlConfiguration.loadConfiguration(configFile);

        ConfigurationSection enchantsSection = configuration.getConfigurationSection("enchants");
        if (enchantsSection == null) {
            throw new IOException("Failed to load enchants section from config");
        }

        ConfigurationSection soulboundSection = enchantsSection.getConfigurationSection("soulbound");
        if (soulboundSection == null) {
            soulboundSection = enchantsSection.createSection("soulbound");
        }
        ENCHANTS.put(SoulboundEnchant.KEY, new SoulboundEnchant(
                soulboundSection.getInt("anvilCost", 1),
                soulboundSection.getInt("weight", 10),
                EnchantmentRegistryEntry.EnchantmentCost.of(
                        soulboundSection.getInt("minimumCost.base", 10),
                        soulboundSection.getInt("minimumCost.additionalPerLevel", 1)
                ),
                EnchantmentRegistryEntry.EnchantmentCost.of(
                        soulboundSection.getInt("maximumCost.base", 65),
                        soulboundSection.getInt("maximumCost.additionalPerLevel", 1)
                ),
                soulboundSection.getBoolean("canGetFromEnchantingTable", true),
                getTagsFromList(getStringList(
                        soulboundSection,
                        "supportedItemTags",
                        List.of(
                                "#minecraft:enchantable/armor",
                                "#minecraft:enchantable/weapon",
                                "#minecraft:enchantable/mining"
                        )
                ))
        ));

        ConfigurationSection telepathySection = enchantsSection.getConfigurationSection("telepathy");
        if (telepathySection == null) {
            telepathySection = enchantsSection.createSection("telepathy");
        }
        ENCHANTS.put(TelepathyEnchant.KEY, new TelepathyEnchant(
                telepathySection.getInt("anvilCost", 1),
                telepathySection.getInt("weight", 5),
                EnchantmentRegistryEntry.EnchantmentCost.of(
                        telepathySection.getInt("minimumCost.base", 15),
                        telepathySection.getInt("minimumCost.additionalPerLevel", 1)
                ),
                EnchantmentRegistryEntry.EnchantmentCost.of(
                        telepathySection.getInt("maximumCost.base", 65),
                        telepathySection.getInt("maximumCost.additionalPerLevel", 1)
                ),
                telepathySection.getBoolean("canGetFromEnchantingTable", true),
                getTagsFromList(getStringList(
                        telepathySection,
                        "supportedItemTags",
                        List.of(
                                "#minecraft:enchantable/mining"
                        )
                ))
        ));

        ConfigurationSection replantingSection = enchantsSection.getConfigurationSection("replanting");
        if (replantingSection == null) {
            replantingSection = enchantsSection.createSection("replanting");
        }
        ENCHANTS.put(ReplantingEnchant.KEY, new ReplantingEnchant(
                replantingSection.getInt("anvilCost", 1),
                replantingSection.getInt("weight", 10),
                EnchantmentRegistryEntry.EnchantmentCost.of(
                        replantingSection.getInt("minimumCost.base", 1),
                        replantingSection.getInt("minimumCost.additionalPerLevel", 1)
                ),
                EnchantmentRegistryEntry.EnchantmentCost.of(
                        replantingSection.getInt("maximumCost.base", 65),
                        replantingSection.getInt("maximumCost.additionalPerLevel", 1)
                ),
                replantingSection.getBoolean("canGetFromEnchantingTable", true),
                getTagsFromList(getStringList(
                        replantingSection,
                        "supportedItemTags",
                        List.of(
                                "#minecraft:hoes"
                        )
                ))
        ));

    }

    private List<String> getStringList(ConfigurationSection section, String key, List<String> defaultValue) {
        List<String> list = section.contains(key) ? section.getStringList(key) : null;
        if (list == null) return defaultValue;
        return list;
    }

    private Set<TagEntry<ItemType>> getTagsFromList(List<String> tags) {
        Set<TagEntry<ItemType>> supportedItemTags = new HashSet<>();
        for (String itemTag : tags) {
            if (itemTag == null) continue;
            if (itemTag.startsWith("#")) {
                itemTag = itemTag.substring(1);
            } else {
                logger.warning("Only item tags are supported for now, item tags need to begin with #");
                continue;
            }
            try {
                Key key = Key.key(itemTag);
                TagKey<ItemType> tagKey = ItemTypeTagKeys.create(key);
                TagEntry<ItemType> tagEntry = TagEntry.tagEntry(tagKey);
                supportedItemTags.add(tagEntry);
            } catch (IllegalArgumentException e) {
                logger.warning("Failed to create tag entry for " + itemTag);
            }
        }
        return supportedItemTags;
    }

}
