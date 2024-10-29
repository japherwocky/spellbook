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
import java.io.IOException;
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
        configFile.createNewFile();

        FileConfiguration configuration = YamlConfiguration.loadConfiguration(configFile);

        ConfigurationSection enchantsSection = configuration.getConfigurationSection("enchants");
        if (enchantsSection == null) {
            enchantsSection = configuration.createSection("enchants");
        }

        ConfigurationSection soulboundSection = enchantsSection.getConfigurationSection("soulbound");
        if (soulboundSection == null) {
            soulboundSection = enchantsSection.createSection("soulbound");
        }

        SoulboundEnchant soulboundEnchant = new SoulboundEnchant(
                getInt(soulboundSection, "anvilCost", 1),
                getInt(soulboundSection, "weight", 10),
                EnchantmentRegistryEntry.EnchantmentCost.of(
                        getInt(soulboundSection, "minimumCost.base", 10),
                        getInt(soulboundSection, "minimumCost.additionalPerLevel", 1)
                ),
                EnchantmentRegistryEntry.EnchantmentCost.of(
                        getInt(soulboundSection, "maximumCost.base", 65),
                        getInt(soulboundSection, "maximumCost.additionalPerLevel", 1)
                ),
                getBoolean(soulboundSection, "canGetFromEnchantingTable", true),
                getTagsFromList(getStringList(
                        soulboundSection,
                        "supportedItemTags",
                        List.of(
                                "#minecraft:enchantable/armor",
                                "#minecraft:enchantable/weapon",
                                "#minecraft:enchantable/mining"
                        )
                ))
        );

        if (getBoolean(soulboundSection, "enabled", true)) {
            ENCHANTS.put(SoulboundEnchant.KEY, soulboundEnchant);
        }

        ConfigurationSection telepathySection = enchantsSection.getConfigurationSection("telepathy");
        if (telepathySection == null) {
            telepathySection = enchantsSection.createSection("telepathy");
        }

        TelepathyEnchant telepathyEnchant = new TelepathyEnchant(
                getInt(telepathySection, "anvilCost", 1),
                getInt(telepathySection, "weight", 5),
                EnchantmentRegistryEntry.EnchantmentCost.of(
                        getInt(telepathySection, "minimumCost.base", 15),
                        getInt(telepathySection, "minimumCost.additionalPerLevel", 1)
                ),
                EnchantmentRegistryEntry.EnchantmentCost.of(
                        getInt(telepathySection, "maximumCost.base", 65),
                        getInt(telepathySection, "maximumCost.additionalPerLevel", 1)
                ),
                getBoolean(telepathySection, "canGetFromEnchantingTable", true),
                getTagsFromList(getStringList(
                        telepathySection,
                        "supportedItemTags",
                        List.of(
                                "#minecraft:enchantable/mining"
                        )
                ))
        );

        if (getBoolean(telepathySection, "enabled", true)) {
            ENCHANTS.put(TelepathyEnchant.KEY, telepathyEnchant);
        }

        ConfigurationSection replantingSection = enchantsSection.getConfigurationSection("replanting");
        if (replantingSection == null) {
            replantingSection = enchantsSection.createSection("replanting");
        }

        ReplantingEnchant replantingEnchant = new ReplantingEnchant(
                getInt(replantingSection, "anvilCost", 1),
                getInt(replantingSection, "weight", 10),
                EnchantmentRegistryEntry.EnchantmentCost.of(
                        getInt(replantingSection, "minimumCost.base", 1),
                        getInt(replantingSection, "minimumCost.additionalPerLevel", 1)
                ),
                EnchantmentRegistryEntry.EnchantmentCost.of(
                        getInt(replantingSection, "maximumCost.base", 65),
                        getInt(replantingSection, "maximumCost.additionalPerLevel", 1)
                ),
                getBoolean(replantingSection, "canGetFromEnchantingTable", true),
                getTagsFromList(getStringList(
                        replantingSection,
                        "supportedItemTags",
                        List.of(
                                "#minecraft:hoes"
                        )
                ))
        );

        if (getBoolean(replantingSection, "enabled", true)) {
            ENCHANTS.put(ReplantingEnchant.KEY, replantingEnchant);
        }

        ConfigurationSection executionerSection = enchantsSection.getConfigurationSection("executioner");
        if (executionerSection == null) {
            executionerSection = enchantsSection.createSection("executioner");
        }

        ExecutionerEnchant executionerEnchant = new ExecutionerEnchant(
                getInt(executionerSection, "anvilCost", 1),
                getInt(executionerSection, "weight", 10),
                EnchantmentRegistryEntry.EnchantmentCost.of(
                        getInt(executionerSection, "minimumCost.base", 40),
                        getInt(executionerSection, "minimumCost.additionalPerLevel", 3)
                ),
                EnchantmentRegistryEntry.EnchantmentCost.of(
                        getInt(executionerSection, "maximumCost.base", 65),
                        getInt(executionerSection, "maximumCost.additionalPerLevel", 1)
                ),
                getBoolean(executionerSection, "canGetFromEnchantingTable", true),
                getTagsFromList(getStringList(
                        executionerSection,
                        "supportedItemTags",
                        List.of(
                                "#minecraft:enchantable/weapon"
                        )
                )),
                getInt(executionerSection, "maxLevel", 5),
                getDouble(executionerSection, "damageMultiplierPerLevel", 0.05),
                getDouble(executionerSection, "maxDamageHpThreshold", 0.25)
        );

        if (getBoolean(executionerSection, "enabled", true)) {
            ENCHANTS.put(ExecutionerEnchant.KEY, executionerEnchant);
        }

        ConfigurationSection beheadingSection = enchantsSection.getConfigurationSection("beheading");
        if (beheadingSection == null) {
            beheadingSection = enchantsSection.createSection("beheading");
        }

        BeheadingEnchant beheadingEnchant = new BeheadingEnchant(
                getInt(beheadingSection, "anvilCost", 1),
                getInt(beheadingSection, "weight", 10),
                EnchantmentRegistryEntry.EnchantmentCost.of(
                        getInt(beheadingSection, "minimumCost.base", 40),
                        getInt(beheadingSection, "minimumCost.additionalPerLevel", 3)
                ),
                EnchantmentRegistryEntry.EnchantmentCost.of(
                        getInt(beheadingSection, "maximumCost.base", 65),
                        getInt(beheadingSection, "maximumCost.additionalPerLevel", 1)
                ),
                getBoolean(beheadingSection, "canGetFromEnchantingTable", true),
                getTagsFromList(getStringList(
                        beheadingSection,
                        "supportedItemTags",
                        List.of(
                                "#minecraft:axes"
                        )
                )),
                getInt(beheadingSection, "maxLevel", 5),
                getDouble(beheadingSection, "chanceToDropHeadPerLevel", 0.02)
        );

        if (getBoolean(beheadingSection, "enabled", true)) {
            ENCHANTS.put(BeheadingEnchant.KEY, beheadingEnchant);
        }

        ConfigurationSection smeltingSection = enchantsSection.getConfigurationSection("smelting");
        if (smeltingSection == null) {
            smeltingSection = enchantsSection.createSection("smelting");
        }

        SmeltingEnchant smeltingEnchant = new SmeltingEnchant(
                getInt(smeltingSection, "anvilCost", 1),
                getInt(smeltingSection, "weight", 10),
                EnchantmentRegistryEntry.EnchantmentCost.of(
                        getInt(smeltingSection, "minimumCost.base", 40),
                        getInt(smeltingSection, "minimumCost.additionalPerLevel", 3)
                ),
                EnchantmentRegistryEntry.EnchantmentCost.of(
                        getInt(smeltingSection, "maximumCost.base", 65),
                        getInt(smeltingSection, "maximumCost.additionalPerLevel", 1)
                ),
                getBoolean(smeltingSection, "canGetFromEnchantingTable", true),
                getTagsFromList(getStringList(
                        smeltingSection,
                        "supportedItemTags",
                        List.of(
                                "#minecraft:enchantable/mining"
                        )
                ))
        );

        if (getBoolean(smeltingSection, "enabled", true)) {
            ENCHANTS.put(SmeltingEnchant.KEY, smeltingEnchant);
        }

        ConfigurationSection cursesSection = configuration.getConfigurationSection("curses");
        if (cursesSection == null) {
            cursesSection = configuration.createSection("curses");
        }

        ConfigurationSection panicSection = cursesSection.getConfigurationSection("panic");
        if (panicSection == null) {
            panicSection = cursesSection.createSection("panic");
        }

        PanicEnchant panicEnchant = new PanicEnchant(
                getInt(panicSection, "anvilCost", 1),
                getInt(panicSection, "weight", 2),
                EnchantmentRegistryEntry.EnchantmentCost.of(
                        getInt(panicSection, "minimumCost.base", 0),
                        getInt(panicSection, "minimumCost.additionalPerLevel", 3)
                ),
                EnchantmentRegistryEntry.EnchantmentCost.of(
                        getInt(panicSection, "maximumCost.base", 20),
                        getInt(panicSection, "maximumCost.additionalPerLevel", 1)
                ),
                getBoolean(panicSection, "canGetFromEnchantingTable", true),
                getTagsFromList(getStringList(
                        panicSection,
                        "supportedItemTags",
                        List.of(
                                "#minecraft:enchantable/armor"
                        )
                )),
                getInt(panicSection, "maxLevel", 1),
                getDouble(panicSection, "panicChancePerLevel", 0.025)
        );

        if (getBoolean(panicSection, "enabled", true)) {
            ENCHANTS.put(PanicEnchant.KEY, panicEnchant);
        }

        configuration.save(configFile);
    }

    private List<String> getStringList(ConfigurationSection section, String key, List<String> defaultValue) {
        List<String> list = section.contains(key) ? section.getStringList(key) : null;
        if (list == null) {
            section.set(key, defaultValue);
            return defaultValue;
        }
        return list;
    }

    private int getInt(ConfigurationSection section, String key, int defaultValue) {
        int value = section.contains(key) ? section.getInt(key) : -1;
        if (value == -1) {
            section.set(key, defaultValue);
            return defaultValue;
        }
        return value;
    }

    private double getDouble(ConfigurationSection section, String key, double defaultValue) {
        double value = section.contains(key) ? section.getDouble(key) : -1;
        if (value == -1) {
            section.set(key, defaultValue);
            return defaultValue;
        }
        return value;
    }

    private boolean getBoolean(ConfigurationSection section, String key, boolean defaultValue) {
        boolean value = section.contains(key) && section.getBoolean(key);
        if (!value) {
            section.set(key, defaultValue);
            return defaultValue;
        }
        return true;
    }

    private Set<TagEntry<ItemType>> getTagsFromList(List<String> tags) {
        Set<TagEntry<ItemType>> supportedItemTags = new HashSet<>();
        for (String itemTag : tags) {
            if (itemTag == null) continue;
            if (itemTag.startsWith("#")) {
                itemTag = itemTag.substring(1);
                try {
                    Key key = Key.key(itemTag);
                    TagKey<ItemType> tagKey = ItemTypeTagKeys.create(key);
                    TagEntry<ItemType> tagEntry = TagEntry.tagEntry(tagKey);
                    supportedItemTags.add(tagEntry);
                } catch (IllegalArgumentException e) {
                    logger.warning("Failed to create tag entry for " + itemTag);
                }
                continue;
            }
            try {
                Key key = Key.key(itemTag);
                TypedKey<ItemType> typedKey = TypedKey.create(RegistryKey.ITEM, key);
                TagEntry<ItemType> tagEntry = TagEntry.valueEntry(typedKey);
                supportedItemTags.add(tagEntry);
            } catch (IllegalArgumentException | NullPointerException e) {
                logger.warning("Failed to create tag entry for " + itemTag);
            }
        }
        return supportedItemTags;
    }

}
