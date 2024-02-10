package com.isles.skyblockisles.islesextra.client.screen;

import com.isles.skyblockisles.islesextra.client.IslesExtraClient;
import com.isles.skyblockisles.islesextra.event.LeftIslesCallback;
import com.isles.skyblockisles.islesextra.event.SwitchedIslesServerCallback;
import com.isles.skyblockisles.islesextra.isles.IslesConstants;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;

public class IslesHudHandler {

    private static final Identifier background = new Identifier("happyhud:textures/font/assets/isles/transparent_bg.png");
    private static boolean timer = false;
    private static long freezeTime = 0;
    private static long startMillis = 0;

    public static void register() {
        SwitchedIslesServerCallback.EVENT.register(() -> {
            System.out.println("switched isles server with gui: " + IslesExtraClient.getOpenedGui());
            if (!timer && IslesExtraClient.getOpenedGui().equals(IslesConstants.Gui.BOSS_RUSH_DIFFICULTY_SELECTOR)) {
                timer = true;
                freezeTime = 0;
                startMillis = System.currentTimeMillis() + 5000;
            }
            else if (timer && !IslesExtraClient.getOpenedGui().equals(IslesConstants.Gui.BOSS_RUSH_DIFFICULTY_SELECTOR)) {
                timer = false;
            }
            return ActionResult.PASS;
        });
        LeftIslesCallback.EVENT.register(() -> {
            timer = false;
            return ActionResult.PASS;
        });
    }

    public static void freezeTime() {
        if (timer) freezeTime = System.currentTimeMillis();
    }

    public static void renderHud(DrawContext context, TextRenderer textRenderer) {
        if (!timer) return;
        context.drawTexture(background, (int) (context.getScaledWindowWidth()/2f - 65/2f), context.getScaledWindowHeight() - 90, 0, 0, 65, 11, 65, 11);
        long time = freezeTime == 0 ? System.currentTimeMillis() : freezeTime;
        context.drawCenteredTextWithShadow(textRenderer, formatMillisMMSS(time - startMillis), (int) (context.getScaledWindowWidth()/2f), context.getScaledWindowHeight() - 90 + 2, 0xFFFFFF);
    }

    private static String formatMillisMMSS(long millis) {
        if (millis < 0) return "00:00";
        int totalSeconds = (int) Math.ceil(millis / 1000.0);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds - seconds) / 60;
        return (minutes < 10 ? "0"+minutes : minutes) + ":" + (seconds < 10 ? "0"+seconds : seconds);
    }

}
