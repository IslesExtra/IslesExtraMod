package com.kyllian.skyblockisles.islesextra.gui;

import com.kyllian.skyblockisles.islesextra.entity.IslesEntities;
import com.kyllian.skyblockisles.islesextra.entity.custom.queen_bee.QueenBeeEntity;
import com.kyllian.skyblockisles.islesextra.utility.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;

public class Bestiary extends TexturedGui {

    // Attempt at recreating that bestiary thing I made with components instead of rendering the thing as one texture.

    public Bestiary() {
        super("bestiary");
        model = new QueenBeeEntity(IslesEntities.QUEEN_BEE, MinecraftClient.getInstance().world);
    }

    final Color background = new Color(13, 13, 18);
    final int yellow = new Color(243, 209, 20).getRGB();
    final QueenBeeEntity model;

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {

        // Dark-colored background, no gradient
        Screen.fill(matrices, 0, 0, width, height, background.getRGB());

        super.render(matrices, mouseX, mouseY, delta);

        RenderUtils.drawEntity(50, height/2, 25, model, 25, 10);

        //matrices.scale(2, 2, 2);
        Screen.drawCenteredText(matrices, textRenderer, "Queen Bee", 50, 200, yellow);

        Screen.drawCenteredText(matrices, textRenderer, "The Queen Bee is stingy.", 50, 200, 0xFFFFFF);
    }
}
