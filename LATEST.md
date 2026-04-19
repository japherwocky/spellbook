# Spellbook v1.4.0 Release Notes

## Platform Update

### Paper 1.26.1.2 Support
This release updates Spellbook to support **Minecraft 1.26.1.2** running on **Paper build 13**.

- **Updated Paper API**: `1.21.11-R0.1-SNAPSHOT` → `26.1.2.build.13-alpha`
- **API Version**: `1.21.8` → `1.26.1.2`
- **Java Version**: Still requires Java 21

## Compatibility

All existing enchantments and features remain fully functional:
- Soulbound - Keep items on death
- Telekinesis - Auto-collect drops
- Replanting - Auto-replant crops
- Executioner - Bonus damage to low-health targets
- Beheading - Chance to drop mob heads
- Smelting - Auto-smelt mined blocks
- Airbag - Elytra crash damage reduction
- Homecoming - Totem teleports to spawn
- Cloaking - Invisibility while sneaking
- Volley - Multiple arrows per shot
- Ward - Auto-block with cooldown
- Flight - Creative flight on boots (drains hunger)
- Fireball - Shoot fireballs with swords
- Magic Missile - Homing missiles on right-click
- Bless - Flat damage bonus (+1 per level)
- Armor - Bonus armor points (+1 per level)
- Vein Miner - Mine connected ore veins
- Panic (Curse) - Triggers on low health

### Unbreakable Netherite Feature
Netherite armor and tools continue to take **zero durability damage**.
- Configurable via `unbreakableNetherite.enabled` in config.yml

## Technical Changes

- Updated Maven dependency to use new Paper versioning scheme (`26.1.2.build.13-alpha`)
- No breaking API changes required in plugin code
- All enchantment registry and event handling APIs remain compatible

## Installation

1. Download `Spellbook-1.4.0.jar`
2. Place in your server's `plugins/` folder
3. Ensure you're running **Paper 1.26.1.2 (build 13)** or compatible version
4. Restart your server

---

For full enchantment documentation, see [docs/ENCHANTMENTS.md](docs/ENCHANTMENTS.md)
