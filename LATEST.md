# Spellbook v1.6.0 Release Notes

## Enchantment Fixes

- **Volley** — fixed a bug where Volley never fired any extra arrows (a wrong enchantment lookup made the effect silently do nothing). Volley now works as documented, for both normal and spectral arrows.
- **Replanting** — now only replants **fully-grown** crops. Breaking an immature crop no longer wastes a seed.
- **Cloaking** — taking damage now **breaks the cloak**: the invisibility ends immediately and the stillness timer resets. *(Behavior change — a cloaked player is no longer unbreakable while being attacked.)*
- **Smelting** — **Silk Touch now suppresses Smelting**: a tool with both enchantments drops ore blocks unsmelted, preserving the Silk Touch reward. *(Behavior change.)* Smelting still applies to any smeltable block drop (logs → charcoal, sand → glass, ores → ingots).
- **Armor** — fixed a bug where the armor-bonus attribute modifier could stack repeatedly. Armor values are now recalculated cleanly, and any already-stacked modifiers are removed automatically on the next login or equipment change.
- **Vein Miner** — durability now follows vanilla rules: Unbreaking applies, netherite tools respect the `unbreakableNetherite` setting while vein mining, and no durability/hunger is drained in creative mode.

## Improvements

- Smelting: recipe lookups are cached for non-smeltable items too (less work per block broken)
- Volley: normal and spectral arrows now behave symmetrically (same arrow-count scaling, consistent pickup rules)

## Internal

- Documentation refreshed for Java 25 / Paper 26.2 (README, AGENTS.md, enchantment list)
- Releases are published to Modrinth automatically by CI (since 1.5.1)

## Compatibility

- **Paper 26.2+** and **Java 25+** — unchanged from 1.5.x
- No config changes required; new behavior works out of the box

## Installation

1. Download `Spellbook-1.6.0.jar`
2. Place in your server's `plugins/` folder
3. Restart your server

---

For full enchantment documentation, see [docs/ENCHANTMENTS.md](docs/ENCHANTMENTS.md)
