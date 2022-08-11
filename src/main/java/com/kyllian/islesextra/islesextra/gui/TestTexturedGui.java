package com.kyllian.islesextra.islesextra.gui;

import com.kyllian.islesextra.islesextra.entity.ModEntities;
import com.kyllian.islesextra.islesextra.entity.custom.queen_bee.QueenBeeEntity;
import com.kyllian.islesextra.islesextra.utility.RenderUtils;
import net.minecraft.client.util.math.MatrixStack;

public class TestTexturedGui extends TexturedGui {

    public TestTexturedGui(String textureName) {
        super(textureName);
    }

    QueenBeeEntity renderModel;
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        int entityX = width/2;
        int entityY = height/2;
        int entitySize = 20;
        int xDiff = entityX - mouseX;
        int yDiff = entityY - mouseY - entitySize;
        float yaw = (xDiff * 0.25) > 0 ? (float) Math.min((xDiff * 0.25), 45) : (float) Math.max((xDiff * 0.25), -45);
        float pitch = (yDiff * 0.25) > 0 ? (float) Math.min((yDiff * 0.25), 45) : (float) Math.max((yDiff * 0.25), -45);
        if (renderModel == null) renderModel = new QueenBeeEntity(ModEntities.QUEEN_BEE, client.world);
        RenderUtils.drawEntity(entityX, entityY, entitySize, renderModel, yaw, pitch);
    }

}
