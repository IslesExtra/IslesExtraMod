package com.kyllian.skyblockisles.islesextra.mixin;

import com.kyllian.skyblockisles.islesextra.client.ClientData;
import com.kyllian.skyblockisles.islesextra.utility.Dialogue;
import net.minecraft.client.MinecraftClient;
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
    public void render(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.options.hudHidden) return;

        Dialogue.render(matrices);

        if (ClientData.pickedUpItems.size()==0) return;
        ItemRenderer itemRenderer = client.getItemRenderer();
        int offset = 0;
        List<ClientData.PickedUpItem> pickedUpItems = new ArrayList<>(ClientData.pickedUpItems);
        for (ClientData.PickedUpItem item : pickedUpItems) {

            // Hard-coded values for top left layout.

            Text text = Text.literal("§7" + item.count + "x ").append(item.name);
            int width = 25 + 4 + MinecraftClient.getInstance().textRenderer.getWidth(text) + 2;
            drawRect(matrices, 5, 5 + offset, width, 24, new Color(77, 79, 82, 150).getRGB());
            itemRenderer.renderGuiItemIcon(new ItemStack(item.item), 9, 5 + offset + 4);
            MinecraftClient.getInstance().textRenderer.draw(matrices, text, 30, 5 + offset + 8, 0xFFFFFF);
            offset += 24;
        }

    }

    private static void drawRect(MatrixStack matrixStack, int x, int y, int width, int height, int color) {
        Screen.fill(matrixStack, x, y, x + width, y + height, color);
    }

}
