package com.kyllian.skyblockisles.islesextra.mixin;

import com.kyllian.skyblockisles.islesextra.client.PickupLogger;
import com.kyllian.skyblockisles.islesextra.utility.Dialogue;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Mixin(InGameHud.class)
abstract class OverlayGui {

    @Inject(method = "render", at = @At("TAIL"))
    public void render(DrawContext context, float tickDelta, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.options.hudHidden) return;

        // TODO: fix
        //Dialogue.render(context.getMatrices());

        if (PickupLogger.pickedUpItems.size()==0) return;
        ItemRenderer itemRenderer = client.getItemRenderer();
        int offset = 0;
        List<PickupLogger.PickedUpItem> pickedUpItems = new ArrayList<>(PickupLogger.pickedUpItems);
        for (PickupLogger.PickedUpItem item : pickedUpItems) {

            // Hard-coded values for top left layout.

            Text text = Text.literal("§7" + item.count + "x ").append(item.name);
            int width = 25 + 4 + MinecraftClient.getInstance().textRenderer.getWidth(text) + 2;
            drawRect(context, 5, 5 + offset, width, 24, new Color(77, 79, 82, 150).getRGB());
            context.drawItem(new ItemStack(item.item), 9, 5 + offset + 4);
            context.drawText(client.textRenderer, text, 30, 5 + offset + 8, 0xFFFFFF, false);
            offset += 24;
        }

    }

    private static void drawRect(DrawContext context, int x, int y, int width, int height, int color) {
        context.fill(x, y, x + width, y + height, color);
    }

}
