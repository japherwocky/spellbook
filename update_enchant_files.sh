#!/bin/bash

# Create directories for enchants, listeners, and events
mkdir -p src/main/java/me/japherwocky/spellbook/enchants
mkdir -p src/main/java/me/japherwocky/spellbook/listeners
mkdir -p src/main/java/me/japherwocky/spellbook/events

# Copy and update enchant files
for file in src/main/java/me/youhavetrouble/enchantio/enchants/*.java; do
  filename=$(basename "$file")
  if [ "$filename" != "EnchantioEnchant.java" ]; then
    echo "Processing enchant file: $filename"
    # Create the new file with updated package and imports
    sed -e 's/package me.youhavetrouble.enchantio.enchants;/package me.japherwocky.spellbook.enchants;/' \
        -e 's/import me.youhavetrouble.enchantio/import me.japherwocky.spellbook/g' \
        -e 's/implements EnchantioEnchant/implements SpellbookEnchant/g' \
        -e 's/EnchantioConfig/SpellbookConfig/g' \
        -e 's/static me.youhavetrouble.enchantio.EnchantioConfig.ENCHANTS/static me.japherwocky.spellbook.SpellbookConfig.ENCHANTS/g' \
        -e 's/Key.key("enchantio:/Key.key("spellbook:/g' \
        -e 's/Component.translatable("enchantio./Component.translatable("spellbook./g' \
        "$file" > "src/main/java/me/japherwocky/spellbook/enchants/$filename"
  fi
done

# Copy and update listener files
for file in src/main/java/me/youhavetrouble/enchantio/listeners/*.java; do
  filename=$(basename "$file")
  echo "Processing listener file: $filename"
  # Create the new file with updated package and imports
  sed -e 's/package me.youhavetrouble.enchantio.listeners;/package me.japherwocky.spellbook.listeners;/' \
      -e 's/import me.youhavetrouble.enchantio/import me.japherwocky.spellbook/g' \
      -e 's/EnchantioConfig/SpellbookConfig/g' \
      -e 's/Enchantio enchantio/Spellbook spellbook/g' \
      -e 's/Enchantio.getPlugin(Enchantio.class)/Spellbook.getPlugin(Spellbook.class)/g' \
      -e 's/private final Enchantio plugin/private final Spellbook plugin/g' \
      -e 's/public [A-Za-z]*Listener(Enchantio plugin)/public \1Listener(Spellbook plugin)/g' \
      "$file" > "src/main/java/me/japherwocky/spellbook/listeners/$filename"
done

# Copy and update event files
for file in src/main/java/me/youhavetrouble/enchantio/events/*.java; do
  filename=$(basename "$file")
  echo "Processing event file: $filename"
  # Create the new file with updated package and imports
  sed -e 's/package me.youhavetrouble.enchantio.events;/package me.japherwocky.spellbook.events;/' \
      -e 's/import me.youhavetrouble.enchantio/import me.japherwocky.spellbook/g' \
      "$file" > "src/main/java/me/japherwocky/spellbook/events/$filename"
done

echo "All files have been updated!"

