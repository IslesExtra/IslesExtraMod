package com.kyllian.islesextra.islesextra.gui;

import com.kyllian.islesextra.islesextra.IslesExtra;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Iterator;

public class MobScreen extends Screen {


    public MobScreen() {
        super(new LiteralText("Mob Screen"));
    }

    Identifier closeButton = new Identifier(IslesExtra.MOD_ID, "textures/gui/closebutton.png");
    Identifier closeButtonHover = new Identifier(IslesExtra.MOD_ID, "textures/gui/closebuttonhighlighted.png");

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        MinecraftClient client = MinecraftClient.getInstance();
        drawImage(matrixStack, 0, 0, 0, 0, 0, 1920, 1080, client.getWindow().getScaledHeight(), client.getWindow().getScaledWidth(), new Identifier("islesextra:textures/gui/queen_bee.png"));
        // Check if mouse is over close button
        if (mouseX > this.width - 25 && mouseX < this.width - 5 && mouseY > 5 && mouseY < 25) {
            drawImage(matrixStack, this.width - 25, 5, 0, 0, 0, 20, 20, 20, 20, closeButtonHover);
        } else {
            drawImage(matrixStack, this.width - 25, 5, 0, 0, 0, 20, 20, 20, 20, closeButton);
        }
    }

    @Override
    public void init() {
        ScreenMouseEvents.allowMouseClick(this).register(((screen, mouseX, mouseY, button) -> {
            if (mouseX > this.width - 25 && mouseX < this.width - 5 && mouseY > 5 && mouseY < 25) {
                MinecraftClient.getInstance().setScreen(null);
            }
            return false;
        }));
    }

    private void drawImage(MatrixStack ms, int xPos, int yPos, int blitOffset, float textureX, float textureY, int imgSizeX, int imgSizeY, int scaleX, int scaleY, Identifier image) {
        //Minecraft.getInstance().getTextureManager().bindForSetup(image);
        RenderSystem.setShaderTexture(0, image);
        drawTexture(ms, xPos, yPos, blitOffset, textureX, textureY, imgSizeX, imgSizeY, scaleX, scaleY);
    }
}
