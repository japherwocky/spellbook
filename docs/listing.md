# Spellbook

Sixteen enchantments for Paper servers that behave like Mojang made them.
They show up in the enchanting table, they cost levels the way vanilla
enchantments do, and they follow the vanilla rules for what they attach to.
Nothing glows purple or breaks the economy; it's just more enchanting table.

## The enchantments

**Soulbound** — enchanted items stay in your inventory when you die.

**Telekinesis** — mined blocks go straight to your inventory.

**Replanting** — breaking a fully-grown crop replants it for free.

**Vein Miner** — mining one ore breaks the whole vein. Durability follows
vanilla rules, and protection plugins can still cancel individual blocks.

**Smelting** — mined blocks drop their smelted form: logs give charcoal, sand
gives glass, ore gives ingots. Silk Touch wins if a tool has both.

**Executioner** — bonus damage against targets that are already low.

**Beheading** — axes have a chance to take a mob's head as a trophy.

**Volley** — bows fire a spread of arrows, one more per level.

**Fireball** — right-click with a sword to throw a fireball.

**Magic Missile** — hold right-click to charge, release to fire a homing
arrow.

**Bless** — flat damage bonus on weapons, +1 per level.

**Armor** — bonus armor points, +1 per level.

**Ward** — a shield that blocks a hit on its own, with a cooldown.

**Cloaking** — go invisible while sneaking and standing still. Taking a hit
breaks it.

**Airbag** — elytra crash damage cut by 20% per level.

**Flight** — creative-style flight on boots, paid for in hunger.

**Homecoming** — a totem of undying that teleports you to your spawn instead
of saving your life.

**Panic** (curse) — sometimes teleports you when you take damage. It's a
curse. It's in the loot pool anyway.

## And one extra

**Unbreakable Netherite** — netherite gear takes no durability damage at all.
Turn it off in the config if it's too kind.

## For server admins

Everything is configurable in `config.yml`: costs, weights, max levels, which
items accept each enchantment, and an on/off switch per enchantment. The
defaults are tuned to sit alongside vanilla enchantments without crowding
them out of the table.

## Requirements

- Paper 26.2 or newer
- Java 25 or newer

## Setup

1. Drop the jar in your `plugins` folder.
2. Restart the server.
3. Enchant things.
