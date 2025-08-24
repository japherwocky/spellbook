This PR fixes the startup error caused by missing tag references in the missile enchantments.

## Problem
The server was failing to start with the following error:
```
[15:31:49 ERROR]: Couldn't load tag enchantio:flight_enchantable as it is missing following references: #minecraft:enchantable/armor_feet (from Enchantio v1.13.1)
```

## Solution
Replace non-existent tag references with specific item types:
- Replace `#minecraft:enchantable/armor_feet` with specific boot types (iron, gold, diamond, etc.)
- Replace `#minecraft:enchantable/weapon` with specific sword types (wooden, stone, iron, etc.)

This allows the plugin to start correctly without relying on tag references that don't exist in the current Minecraft version.

Co-authored-by: Japhy Bartlett <github@pearachute.com>

