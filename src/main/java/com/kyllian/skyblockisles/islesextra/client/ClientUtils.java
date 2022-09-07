package com.kyllian.skyblockisles.islesextra.client;

import com.kyllian.skyblockisles.islesextra.IslesExtra;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public abstract class ClientUtils {

    public static void sendMessage(String message) {
        assert MinecraftClient.getInstance().player != null;
        MinecraftClient.getInstance().player.sendMessage(Text.of(message), false);
    }

    public static void sendMessage(Text message) {
        assert MinecraftClient.getInstance().player != null;
        MinecraftClient.getInstance().player.sendMessage(message, false);
    }

    public static void openScreen(Screen screen) {
        IslesExtra.nextScreen = screen;
    }

}
