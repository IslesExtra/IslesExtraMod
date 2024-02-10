package com.isles.skyblockisles.islesextra.bossrush.general;

import net.minecraft.client.MinecraftClient;

public class OnlyPartyMessages {

    public static boolean onlyPartyMessages(String message) {
        if (message.contains(MinecraftClient.getInstance().getSession().getUsername())) return true;
        else return message.toLowerCase().startsWith("party");
    }

}
