# Spellbook - Agent Context

This document provides context for AI agents working on the Spellbook Minecraft plugin.

## Project Overview

**Spellbook** is a modern vanilla-style enchantment plugin for Paper Minecraft servers (1.21+). It adds custom enchantments that feel native to the game using Paper's modern registry API.

- **Language**: Java 25
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

### Toolchain gotchas

- **Java version is tied to the Paper API version, not chosen freely.** Paper occasionally bumps the Java bytecode version its own `paper-api` jar is compiled with (e.g. the 26.x line moved to Java 25 / class file major version 69). javac's `--release` flag hard-rejects any classpath entry with a *newer* class file version than the target release, even when the compiler itself is a newer JDK — so `<java.version>` in [pom.xml](pom.xml) must be bumped to match whatever `paper-api` requires whenever it's upgraded, or the build fails with `bad class file ... wrong version`.
- **`maven-shade-plugin` needs to be new enough to shade that bytecode too.** Its bundled ASM library independently needs to support the target class file version (3.6.2+ for Java 25/class file 69) or `mvn package` fails at the shade step even after the compile step succeeds.
- **CI workflow JDK versions must be kept in sync with `pom.xml`.** [.github/workflows/build.yml](.github/workflows/build.yml) and [.github/workflows/release.yml](.github/workflows/release.yml) each pin a `java-version` in the `setup-java` step independently of `pom.xml`'s `<java.version>`. These silently drift out of sync — this repo had every CI build red for four months (April-August 2026) after a Paper bump before anyone noticed, since nothing was gating merges on it.
- When bumping the Paper API version, always do a real local `mvn clean package` (not just skim the diff) before pushing — a clean local build catches this class of failure immediately if your local JDK happens to already be new enough.

## Release Process

Releases are cut by creating a GitHub release, not just pushing a tag:

```bash
gh release create 1.5.0 --title "Spellbook v1.5.0" --notes "..."
```

- Tag name is the bare version (no `v` prefix, per current convention — some older tags do have one).
- Creating the release triggers [release.yml](../.github/workflows/release.yml) (`on: release: [created, edited]`), which builds with `-Drevision=<tag>`, uploads the shaded jar as a release asset, and publishes the version to Modrinth (see below).
- Editing a release's notes *should* also re-trigger the build, but this has been unreliable in practice — if a build needs to be retried, deleting and recreating the release (`gh release delete <tag> --yes --cleanup-tag`, then `gh release create` again) is the reliable path.
- Don't move an already-pushed tag to a new commit (`git tag -f` + force-push) to retry a broken release — delete and recreate instead; force-pushing a shared ref is a bigger footgun than a clean redo.
- Always confirm the resulting release actually has a jar attached (`gh release view <tag> --json assets`) — a failed build still leaves the release published, just asset-less.
- Remember to mark the newest version `--latest` after cutting it, especially if releases were created out of chronological order (e.g. backfilling older versions after the newest one already exists).

### Publishing to Modrinth (and CurseForge)

The release workflow publishes each new release to Modrinth via [mc-publish](https://github.com/Kira-NT/mc-publish) (`Kira-NT/mc-publish@v3`), a build-system-agnostic GitHub Action (TypeScript, runs on the runner's Node — fully decoupled from this Maven build). The Modrinth project is **`spellbook-plugin`** (not `spellbook` — that's the README link's guess). The plugin's metadata is supplied **explicitly** in the workflow: mc-publish's auto-detection only understands Fabric/Forge/Quilt metadata files, not `paper-plugin.yml`, so `loaders`, `game-versions`, `version`, and the changelog are set by hand. No extra metadata files live in this repo, and the plugin stays a pure Paper plugin.

- **One-time setup**: create a Modrinth personal access token (Modrinth → Settings → Authorization tokens, with the *Create versions* scope) and add it as the repository secret `MODRINTH_TOKEN`. Without the secret the publish step fails.
- **Gating**: the publish step runs only on `created` release events, never `edited`, so editing release notes can never re-upload and collide with the published version.
- **Redo caveat**: Modrinth rejects a second upload with the same version number. If you delete and recreate a release (the reliable retry path above) that already published, delete the version on Modrinth (project page → Versions) before re-cutting.
- **Verification**: a successful run writes the published Modrinth URL into the job summary. Also glance at the Modrinth project's Versions page — a "successful" publish with wrong metadata still appears, just discoverable incorrectly.
- **Enabling CurseForge**: an older version of the plugin was published there historically. To enable: add a `CURSEFORGE_TOKEN` secret (CF API token with upload rights to the project), then uncomment `curseforge-id` / `curseforge-token` in the publish step and fill in the numeric project ID from the CF project page ("About" section). Note CurseForge has a moderation/review queue for new files.

#### Release-publishing gotcha: `game-versions` drift

The `game-versions` list in [release.yml](../.github/workflows/release.yml) is hardcoded and must be updated on every Minecraft/Paper bump, alongside `api-version` in `paper-plugin.yml` and `<java.version>` in `pom.xml`. Nothing fails when it drifts — releases just don't appear for newer Minecraft versions. This is the same silent-drift failure class as the CI JDK version gotcha above; fold it into the same Paper-bump checklist.

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

This plugin uses Paper's modern **Registry API** for enchantments (1.26.2+):
- `RegistryKey.ENCHANTMENT` for enchantment registry
- `TypedKey` for typed registry keys
- `TagKey` and `TagEntry` for tags
- `EnchantmentRegistryEntry` for enchantment data

This is the modern replacement for Bukkit's old enchantment API.
