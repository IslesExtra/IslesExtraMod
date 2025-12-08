package com.isles.skyblockisles.islesextra.utils;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

import java.util.Arrays;

public class ItemUtils {

    //TODO: DONT ITERATE THROUGH DISPLAY BUT THROUGH TOP LEVEL NBT VALUES FOR [RARITY]_TYPE
    public static String getRarity(ItemStack item) {
        var nbtComponent = item.get(DataComponentTypes.CUSTOM_DATA);
        if (nbtComponent == null) return IslesConstants.Rarity.NONE.toString();
        NbtCompound nbtCompound = nbtComponent.copyNbt().getCompoundOrEmpty("display");
        var loreList = nbtCompound.getList("Lore");

        // Iterate through every Element in Lorelist, return Rarity if a Value of IslesConstants.Rarity is found
        return (loreList.stream()
                .map(NbtElement::toString)
                .filter(loreString -> Arrays.stream(IslesConstants.Rarity.values()).anyMatch(rarity -> loreString.contains(rarity.name())))
                .findFirst()
                .map(rarity -> Arrays.stream(IslesConstants.Rarity.values())
                        .filter(r -> rarity.contains(r.name()))
                        .findFirst()
                        .orElse(IslesConstants.Rarity.NONE))
                .orElse(IslesConstants.Rarity.NONE)).toString();

    }
}
