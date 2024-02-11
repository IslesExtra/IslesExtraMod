package com.isles.skyblockisles.islesextra.utils;

import com.isles.skyblockisles.islesextra.IslesExtra;
import com.isles.skyblockisles.islesextra.client.screen.IslesHudHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.*;

public abstract class ClientUtils {

    public static MinecraftClient getClient() {return MinecraftClient.getInstance();}
    public static PlayerEntity getPlayer() {return getClient().player;}
    public static World getWorld() {return getClient().world;}
    public static InGameHud getHUD() {return getClient().inGameHud;}
    public static Boolean inBoss() {return IslesHudHandler.inBoss;}
    public static IslesConstants.Boss getBoss() {
        return Arrays.stream(IslesConstants.Boss.values())
                .filter(boss -> getWorld().getRegistryKey().toString().toLowerCase().contains(boss.toString().toLowerCase()))
                .findFirst()
                .orElse(IslesConstants.Boss.NOBOSS072);
    }

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

    public static void sendTitle(String title, int fade, int stay, int leave) {
        getHUD().setTitleTicks(fade, stay, leave);
        getHUD().setTitle(Text.of(title));
    }
    public static void sendTitle(String title){
        sendTitle(title, 5, 30, 5);
    }

}
