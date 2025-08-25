#!/bin/bash

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
      "$file" > "src/main/java/me/japherwocky/spellbook/listeners/$filename"
done

echo "Listener files have been updated!"

