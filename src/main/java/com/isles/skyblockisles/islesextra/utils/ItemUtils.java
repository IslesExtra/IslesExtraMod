package com.isles.skyblockisles.islesextra.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

public class ItemUtils {

    //TODO: DONT ITERATE THROUGH DISPLAY BUT THROUGH TOP LEVEL NBT VALUES FOR [RARITY]_TYPE
    public static String getRarity(ItemStack item) {
        NbtCompound nbtCompound = item.getNbt();
        if (nbtCompound == null) return "none";

        NbtList loreList = nbtCompound.getCompound("display").getList("Lore", 8);

        //Iterate through every Element in Lorelist, return Rarity if a Value of rarities is found
        return loreList.stream()
                .map(NbtElement::toString)
                .filter(loreString -> IslesConstants.rarities.stream().anyMatch(loreString::contains))
                .findFirst()
                .map(rarity -> IslesConstants.rarities.stream().filter(rarity::contains).findFirst().orElse("none"))
                .orElse("None");
    }



}
