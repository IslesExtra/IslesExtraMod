package com.kyllian.islesextra.islesextra.gui.elements;

import com.kyllian.islesextra.islesextra.gui.GuiData;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import javax.annotation.Nullable;

public class TextureElement extends GuiElement {

    int textureX, textureY, offsetX, offsetY, sizeX, sizeY;
    Identifier texture;

    public TextureElement(int textureX, int textureY, int textureSizeX, int textureSizeY, int offsetX, int offsetY, @Nullable Identifier texture) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.textureX = textureX;
        this.textureY = textureY;
        this.sizeX = textureSizeX;
        this.sizeY = textureSizeY;
        this.texture = (texture==null) ? GuiData.texture : texture;
    }

    @Override
    public void render(MatrixStack matrices, int defaultX, int defaultY, int textureWidth, int textureHeight, int mouseX, int mouseY, boolean isMouseDown) {
        drawImage(matrices, defaultX + offsetX, defaultY + offsetY, textureX, textureY, sizeX, sizeY, sizeX, sizeY, textureWidth, textureHeight, texture);
    }

}
