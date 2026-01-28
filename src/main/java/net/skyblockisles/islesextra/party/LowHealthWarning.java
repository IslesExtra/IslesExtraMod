package net.skyblockisles.islesextra.party;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.skyblockisles.islesextra.IslesExtra;
import net.skyblockisles.islesextra.annotations.Init;
import net.skyblockisles.islesextra.config.IslesConfig;

import java.awt.*;

public class LowHealthWarning {
    @Init
    public static void init() {
        HudElementRegistry.attachElementBefore(VanillaHudElements.CHAT, Identifier.of(IslesExtra.MOD_ID, "low_health_display"), LowHealthWarning::lowHealthDisplay);
    }

    public static void lowHealthDisplay(DrawContext context, RenderTickCounter counter) {
        if (IslesParty.getMembers().isEmpty()) return;
        if (!IslesConfig.HANDLER.instance().partyLowHealthEnable) return;

        TextRenderer renderer = MinecraftClient.getInstance().textRenderer;

        // Use array to be able to access within lambda
        int[] y = { context.getScaledWindowHeight() / 2 };
        int x = 5;

        IslesParty.getMemberEntities().stream()
            .filter(playerEntity -> playerEntity.getHealth() < (playerEntity.getMaxHealth() * IslesConfig.HANDLER.instance().partyLowHealthThreshold))
            .forEach(playerEntity -> {
                Text text = Text.translatable("text.islesextra.party.lowhealth", playerEntity.getName().getString()).formatted(Formatting.RED, Formatting.BOLD);
                context.drawTextWithShadow(renderer, text, x, y[0], 0xFFFFFFFF);
                y[0] += renderer.fontHeight + 2;
            });
    }
}
