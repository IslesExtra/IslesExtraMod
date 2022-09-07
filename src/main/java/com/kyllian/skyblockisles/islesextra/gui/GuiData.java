package com.kyllian.skyblockisles.islesextra.gui;

import com.kyllian.skyblockisles.islesextra.gui.elements.GuiElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class GuiData {

    // This class should probably be merged into the TexturedGui class

    public static Identifier texture = null;
    private final List<GuiElement> elements;
    public final int defaultX, defaultY, textureSizeX, textureSizeY;

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

    public void addElement(GuiElement element) { elements.add(element); }

    public List<GuiElement> getElements() { return elements; }

}
