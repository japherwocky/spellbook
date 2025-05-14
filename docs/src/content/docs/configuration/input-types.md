---
title: Input types
description: How to specify common input types in configuration
---

## Tags
There are 2 types of tags that can be used in the configuration:
- `minecraft:item_name` - For specific items, use namespaced vanilla item id. If namespace is not provided, `minecraft` is assumed.
- `#minecraft:item_tag_name` - For <a href="https://minecraft.wiki/w/Item_tag_(Java_Edition)" target="_blank">item tags</a>,
  use namespaced tag id. If namespace is not provided, `minecraft` is assumed. Custom tags from other plugins and
  datapacks are also supported.

## Slot types
- `ANY` - Any slot.
- `MAINHAND` - Main hand slot.
- `OFFHAND` - Offhand slot.
- `ARMOR` - Any armor slot.
- `HELMET` - Helmet slot.
- `CHESTPLATE` - Chestplate slot.
- `LEGGINGS` - Leggings slot.
- `BOOTS` - Boots slot.
