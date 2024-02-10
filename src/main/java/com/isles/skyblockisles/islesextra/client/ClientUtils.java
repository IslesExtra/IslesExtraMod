package com.isles.skyblockisles.islesextra.client;

import com.isles.skyblockisles.islesextra.IslesExtra;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;

import java.util.HashSet;
import java.util.Set;

public abstract class ClientUtils {

    public static void sendMessage(String message) {
        sendMessage(Text.of(message));
    }

    public static boolean sendMessage(Text message) {
        if (MinecraftClient.getInstance().player == null) return false;
        MinecraftClient.getInstance().player.sendMessage(message, false);
        return true;
    }

    public static void scheduleNextTick(Runnable task) {
        IslesExtra.tasks.add(task);
    }

    public static Set<String> getPlayerNames(boolean at) {
        ClientPlayNetworkHandler networkHandler = MinecraftClient.getInstance().getNetworkHandler();
        if (networkHandler == null) return Set.of();
        Set<String> list = new HashSet<>();
        for (PlayerListEntry playerListEntry : networkHandler.getPlayerList()) {
            if (!playerListEntry.getProfile().getName().startsWith("|")) list.add((at ? "@" : "") + playerListEntry.getProfile().getName());
        }
        return list;
    }

}
