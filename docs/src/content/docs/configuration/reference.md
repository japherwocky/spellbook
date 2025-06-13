---
title: Configuration reference
description: Comprehensive reference of all configuration options available in Enchantio
---


## Common options

Options that are available for all (or most) enchantments and are required for the enchantment to work.

### anvilCost

- **Type**: `int`

Cost that will be added to the item when adding the enchantment to it in anvil.

### weight

- **Type**: `int`

Weight of the enchantment. Used if `canGetFromEnchantingTable` is set to true. Higher numbers mean it will come up more
frequently in the enchanting table.

### minimumCost

Minimum cost that will be required for the enchantment to show up in the enchanting table. **This is NOT levels**.

#### base

- **Type**: `int`

Base cost of the enchant.

#### perLevel

- **Type**: `int`

Cost to add per level of the enchantment.

### maximumCost

Maximum cost that will be required for the enchantment to show up in the enchanting table. **This is NOT levels**.

#### base

- **Type**: `int`

Base cost of the enchant.

#### perLevel

- **Type**: `int`

Cost to add per level of the enchantment.

### enchantmentTags

- **Type**: `key[]`

This is list of [tags](/configuration/input-types#tags) that the enchantment will be tagged with. This can be used to put the enchantment in enchanting table,
mark it as a curse, etc. <a href="https://minecraft.wiki/w/Enchantment_tag_(Java_Edition)" target="_blank">All vanilla</a>
and custom tags are supported.

### supportedItemTags

- **Type**: `key[]`

[Items and/or item tags](/configuration/input-types#tags) that the enchantment can be applied to.

### activeSlots

- **Type**: `string[]`

[Slots](/configuration/input-types#slot-types) that the enchantment will be active in.


### enabled

- **Type**: `boolean`

Decides if the enchantment is registered or not.

:::caution[WARNING]
If you disable an enchantment that is already on an item, it will be removed from the item when it's loaded.
It will also come with warnings in the console. It's not recommended to disable enchantments after they are already in
use.
:::

#### maxLevel

- **Type**: `int`

Maximum level of the enchantment. If set to 1, the enchantment will be a single level enchantment. If this option is not
in the configuration section for specific enchantment, it means it's locked to a single level enchantment, because logic
of the enchantment does not support multiple levels.

## Telepathy

- **Type**: `boolean`

If set to true, items teleported by the enchant will only be able to be picked up by the player that broke the block.

## Executioner

### maxDamageHpThreshold

- **Type**: `double`

Health threshold under which the enchantment will deal more damage.

### damageMultiplierPerLevel

- **Type**: `double`

Multiplier that will be applied to the damage dealt by the enchantment. Value of the multiplier is added to 1.0, so if
you want to deal 2x damage, set this to 1.0. If you want to deal 3x damage, set this to 2.0 and so on. This is additive.

## Airbag

### damageReductionPerLevel

- **Type**: `double`

Damage reduction that will be applied to the damage dealt by the enchantment. Value of the multiplier is added to 1.0,
so if you want to reduce damage by 50%, set this to 0.5. If you want to reduce damage by 75%, set this to 0.25 and so
on. This is additive.

## Cloaking

### ticksToActivate

- **Type**: `int`

Amount of ticks player needs to be sneaking in place for the enchantment to activate.

## Volley

### additionalArrowsPerLevel

- **Type**: `int`

Amount of arrows to shoot additionally per shot. This is additive, so if you want to shoot 3 arrows, set this to 2.

## Ward

### cooldownTicks

- **Type**: `int`

Cooldown in ticks that will be applied to the activating items cooldown group.

### blockSound

- **Type**: `string`

Namespaced key of the sound that will play when enchantment blocks a hit.

## Panic

### panicChancePerLevel

- **Type**: `double`

Chance of the enchantment to activate when player takes damage.
