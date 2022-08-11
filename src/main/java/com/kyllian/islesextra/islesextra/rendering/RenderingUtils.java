package com.kyllian.islesextra.islesextra.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.*;

import java.awt.*;

public class RenderingUtils {

    public static void renderAll(MatrixStack matrices, VertexConsumerProvider vertexConsumers, double cameraX, double cameraY, double cameraZ) {
        System.out.println("Rendering");

        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        Camera camera = client.gameRenderer.getCamera();

        assert player != null;

        Color color = new Color(3, 219, 252, 50);
        drawBox(0, 100, 0, 1, 102, 1, color);
        drawLine(0, 100, 0, 5, 105, 0, color);

    }

    public static void drawLine(double xStart, double yStart, double zStart, double xEnd, double yEnd, double zEnd, Color color) {
        Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
        if (camera.isReady()) {
            Vec3d vec3d = camera.getPos().negate();
            Box box = (new Box(new BlockPos(xStart, yStart, zStart), new BlockPos(xEnd, yEnd, zEnd))).offset(vec3d);
            drawLine(box, color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
        }
    }

    public static void drawLine(Box box, float r, float g, float b, float a) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();

        RenderSystem.enableBlend();

        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(box.minX,box.minY,box.minZ).color(r,g,b,a).next();
        bufferBuilder.vertex(box.maxX,box.maxY,box.maxZ).color(r,g,b,a).next();
        tessellator.draw();
    }

    public static void drawBox(double xStart, double yStart, double zStart, double xEnd, double yEnd, double zEnd, Color color) {
        Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
        if (camera.isReady()) {
            Vec3d vec3d = camera.getPos().negate();
            Box box = (new Box(new BlockPos(xStart, yStart, zStart), new BlockPos(xEnd, yEnd, zEnd))).offset(vec3d);
            DebugRenderer.drawBox(box, color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
        }
    }

}
