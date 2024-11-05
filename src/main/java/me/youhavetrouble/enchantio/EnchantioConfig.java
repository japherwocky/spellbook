package me.youhavetrouble.enchantio;

import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.tag.TagKey;
import io.papermc.paper.tag.TagEntry;
import me.youhavetrouble.enchantio.enchants.*;
import net.kyori.adventure.key.Key;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

@SuppressWarnings("UnstableApiUsage")
public class EnchantioConfig {

    public static final Map<Key, EnchantioEnchant> ENCHANTS = new HashMap<>();
    private static boolean initialized = false;

    protected static void init(Path filePath) throws IOException {
        if (initialized) {
            return;
        }
        initialized = true;
        File file = filePath.toFile();
        if (!file.exists()) {
            file.mkdirs();
        }

        File configFile = new File(filePath.toFile(), "config.yml");
        configFile.createNewFile();

        FileConfiguration configuration = YamlConfiguration.loadConfiguration(configFile);

        ConfigurationSection enchantsSection = getConfigSection(configuration, "enchants");

        ConfigurationSection soulboundSection = getConfigSection(enchantsSection, "soulbound");
        SoulboundEnchant.create(soulboundSection);

        ConfigurationSection telepathySection = getConfigSection(enchantsSection, "telepathy");
        TelepathyEnchant.create(telepathySection);

        ConfigurationSection replantingSection = getConfigSection(enchantsSection, "replanting");
        ReplantingEnchant.create(replantingSection);

        ConfigurationSection executionerSection = getConfigSection(enchantsSection, "executioner");
        ExecutionerEnchant.create(executionerSection);

        ConfigurationSection beheadingSection = getConfigSection(enchantsSection, "beheading");
        BeheadingEnchant.create(beheadingSection);

        ConfigurationSection smeltingSection = getConfigSection(enchantsSection, "smelting");
        SmeltingEnchant.create(smeltingSection);

        ConfigurationSection airbagSection = getConfigSection(enchantsSection, "airbag");
        AirbagEnchant.create(airbagSection);

        ConfigurationSection homecomingSection = getConfigSection(enchantsSection, "homecoming");
        HomecomingEnchant.create(homecomingSection);

        ConfigurationSection cloakingSection = getConfigSection(enchantsSection, "cloaking");
        CloakingEnchant.create(cloakingSection);

        ConfigurationSection cursesSection = getConfigSection(configuration, "curses");

        ConfigurationSection panicSection = getConfigSection(cursesSection, "panic");
        PanicEnchant.create(panicSection);

        ConfigurationSection vampirismSection = getConfigSection(enchantsSection, "vampirism");
        VampirismEnchant.create(vampirismSection);

        ConfigurationSection insomniaSection = getConfigSection(cursesSection, "insomnia");
        InsomniaEnchant.create(insomniaSection);

        configuration.save(configFile);
    }

    public static List<String> getStringList(ConfigurationSection section, String key, List<String> defaultValue) {
        List<String> list = section.contains(key) ? section.getStringList(key) : null;
        if (list == null) {
            section.set(key, defaultValue);
            return defaultValue;
        }
        return list;
    }

    public static int getInt(ConfigurationSection section, String key, int defaultValue) {
        int value = section.contains(key) ? section.getInt(key) : -1;
        if (value == -1) {
            section.set(key, defaultValue);
            return defaultValue;
        }
        return value;
    }

    public static double getDouble(ConfigurationSection section, String key, double defaultValue) {
        double value = section.contains(key) ? section.getDouble(key) : -1;
        if (value == -1) {
            section.set(key, defaultValue);
            return defaultValue;
        }
        return value;
    }

    public static boolean getBoolean(ConfigurationSection section, String key, boolean defaultValue) {
        boolean value = section.contains(key) && section.getBoolean(key);
        if (!value) {
            section.set(key, defaultValue);
            return defaultValue;
        }
        return true;
    }

    public static Set<EquipmentSlotGroup> getEquipmentSlotGroups(@NotNull List<String> slots) {
        Set<EquipmentSlotGroup> equipmentSlotGroups = new HashSet<>();
        for (String slot : slots) {
            EquipmentSlotGroup equipmentSlotGroup = EquipmentSlotGroup.getByName(slot);
            if (equipmentSlotGroup == null) {
                continue;
            }
            equipmentSlotGroups.add(equipmentSlotGroup);
        }
        return equipmentSlotGroups;
    }

    public static Set<TagEntry<ItemType>> getTagsFromList(@NotNull List<String> tags) {
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
                }
                continue;
            }
            try {
                Key key = Key.key(itemTag);
                TypedKey<ItemType> typedKey = TypedKey.create(RegistryKey.ITEM, key);
                TagEntry<ItemType> tagEntry = TagEntry.valueEntry(typedKey);
                supportedItemTags.add(tagEntry);
            } catch (IllegalArgumentException | NullPointerException e) {
            }
        }
        return supportedItemTags;
    }

    public static ConfigurationSection getConfigSection(ConfigurationSection section, String key) {
        ConfigurationSection value = section.getConfigurationSection(key);
        if (value == null) {
            value = section.createSection(key);
        }
        return value;
    }

}
