package me.japherwocky.spellbook.listeners;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import me.japherwocky.spellbook.Spellbook;
import me.japherwocky.spellbook.SpellbookConfig;
import me.japherwocky.spellbook.enchants.SpellbookEnchant;
import me.japherwocky.spellbook.enchants.ArmorEnchant;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@SuppressWarnings("UnstableApiUsage")
public class ArmorListener implements Listener {

    private final Registry<@NotNull Enchantment> registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT);
    private final Enchantment armor = registry.get(ArmorEnchant.KEY);
    
    // Unique modifier ID for our armor enchantment
    private static final UUID ARMOR_MODIFIER_UUID = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
    private static final String ARMOR_MODIFIER_NAME = "spellbook.armor_enchant";

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        updateArmorAttribute(event.getPlayer());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof LivingEntity entity) {
            // Delay the update to ensure inventory changes are processed
            entity.getServer().getScheduler().runTask(
                entity.getServer().getPluginManager().getPlugin("Spellbook"),
                () -> updateArmorAttribute(entity)
            );
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onItemHeld(PlayerItemHeldEvent event) {
        updateArmorAttribute(event.getPlayer());
    }

    private void updateArmorAttribute(LivingEntity entity) {
        if (armor == null) return;
        SpellbookEnchant enchant = SpellbookConfig.ENCHANTS.get(ArmorEnchant.KEY);
        if (!(enchant instanceof ArmorEnchant armorEnchant)) return;

        EntityEquipment equipment = entity.getEquipment();
        if (equipment == null) return;

        // Calculate total armor bonus from all equipped armor pieces
        int totalArmorLevel = 0;
        
        // Check all armor slots
        ItemStack helmet = equipment.getHelmet();
        ItemStack chestplate = equipment.getChestplate();
        ItemStack leggings = equipment.getLeggings();
        ItemStack boots = equipment.getBoots();
        
        if (helmet != null) {
            totalArmorLevel += helmet.getEnchantmentLevel(armor);
        }
        if (chestplate != null) {
            totalArmorLevel += chestplate.getEnchantmentLevel(armor);
        }
        if (leggings != null) {
            totalArmorLevel += leggings.getEnchantmentLevel(armor);
        }
        if (boots != null) {
            totalArmorLevel += boots.getEnchantmentLevel(armor);
        }

        // Get the armor attribute
        AttributeInstance armorAttribute = entity.getAttribute(Attribute.ARMOR);
        if (armorAttribute == null) return;

        // Remove existing modifier if present
        AttributeModifier existingModifier = armorAttribute.getModifier(ARMOR_MODIFIER_UUID);
        if (existingModifier != null) {
            armorAttribute.removeModifier(existingModifier);
        }

        // Add new modifier if there's any armor enchantment level
        if (totalArmorLevel > 0) {
            double armorBonus = totalArmorLevel * armorEnchant.getArmorPerLevel();
            AttributeModifier modifier = new AttributeModifier(
                ARMOR_MODIFIER_UUID,
                ARMOR_MODIFIER_NAME,
                armorBonus,
                AttributeModifier.Operation.ADD_NUMBER
            );
            armorAttribute.addModifier(modifier);
        }
    }
}
