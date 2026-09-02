# Spellbook Enchantments

This document provides a complete list of all standard enchantments available in the Spellbook plugin.

## Enabled Enchantments

The following enchantments are enabled by default and ready to use.

### Soulbound
**Description**: Keep the item in your inventory after death.

### Beheading
**Description**: Adds a chance to drop the head of the entity killed (if entity has a head item available).

### Homecoming
**Description**: When totem of undying is activated, player is teleported to their spawn point. If spawn point is not set, player is teleported to world spawn.

### Ward
**Description**: Automatically blocks hits, but triggers a configurable cooldown every time it does. It triggers a cooldown for the enchanted item's cooldown group.

### Flight
**Description**: Allows creative-like flight while wearing enchanted boots. Drains hunger (adds exhaustion) while flying. Automatically disables in creative/spectator mode.

### Fireball
**Description**: Allows players to shoot fireballs with enchanted swords. Has a 1-second cooldown between uses. Fireball power scales with enchantment level, and using the enchantment adds exhaustion based on level.

### Magic Missile
**Description**: Launches a magic missile arrow when using enchanted swords with bow-like charging mechanics.

**How to use:**
- Hold **right-click** to charge the missile
- **Left-click** to fire (or wait for max charge auto-fire)
- Press **sneak** to cancel charging

**Details:**
- Uses actual arrow projectiles that deal damage naturally through Minecraft's mechanics
- Arrows fly 40% faster than regular bow arrows
- Minimum charge: 0.25 seconds to fire
- Maximum charge: 1 second for full power
- Damage scales with charge: 50-100% of base damage
  - Level 1: 1.5-3 damage (0.75-1.5 hearts)
  - Level 2: 2-4 damage (1-2 hearts)  
  - Level 3: 2.5-5 damage (1.25-2.5 hearts)
- Charging particles spiral and change color (purple → magenta)
- Creates particle trail on arrow flight
- Cooldown: 1 second between casts
- Adds 4-6 exhaustion (~1-1.5 hunger points) per use

### Bless
**Description**: Adds flat damage bonus to weapon attacks. Each level adds +1 damage to every attack made with the enchanted weapon.

### Armor
**Description**: Increases armor points for better damage reduction. Each level adds +1 armor point to the player's total armor rating when wearing enchanted armor pieces.

### Vein Miner
**Description**: Breaks every connected block of the same type in one go. Mines up to 8 blocks at level 1, plus 8 more per level after that.

**Details:**
- Connected blocks are found by searching one block out in each direction (`searchRadius`), so diagonals count
- Drops respect Fortune and Silk Touch, since each block is broken with the tool in hand
- Drains hunger per block mined (`hungerCostPerBlock`), and stops early when the player runs out (`respectHunger`)
- Stops before the tool would break rather than snapping it (`respectDurability`)
- Only mines what the held tool can actually mine (`requireCorrectTool`)
- An optional whitelist restricts it to specific blocks (`useWhitelist`, off by default)

## Disabled Enchantments

The following enchantments are disabled by default pending testing. They can be enabled in `plugins/Spellbook/config.yml` by setting `enchants.<enchantment>.enabled: true`.

### Replanting
**Description**: Replants broken crops using seeds in your inventory or from the loot of the crop if no seeds are available in player's inventory.

### Telekinesis
**Description**: Teleports dropped items to player's location and makes them immediately pickuppable.

### Executioner
**Description**: Items enchanted with executioner will deal more damage to entities under specific health threshold.

### Smelting
**Description**: Automatically smelts drops from mined blocks.

### Airbag
**Description**: Reduces damage from hitting a wall while flying with elytra.

### Cloaking
**Description**: Gives invisibility effect when player is sneaking and not moving.

### Volley
**Description**: Shoots additional arrows when shooting a bow. Amount of arrows and their spread is configurable.

## Curses

### Panic
**Description**: Randomly teleports you when taking damage.
