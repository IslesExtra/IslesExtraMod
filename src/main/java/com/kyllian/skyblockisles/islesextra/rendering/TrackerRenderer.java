package com.kyllian.skyblockisles.islesextra.rendering;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.awt.*;

public class TrackerRenderer {

    private static TrackerRenderer instance;
    public static TrackerRenderer getInstance() { return instance == null ? new TrackerRenderer() : instance; }
    public TrackerRenderer() { instance = this; }

    private static Vec3d tracker;
    public void setTracker(Vec3d dest) { tracker = dest; }

    public void render() {
        if (tracker == null) return;
        MinecraftClient client = MinecraftClient.getInstance();
        assert client.player != null;
        double distance = client.player.getPos().distanceTo(tracker);
        DebugRenderer.drawBox(new BlockPos(tracker.x, tracker.y, tracker.z), 0f , 69/255f, 217/255f, 209/255f, .5f);
        DebugRenderer.drawString("§7Tracker - §f" + Math.round(distance) +"§7m", tracker.x, tracker.y + 1, tracker.z, new Color(255,255,255).getRGB(), (float) (0.01 * distance), true, 0.0F, true);
    }

}