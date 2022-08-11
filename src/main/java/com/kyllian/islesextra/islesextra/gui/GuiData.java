package com.kyllian.islesextra.islesextra.gui;

import com.kyllian.islesextra.islesextra.gui.elements.ButtonElement;
import com.kyllian.islesextra.islesextra.gui.elements.GuiElement;
import com.kyllian.islesextra.islesextra.gui.elements.TextElement;
import com.kyllian.islesextra.islesextra.gui.elements.TextureElement;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class GuiData {

    public static Identifier texture = null;
    private final List<GuiElement> elements;
    private final int defaultX, defaultY, textureSizeX, textureSizeY;

    public GuiData(int width, int height, int offsetX, int offsetY, int sizeX, int sizeY, Identifier img) {
        MinecraftClient client = MinecraftClient.getInstance();
        elements = new ArrayList<>();
        defaultX = client.getWindow().getScaledWidth() / 2 - width / 2 + offsetX;
        defaultY = client.getWindow().getScaledHeight() / 2 - height / 2 + offsetY;
        textureSizeX = sizeX;
        textureSizeY = sizeY;
        texture = img;
    }

    public void renderElements(MatrixStack matrixStack, int mouseX, int mouseY, boolean isMouseDown) {
        elements.forEach((guiElement -> {
            guiElement.render(matrixStack, defaultX, defaultY, textureSizeX, textureSizeY, mouseX, mouseY, isMouseDown);
        }));
    }

    public void addText(TextElement text) {
        elements.add(text);
    }
    public void addTexture(TextureElement texure) { elements.add(texure); }
    public void addButton(ButtonElement button) { elements.add(button); }

}
