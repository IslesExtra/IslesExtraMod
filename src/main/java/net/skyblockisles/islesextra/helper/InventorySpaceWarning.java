package net.skyblockisles.islesextra.helper;

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

public class InventorySpaceWarning {

    @Init
    public static void init() {
        HudElementRegistry.attachElementBefore(VanillaHudElements.CHAT, Identifier.of(IslesExtra.MOD_ID, "low_inventory_warning"), InventorySpaceWarning::noInventorySpace);
    }

    public static void noInventorySpace(DrawContext context, RenderTickCounter counter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (!IslesConfig.HANDLER.instance().lowInventoryEnable) return;
        if (client.player == null) return;
        if (client.player.getInventory().getEmptySlot() != -1) return;

        TextRenderer renderer = client.textRenderer;
        Text text = Text.translatable("text.islesextra.inventoryFull").formatted(Formatting.RED, Formatting.BOLD);

        float scale = 2.0f;
        int textWidth = renderer.getWidth(text);
        float x = (context.getScaledWindowWidth() / 2f) - (textWidth * scale / 2f);
        float y = (context.getScaledWindowHeight() / 2f) - (renderer.fontHeight * scale / 2f);

        context.getMatrices().pushMatrix();
        context.getMatrices().scale(scale, scale);

        context.drawTextWithShadow(renderer, text, (int)(x / scale), (int)(y / scale), 0xFFFFFFFF);
        context.getMatrices().popMatrix();
    }
}
