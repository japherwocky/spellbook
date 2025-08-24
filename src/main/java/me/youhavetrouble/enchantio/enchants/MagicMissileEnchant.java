package me.youhavetrouble.enchantio.enchants;

import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.tag.TagKey;
import io.papermc.paper.tag.TagEntry;
import me.youhavetrouble.enchantio.EnchantioConfig;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static me.youhavetrouble.enchantio.EnchantioConfig.ENCHANTS;

@SuppressWarnings("UnstableApiUsage")
public class MagicMissileEnchant implements EnchantioEnchant {

    public static final Key KEY = Key.key("enchantio:magic_missile");

    private final int anvilCost, weight;
    private final EnchantmentRegistryEntry.EnchantmentCost minimumCost;
    private final EnchantmentRegistryEntry.EnchantmentCost maximumCost;
    private final Set<TagEntry<ItemType>> supportedItemTags = new HashSet<>();
    private final Set<TagKey<Enchantment>> enchantTagKeys = new HashSet<>();

    public MagicMissileEnchant(
            int anvilCost,
            int weight,
            EnchantmentRegistryEntry.EnchantmentCost minimumCost,
            EnchantmentRegistryEntry.EnchantmentCost maximumCost,
            Collection<TagKey<Enchantment>> enchantTagKeys,
            Collection<TagEntry<ItemType>> supportedItemTags
    ) {
        this.anvilCost = anvilCost;
        this.weight = weight;
        this.minimumCost = minimumCost;
        this.maximumCost = maximumCost;
        this.supportedItemTags.addAll(supportedItemTags);
        this.enchantTagKeys.addAll(enchantTagKeys);
    }

    @Override
    public @NotNull Key getKey() {
        return KEY;
    }

    @Override
    public @NotNull Component getDescription() {
        return Component.translatable("enchantio.enchant.magic_missile", "Magic Missile");
    }

    @Override
    public int getAnvilCost() {
        return anvilCost;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public EnchantmentRegistryEntry.@NotNull EnchantmentCost getMinimumCost() {
        return minimumCost;
    }

    @Override
    public EnchantmentRegistryEntry.@NotNull EnchantmentCost getMaximumCost() {
        return maximumCost;
    }

    @Override
    public @NotNull Set<EquipmentSlotGroup> getActiveSlotGroups() {
        return Set.of(EquipmentSlotGroup.MAINHAND);
    }

    @Override
    public @NotNull Set<TagEntry<ItemType>> getSupportedItems() {
        return supportedItemTags;
    }

    @Override
    public @NotNull Set<TagKey<Enchantment>> getEnchantTagKeys() {
        return Collections.unmodifiableSet(enchantTagKeys);
    }

    /**
     * Finds the nearest living entity in the direction the player is looking.
     * 
     * @param player The player casting the magic missile
     * @param range The maximum range to search for targets
     * @return The nearest living entity, or null if none found
     */
    public static LivingEntity findTarget(Player player, double range) {
        Vector direction = player.getEyeLocation().getDirection();
        Location eyeLocation = player.getEyeLocation();
        
        LivingEntity target = null;
        double closestDistance = Double.MAX_VALUE;
        
        // Get all entities within range
        for (Entity entity : player.getNearbyEntities(range, range, range)) {
            if (!(entity instanceof LivingEntity) || entity == player) continue;
            
            // Calculate vector from player to entity
            Vector toEntity = entity.getLocation().toVector().subtract(eyeLocation.toVector());
            double distance = toEntity.length();
            
            // Normalize the vector
            toEntity.normalize();
            
            // Calculate the dot product to determine if entity is in front of player
            double dot = toEntity.dot(direction);
            
            // If dot product is positive (entity is in front) and angle is small enough
            if (dot > 0.8 && distance < closestDistance) { // 0.8 is roughly a 36-degree cone
                target = (LivingEntity) entity;
                closestDistance = distance;
            }
        }
        
        return target;
    }

    public static MagicMissileEnchant create(ConfigurationSection configurationSection) {
        MagicMissileEnchant magicMissileEnchant = new MagicMissileEnchant(
                EnchantioConfig.getInt(configurationSection, "anvilCost", 1),
                EnchantioConfig.getInt(configurationSection, "weight", 10),
                EnchantmentRegistryEntry.EnchantmentCost.of(
                        EnchantioConfig.getInt(configurationSection, "minimumCost.base", 10),
                        EnchantioConfig.getInt(configurationSection, "minimumCost.additionalPerLevel", 10)
                ),
                EnchantmentRegistryEntry.EnchantmentCost.of(
                        EnchantioConfig.getInt(configurationSection, "maximumCost.base", 65),
                        EnchantioConfig.getInt(configurationSection, "maximumCost.additionalPerLevel", 10)
                ),
                EnchantioConfig.getEnchantmentTagKeysFromList(EnchantioConfig.getStringList(
                        configurationSection,
                        "enchantmentTags",
                        List.of("#in_enchanting_table")
                )),
                EnchantioConfig.getItemTagEntriesFromList(EnchantioConfig.getStringList(
                        configurationSection,
                        "supportedItemTags",
                        List.of(
                                "minecraft:wooden_sword",
                                "minecraft:stone_sword",
                                "minecraft:iron_sword",
                                "minecraft:golden_sword",
                                "minecraft:diamond_sword",
                                "minecraft:netherite_sword"
                        )
                ))
        );

        if (EnchantioConfig.getBoolean(configurationSection, "enabled", true)) {
            ENCHANTS.put(MagicMissileEnchant.KEY, magicMissileEnchant);
        }

        return magicMissileEnchant;
    }
}
