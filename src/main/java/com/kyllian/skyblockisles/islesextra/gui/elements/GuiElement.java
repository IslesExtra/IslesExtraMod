package com.kyllian.skyblockisles.islesextra.gui.elements;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public abstract class GuiElement {

    public abstract void render(MatrixStack matrices, int defaultX, int defaultY, int textureWidth, int textureHeight, int mouseX, int mouseY, boolean isMouseDown);

    /*
    Parameters:
    matrices - the matrix stack used for rendering
    x - the X coordinate of the rectangle
    y - the Y coordinate of the rectangle
    width - the width of the rectangle
    height - the height of the rectangle
    u - the left-most coordinate of the texture region
    v - the top-most coordinate of the texture region
    regionWidth - width of the selected region on the texture
    regionHeight - height of the selected region on the texture
    textureWidth - the width of the entire texture
    textureHeight - the height of the entire texture
     */
    public static void drawImage(MatrixStack matrixStack, int screenX, int screenY, int imgX, int imgY, int width, int height, int regionWidth, int regionHeight, int imgWidth, int imgHeight, Identifier texture) {
        RenderSystem.setShaderTexture(0, texture);
        // TODO: fix
        //Screen.drawTexture(matrixStack, screenX, screenY, width, height, imgX, imgY, regionWidth, regionHeight, imgWidth, imgHeight);
    }

}
