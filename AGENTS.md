# Spellbook - Agent Context

This document provides context for AI agents working on the Spellbook Minecraft plugin.

## Project Overview

**Spellbook** is a modern vanilla-style enchantment plugin for Paper Minecraft servers (1.21+). It adds custom enchantments that feel native to the game using Paper's modern registry API.

- **Language**: Java 21
- **Build Tool**: Maven
- **Platform**: Paper API 26.2+ (Minecraft 26.2)
- **License**: See LICENSE file

## Architecture

### Core Structure

```
me.japherwocky.spellbook/
├── Spellbook.java              # Main plugin class, registers listeners
├── SpellbookConfig.java        # Configuration management & enchant registration
├── SpellbookBootstrap.java     # Bootstrapper for enchantment registry
├── enchants/                   # Enchantment definitions
│   ├── SpellbookEnchant.java   # Base interface all enchants implement
│   └── *Enchant.java           # Individual enchantment implementations
├── listeners/                  # Event listeners for enchantment effects
│   └── *Listener.java          # Per-enchant effect handlers
└── events/                     # Custom events
    └── *.java
```

### Adding a New Enchantment

To add a new enchantment, you need to create/modify **four components**:

1. **Enchantment Definition** (`enchants/YourEnchant.java`)
   - Implement `SpellbookEnchant` interface
   - Define key, description, costs, max level, weight
   - Specify supported items and active slot groups
   - Create a static `create(ConfigurationSection)` factory method

2. **Event Listener** (`listeners/YourListener.java`)
   - Implements `Listener`
   - Contains the actual enchantment logic (effects)
   - References enchant via its `KEY` constant

3. **Configuration Registration** (`SpellbookConfig.java`)
   - Add config section creation in `init()` method
   - Call `YourEnchant.create(section)`

4. **Listener Registration** (`Spellbook.java`)
   - Register listener in `onEnable()` if enchant is enabled

### Enchantment Interface (`SpellbookEnchant`)

All enchantments must implement:

```java
@NotNull Key getKey();                                    // Unique identifier (e.g., "spellbook:bless")
@NotNull Component getDescription();                      // Display name/translatable
int getAnvilCost();                                       // XP cost in anvil
int getMaxLevel();                                        // Maximum enchant level
int getWeight();                                          // Rarity weight
@NotNull EnchantmentCost getMinimumCost();               // Min level for enchanting table
@NotNull EnchantmentCost getMaximumCost();               // Max level for enchanting table
@NotNull Set<EquipmentSlotGroup> getActiveSlotGroups();  // When enchant is active
@NotNull Set<TagEntry<ItemType>> getSupportedItems();    // What items can have this
@NotNull Set<TagKey<Enchantment>> getEnchantTagKeys();   // Tags (e.g., #in_enchanting_table)
```

### Configuration Pattern

Enchantments use YAML configuration with defaults:

```yaml
enchants:
  yourenchant:
    enabled: true
    anvilCost: 1
    weight: 10
    maxLevel: 5
    minimumCost:
      base: 1
      additionalPerLevel: 11
    maximumCost:
      base: 21
      additionalPerLevel: 11
    enchantmentTags:
      - "#in_enchanting_table"
    supportedItems:
      - "#swords"
      - "minecraft:trident"
    activeSlots:
      - "mainhand"
```

Use `SpellbookConfig.getInt()`, `getString()`, `getBoolean()`, etc. with sensible defaults.

## Current Enchantments

### Standard Enchantments
- **Soulbound** - Keep item on death
- **Telekinesis** - Auto-collect drops
- **Replanting** - Auto-replant crops
- **Executioner** - Bonus damage to low-health targets
- **Beheading** - Chance to drop mob heads
- **Smelting** - Auto-smelt mined blocks
- **Airbag** - Elytra crash damage reduction
- **Homecoming** - Totem teleports to spawn
- **Cloaking** - Invisibility while sneaking
- **Volley** - Multiple arrows per shot
- **Ward** - Auto-block with cooldown
- **Flight** - Creative flight on boots (drains hunger)
- **Fireball** - Shoot fireballs with swords
- **Magic Missile** - Homing missiles on right-click
- **Bless** - Flat damage bonus (+1 per level)
- **Armor** - Bonus armor points (+1 per level)

### Curses
- **Panic** - (Curse) Triggers on low health

### Features (Non-Enchantment)
- **Unbreakable Netherite** - Netherite armor and tools take no durability damage. Configurable via `unbreakableNetherite.enabled` in config.yml (default: true). The durability bar will not appear on fresh netherite items, and existing damage is preserved (not repaired).

## Key Patterns

### Utility Methods in `Spellbook.java`

```java
// Get highest enchant level across all equipment slots
Spellbook.getHighestEnchantLevel(equipment, enchantment)

// Get sum of enchant levels across all slots
Spellbook.getSumOfEnchantLevels(equipment, enchantment)

// Find first item with enchant
SpellbookEnchant.findFirstWithEnchant(equipment, enchantment)
```

### Checking for Enchant

```java
int level = equipment.getItemInMainHand().getEnchantmentLevel(YourEnchant.KEY);
if (level > 0) {
    // Enchant is present
}
```

## Building

```bash
mvn clean package
```

Output: `target/Spellbook-{version}.jar`

## Testing

1. Build the plugin
2. Copy JAR to Paper server's `plugins/` folder
3. Restart server
4. Use `/enchant` commands or enchanting table to test

## Code Style Notes

- Use `@SuppressWarnings("UnstableApiUsage")` on classes using Paper's registry API
- All enchant keys use `spellbook:` namespace
- Use `Component.translatable()` for enchantment names
- Equipment slot checking respects the enchant's `activeSlotGroups`
- Config getters provide sensible defaults

## Paper API Notes

This plugin uses Paper's modern **Registry API** for enchantments (1.21+):
- `RegistryKey.ENCHANTMENT` for enchantment registry
- `TypedKey` for typed registry keys
- `TagKey` and `TagEntry` for tags
- `EnchantmentRegistryEntry` for enchantment data

This is the modern replacement for Bukkit's old enchantment API.
