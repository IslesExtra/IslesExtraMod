package net.skyblockisles.islesextra.constants;

import org.joml.Matrix3x2fStack;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.skyblockisles.islesextra.IslesExtra;
import net.skyblockisles.islesextra.annotations.Init;

public class Renderer {
    private static Entity target;
    private static int color = 0xFFFF0000;
    private static final MinecraftClient client = MinecraftClient.getInstance();

    @Init
    public static void init() {
        HudElementRegistry.addLast(Identifier.of(IslesExtra.MOD_ID, "last_element"), Renderer::traceToEntity);
    }

    private static void traceToEntity(DrawContext context, RenderTickCounter counter) {
        if (target == null || target.isRemoved()) return;

        float delta = counter.getTickProgress(true);
        Vec3d pos = target.getLerpedPos(delta).add(0, target.getEyeHeight(target.getPose()), 0);
        Vec3d projected = client.gameRenderer.project(pos);

        float screenWidth = client.getWindow().getScaledWidth();
        float screenHeight = client.getWindow().getScaledHeight();
        float centerX = screenWidth / 2f;
        float centerY = screenHeight / 2f;

        float projectedX = (float) ((projected.x + 1.0) / 2.0 * screenWidth);
        float projectedY = (float) ((1.0 - projected.y) / 2.0 * screenHeight);

        float dx = projectedX - centerX;
        float dy = projectedY - centerY;

        if (projected.z >= 1.0) {
            dx = -dx;
            dy = -dy;
        }

        float length = (float) Math.sqrt(dx * dx + dy * dy);
        float angle = (float) Math.atan2(dy, dx);

        float margin = 2.0f;
        float maxX = centerX - margin;
        float maxY = centerY - margin;

        float scaleX = Math.abs(maxX / dx);
        float scaleY = Math.abs(maxY / dy);
        float scale = Math.min(1.0f, Math.min(scaleX, scaleY));

        float finalLength = length * scale;

        Matrix3x2fStack matrices = context.getMatrices();
        matrices.pushMatrix();
        matrices.translate(centerX, centerY);
        matrices.rotate(angle);

        context.fill(0, 0, (int) finalLength, 1, color);

        matrices.popMatrix();
    }

    public static void setTarget(Entity entity) {
        target = entity;
    }

    public static void setColor(int colorCode) {
        color = colorCode;
    }
}
