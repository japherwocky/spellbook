package me.japherwocky.spellbook.listeners;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import me.japherwocky.spellbook.Spellbook;
import me.japherwocky.spellbook.SpellbookConfig;
import me.japherwocky.spellbook.enchants.SpellbookEnchant;
import me.japherwocky.spellbook.enchants.BlessEnchant;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EntityEquipment;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public class BlessListener implements Listener {

    private final Registry<@NotNull Enchantment> registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT);
    private final Enchantment bless = registry.get(BlessEnchant.KEY);

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onBlessDamage(EntityDamageByEntityEvent event) {
        if (bless == null) return;
        SpellbookEnchant enchant = SpellbookConfig.ENCHANTS.get(BlessEnchant.KEY);
        if (!(enchant instanceof BlessEnchant blessEnchant)) return;
        
        Entity damager = event.getDamageSource().getCausingEntity();
        if (damager == null) return;
        if (!damager.equals(event.getDamageSource().getDirectEntity())) return;
        if (!(damager instanceof LivingEntity damagerEntity)) return;

        EntityEquipment damagerEquipment = damagerEntity.getEquipment();
        if (damagerEquipment == null) return;
        
        int level = Spellbook.getSumOfEnchantLevels(damagerEquipment, bless);
        if (level == 0) return;

        // Add flat damage bonus: +1 damage per level
        double bonusDamage = level * blessEnchant.getDamagePerLevel();
        event.setDamage(event.getDamage() + bonusDamage);
    }
}
