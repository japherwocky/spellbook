package me.youhavetrouble.enchantio.enchants;

import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.tag.TagKey;
import io.papermc.paper.tag.TagEntry;
import me.youhavetrouble.enchantio.EnchantioConfig;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static me.youhavetrouble.enchantio.EnchantioConfig.ENCHANTS;

@SuppressWarnings("UnstableApiUsage")
public class FireballEnchant implements EnchantioEnchant {

    public static final Key KEY = Key.key("enchantio:fireball");

    private final int anvilCost, weight;
    private final EnchantmentRegistryEntry.EnchantmentCost minimumCost;
    private final EnchantmentRegistryEntry.EnchantmentCost maximumCost;
    private final Set<TagEntry<ItemType>> supportedItemTags = new HashSet<>();
    private final Set<TagKey<Enchantment>> enchantTagKeys = new HashSet<>();

    public FireballEnchant(
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
        return Component.translatable("enchantio.enchant.fireball", "Fireball");
    }

    @Override
    public int getAnvilCost() {
        return anvilCost;
    }

    @Override
    public int getMaxLevel() {
        return 2;
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
     * Creates a fireball entity at the location of the given entity, with the direction of the entity's view.
     * 
     * @param entity The entity that is shooting the fireball
     * @param power The power of the fireball (affects explosion size)
     * @return The created fireball entity
     */
    public static Fireball createFireball(LivingEntity entity, float power) {
        Vector direction = entity.getEyeLocation().getDirection();
        
        // Create the fireball
        Fireball fireball = entity.getWorld().spawn(
                entity.getEyeLocation().add(direction.clone().multiply(1.5)), // Spawn in front of the player
                Fireball.class
        );
        
        // Set properties
        fireball.setShooter(entity);
        fireball.setYield(power); // Explosion power
        fireball.setIsIncendiary(true); // Set fire
        fireball.setVelocity(direction.multiply(0.5)); // Set velocity instead of direction
        
        return fireball;
    }

    public static FireballEnchant create(ConfigurationSection configurationSection) {
        FireballEnchant fireballEnchant = new FireballEnchant(
                EnchantioConfig.getInt(configurationSection, "anvilCost", 1),
                EnchantioConfig.getInt(configurationSection, "weight", 10),
                EnchantmentRegistryEntry.EnchantmentCost.of(
                        EnchantioConfig.getInt(configurationSection, "minimumCost.base", 10),
                        EnchantioConfig.getInt(configurationSection, "minimumCost.additionalPerLevel", 13)
                ),
                EnchantmentRegistryEntry.EnchantmentCost.of(
                        EnchantioConfig.getInt(configurationSection, "maximumCost.base", 65),
                        EnchantioConfig.getInt(configurationSection, "maximumCost.additionalPerLevel", 13)
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
                                "#minecraft:enchantable/weapon"
                        )
                ))
        );

        if (EnchantioConfig.getBoolean(configurationSection, "enabled", true)) {
            ENCHANTS.put(FireballEnchant.KEY, fireballEnchant);
        }

        return fireballEnchant;
    }
}
