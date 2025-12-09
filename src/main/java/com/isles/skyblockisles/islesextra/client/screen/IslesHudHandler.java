package com.isles.skyblockisles.islesextra.client.screen;

import com.isles.skyblockisles.islesextra.event.IslesLocationChangedCallback;
import com.isles.skyblockisles.islesextra.event.LeftIslesCallback;
import com.isles.skyblockisles.islesextra.event.handler.EventHandler;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;

public final class IslesHudHandler implements EventHandler {

    private static final Identifier background = Identifier.of("happyhud:textures/font/assets/isles/transparent_bg.png");
    public static boolean inBoss = false;
    private static long freezeTime = 0;
    private static long startMillis = 0;

    public void register() {
        IslesLocationChangedCallback.EVENT.register(location -> {
            if (!location.isEmpty()) { // TODO; do additional boss check
                inBoss = true;
                freezeTime = 0;
                startMillis = System.currentTimeMillis() + 5000;
            }
            else if (inBoss) inBoss = false;
            return ActionResult.PASS;
        });
        LeftIslesCallback.EVENT.register(() -> {
            inBoss = false;
            return ActionResult.PASS;
        });
        ClientEntityEvents.ENTITY_LOAD.register(((entity, world) -> {
            if (entity instanceof ItemEntity) freezeTime();
        }));
    }

    public static void freezeTime() {
        if (inBoss && freezeTime == 0) freezeTime = System.currentTimeMillis();
    }

    public static void renderHud(DrawContext context, TextRenderer textRenderer) {
        if (!inBoss) return;
        context.drawTexture(RenderPipelines.GUI_TEXTURED, background, (int) (context.getScaledWindowWidth()/2f - 65/2f), context.getScaledWindowHeight() - 90, 0, 0, 65, 11, 65, 11);
        long time = freezeTime == 0 ? System.currentTimeMillis() : freezeTime;
        int color = freezeTime == 0 ? 0xFFFFFF : 0x7dde2f;
        context.drawCenteredTextWithShadow(textRenderer, formatMillisMMSS(time - startMillis), (int) (context.getScaledWindowWidth()/2f), context.getScaledWindowHeight() - 90 + 2, color);
    }

    private static String formatMillisMMSS(long millis) {
        if (millis < 0) return "00:00";
        int totalSeconds = (int) Math.ceil(millis / 1000.0);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds - seconds) / 60;
        return (minutes < 10 ? "0"+minutes : minutes) + ":" + (seconds < 10 ? "0"+seconds : seconds);
    }

}
