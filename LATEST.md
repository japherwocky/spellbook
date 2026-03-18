# Spellbook v1.2.1 Release Notes

## Changes

### Fireball Enchantment
- **Hunger drain increased 10x** - Casting fireballs now consumes significantly more hunger, making it a more costly ability to use
  - Level 1: ~2.5 hunger per cast (was ~0.25)
  - Level 2: ~3.75 hunger per cast (was ~0.125)
- **Added sword swing animation** - Players now see their sword swing when casting fireballs, making the ability feel more responsive

---

# Spellbook v1.2.0 Release Notes

## New Features

### Unbreakable Netherite
Netherite armor and tools now take **zero durability damage**. This is designed for end-game players who want to use their best gear without worrying about mending or repair costs.

- **What it affects**: All netherite equipment
  - Armor: Helmet, Chestplate, Leggings, Boots
  - Tools: Sword, Pickaxe, Axe, Shovel, Hoe
- **Behavior**:
  - Fresh netherite items will not show a durability bar
  - Existing damaged items retain their current damage (not repaired)
  - No new durability damage is applied from any source (mob attacks, tool use, etc.)
- **Configuration**: Edit `plugins/Spellbook/config.yml` to toggle:
  ```yaml
  unbreakableNetherite:
    enabled: true  # set to false to disable
  ```

## Bug Fixes

- Fixed durability bar appearing unexpectedly on brand new netherite items
- Fixed client/server sync issues with durability updates

## Maintenance

- Cleaned up compiler warnings for JDK 21 compatibility
- Improved attribute modifier handling in Armor enchantment

---

For full enchantment documentation, see [docs/ENCHANTMENTS.md](docs/ENCHANTMENTS.md)
