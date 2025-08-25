#!/bin/bash

# Find all enchant classes
enchant_files=$(grep -l "getActiveSlots" --include="*.java" src/main/java/me/japherwocky/spellbook/enchants/*.java)

# Update each file
for file in $enchant_files; do
  echo "Updating $file"
  sed -i 's/public @NotNull Iterable<EquipmentSlotGroup> getActiveSlots()/public @NotNull Set<EquipmentSlotGroup> getActiveSlotGroups()/g' "$file"
done

echo "All files updated!"
