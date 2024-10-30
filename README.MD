# Enchantio

Modern vanilla-style minecraft enchantment plugin for paper servers.

## Enchantments

### Soulbound
**Translation key**: `enchantio.enchantment.soulbound`

**Description**:
Keep the item in your inventory after death.

### Replanting
**Translation key**: `enchantio.enchantment.replanting`

**Description**:
Replants broken crops using seeds in your inventory or from the loot of the crop if no seeds are available in players inventory.

### Telepathy
**Translation key**: `enchantio.enchantment.telepathy`

**Description**:
Teleports dropped items to players location and makes them immediately pickuppable.

### Executioner
**Translation key**: `enchantio.enchantment.executioner`

**Description**:
Items enchanted with executioner will deal more damage to entities under specific health threshold.

### Beheading
**Translation key**: `enchantio.enchantment.beheading`

**Description**:
Adds a chance to drop the head of the entity killed (if entity has a head item available).

### Smelting
**Translation key**: `enchantio.enchantment.smelting`

**Description**:
Automatically smelts drops from mined blocks.

### Airbag
**Translation key**: `enchantio.enchantment.airbag`

**Description**:
Reduces damage from hitting a wall while flying with elytra.

## Curses

### Curse of Panic
**Translation key**: `enchantio.enchant.panic_curse`

**Description**:
When player takes damage, there is a chance that their hotbar items will be scrambled.

## Configuration

### supportedItemTags
There are 2 types of tags that can be used in the configuration:
- `minecraft:item_name` - For specific items, use namespaced vanilla item id. If namespace is not provided, `minecraft` is assumed.
- `#minecraft:item_tag_name` - For item tags, use namespaced tag id. If namespace is not provided, `minecraft` is assumed. Custom tags from other plugins and datapacks are also supported.

### activeSlots
Slot types that enchant is active in. Only some enchantments support this feature.

Available slot types:
- `MAINHAND` - Main hand slot.
- `OFFHAND` - Offhand slot.
- `ARMOR` - Any armor slot.
- `HELMET` - Helmet slot.
- `CHESTPLATE` - Chestplate slot.
- `LEGGINGS` - Leggings slot.
- `BOOTS` - Boots slot.
- `ANY` - Enchantment is active in all slots.
