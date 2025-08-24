package me.youhavetrouble.enchantio.listeners;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import me.japherwocky.spellbook.Spellbook;
import me.japherwocky.spellbook.EnchantioConfig;
import me.japherwocky.spellbook.enchants.BeheadingEnchant;
import me.japherwocky.spellbook.enchants.SpellbookEnchant;
import me.japherwocky.spellbook.events.EntityBeheadEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;


@SuppressWarnings("UnstableApiUsage")
public class BeheadingListener implements Listener {

    private final Registry<Enchantment> registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT);
    private final Enchantment beheading = registry.get(BeheadingEnchant.KEY);

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onBeheading(EntityDeathEvent event) {
        if (beheading == null) return;
        SpellbookEnchant enchant = EnchantioConfig.ENCHANTS.get(BeheadingEnchant.KEY);
        if (!(enchant instanceof BeheadingEnchant beheadingEnchant)) return;
        if (event.getDamageSource().isIndirect()) return;
        Entity killer = event.getDamageSource().getCausingEntity();
        if (killer == null) return;
        if (!(killer instanceof LivingEntity killerEntity)) return;
        EntityEquipment killerEquipment = killerEntity.getEquipment();
        if (killerEquipment == null) return;

        int level = Spellbook.getHighestEnchantLevel(killerEquipment, beheading);
        if (level == 0) return;

        double chance = level * beheadingEnchant.getChanceToDropHeadPerLevel();

        if (ThreadLocalRandom.current().nextDouble() > chance) return;

        ItemStack head = getHeadForEntity(event.getEntity(), event.getDrops());
        if (head == null) return;
        EntityBeheadEvent beheadEvent = new EntityBeheadEvent(event.getEntity(), head);
        Bukkit.getPluginManager().callEvent(beheadEvent);
        if (beheadEvent.isCancelled()) return;
        event.getDrops().add(beheadEvent.getHeadToDrop());
    }

    private ItemStack getHeadForEntity(Entity entity, Collection<ItemStack> drops) {
        ItemStack head = null;

        switch (entity.getType()) {
            case ZOMBIE -> {
                if (listContainsItemType(drops, Material.ZOMBIE_HEAD)) return null;
                head = new ItemStack(org.bukkit.Material.ZOMBIE_HEAD);
            }
            case PIGLIN -> {
                if (listContainsItemType(drops, Material.PIGLIN_HEAD)) return null;
                head = new ItemStack(org.bukkit.Material.PIGLIN_HEAD);
            }
            case WITHER_SKELETON -> {
                if (listContainsItemType(drops, Material.WITHER_SKELETON_SKULL)) return null;
                head = new ItemStack(org.bukkit.Material.WITHER_SKELETON_SKULL);
            }
            case SKELETON -> {
                if (listContainsItemType(drops, Material.SKELETON_SKULL)) return null;
                head = new ItemStack(org.bukkit.Material.SKELETON_SKULL);
            }
            case CREEPER -> {
                if (listContainsItemType(drops, Material.CREEPER_HEAD)) return null;
                head = new ItemStack(org.bukkit.Material.CREEPER_HEAD);
            }
            case ENDER_DRAGON -> {
                if (listContainsItemType(drops, Material.DRAGON_HEAD)) return null;
                head = new ItemStack(org.bukkit.Material.DRAGON_HEAD);
            }
            case PLAYER -> {
                if (listContainsItemType(drops, Material.PLAYER_HEAD)) return null;
                Player player = (Player) entity;
                head = getPlayerHead(player);
            }
        }
        return head;
    }

    private boolean listContainsItemType(@NotNull Iterable<ItemStack> list, @NotNull Material type) {
        for (ItemStack item : list) {
            if (type.equals(item.getType())) return true;
        }
        return false;
    }

    private ItemStack getPlayerHead(@NotNull Player player) {
        ItemStack head = new ItemStack(org.bukkit.Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwningPlayer(player);
        head.setItemMeta(meta);
        return head;
    }

}
