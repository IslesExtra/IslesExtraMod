package com.isles.skyblockisles.islesextra.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

import java.util.Arrays;

public class ItemUtils {

    //TODO: DONT ITERATE THROUGH DISPLAY BUT THROUGH TOP LEVEL NBT VALUES FOR [RARITY]_TYPE
    public static String getRarity(ItemStack item) {
        NbtCompound nbtCompound = item.getNbt();
        if (nbtCompound == null) return IslesConstants.Rarity.NONE.toString();

        NbtList loreList = nbtCompound.getCompound("display").getList("Lore", 8);

        //Iterate through every Element in Lorelist, return Rarity if a Value of IslesConstants.Rarity is found
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
