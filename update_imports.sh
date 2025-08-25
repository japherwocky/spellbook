#!/bin/bash

# Script to update import statements from me.youhavetrouble.enchantio to me.japherwocky.spellbook
# Also updates class references from Enchantio to Spellbook and EnchantioEnchant to SpellbookEnchant

echo "Updating import statements in Java files..."

# Find all Java files in the src directory
find src -name "*.java" -type f | while read file; do
    echo "Processing file: $file"
    
    # Update static imports
    sed -i 's/import static me\.youhavetrouble\.enchantio\./import static me.japherwocky.spellbook./g' "$file"
    
    # Update regular imports
    sed -i 's/import me\.youhavetrouble\.enchantio\./import me.japherwocky.spellbook./g' "$file"
    
    # Update class references from Enchantio to Spellbook
    sed -i 's/Enchantio\./Spellbook./g' "$file"
    sed -i 's/import me\.japherwocky\.spellbook\.Enchantio;/import me.japherwocky.spellbook.Spellbook;/g' "$file"
    sed -i 's/\bEnchantio\b/Spellbook/g' "$file"
    
    # Update EnchantioEnchant to SpellbookEnchant
    sed -i 's/EnchantioEnchant/SpellbookEnchant/g' "$file"
done

echo "Import statements have been updated!"

