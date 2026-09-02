# Spellbook v1.5.1 Release Notes

## Catch-up Release for Modrinth

Modrinth previously had **1.2.0**, so this version skips ahead and includes everything from 1.3.0–1.5.0 (Vein Miner enchantment, Paper 26.2 support, Java 25).

## Changes since 1.5.0

- **Automated Modrinth publishing**: releases are now published to Modrinth by CI via [mc-publish](https://github.com/Kira-NT/mc-publish) (gated to release creation only)
- **Documentation**: Vein Miner added to the enchantment list; README refreshed for Java 25 / Paper 26.2
- **No plugin code changes** — this is a release-engineering and documentation release

## Compatibility

- **Paper 26.2 (build 112)** or compatible
- **Java 25+** (both build JDK and server JVM)
- All enchantments and features from 1.5.0 remain fully functional

## Installation

1. Download `Spellbook-1.5.1.jar`
2. Place in your server's `plugins/` folder
3. Restart your server

---

For full enchantment documentation, see [docs/ENCHANTMENTS.md](docs/ENCHANTMENTS.md)
