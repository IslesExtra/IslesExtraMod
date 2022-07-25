package com.kyllian.islesextra.islesextra.gui;

import com.kyllian.islesextra.islesextra.IslesExtra;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector2f;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

import java.net.URI;
import java.net.URL;
import java.nio.file.*;
import java.util.*;

public class MapGui extends Screen {

    private float zoomLevel = 36;

    private final int defaultOffsetX = 214;
    private final int defaultOffsetY = 138;

    public void shiftZoomLevel(int shift) {
        if (zoomLevel + shift <= 8) {
            zoomLevel = 8;
        } else zoomLevel += shift;
    }

    public float offsetX = 0;
    public float offsetY = 0;

    public MapGui() {
        super(new LiteralText("MapGui"));

        URL url = IslesExtra.class.getResource("/assets/islesextra/tiles/");

        try {
            if (url != null) {
                URI uri = url.toURI();
                Path path;

                System.out.println(uri.getScheme());
                path = Paths.get(Objects.requireNonNull(IslesExtra.class.getResource("/assets/islesextra/tiles/")).toURI());

                Iterator<Path> it = Files.walk(path).iterator();

                while (it.hasNext()) {
                    Path p = it.next();
                    System.out.println(p.toString());
                    if (!p.toString().endsWith(".jpg")) continue;
                    String s = p.toString().split("tiles")[1].replace('\\', '.');
                    tiles.put(s.substring(1, s.length() - 4), new Identifier(IslesExtra.MOD_ID, p.toString().split("islesextra")[1].replace("\\", "/")));
                }
                System.out.println(tiles.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    HashMap<String, Identifier> tiles = new HashMap<>();

    @Override
    public void render(MatrixStack poseStack, int mouseX, int mouseY, float partialTicks) {
        MinecraftClient mc = MinecraftClient.getInstance();
        int width = mc.getWindow().getScaledWidth();
        int height = mc.getWindow().getScaledHeight();

        //System.out.println("Rendering");

        // First draw the background of the screen
        int bgX1 = 0;
        int bgY1 = 0;
        int bgX2 = width;
        int bgY2 = height;
        drawRectangle(poseStack, bgX1, bgY1, bgX2, bgY2, 0xFF000000);

        // Draw tiles
        for (String tile : tiles.keySet()) {
            if (tile.startsWith("6")) {
                Identifier image = tiles.get(tile);
                String[] values = tile.split("\\.");
                if (values.length<3) continue;
                int sizeX = Math.round(width / zoomLevel);
                int sizeY = Math.round(width / zoomLevel);
                int xPos = Math.round(Integer.parseInt(values[1]) * sizeX - (32 * sizeX) + offsetX + defaultOffsetX);
                int yPos = Math.round(Integer.parseInt(values[2]) * sizeY - (32 * sizeY) + offsetY + defaultOffsetY);
                if (xPos + sizeX > bgX1 && xPos < bgX2 && yPos + sizeY > bgY1 && yPos < bgY2) {
                    drawImage(poseStack, xPos, yPos, 0, 0f, 0f, sizeX, sizeY, sizeX, sizeY, image);
                }
            }
        }

        //System.out.println(mouseX + " / " + mouseY);

        // Call the super class' method to complete rendering
        super.render(poseStack, mouseX, mouseY, partialTicks);
    }

    public void getPosition(double iX, double iY, int button) {
        if (button == 0) {
            //System.out.println(mouseX + " / " + mouseY);
            MinecraftClient mc = MinecraftClient.getInstance();
            int width = mc.getWindow().getScaledWidth();
            int height = mc.getWindow().getScaledHeight();

            int sizeX = Math.round(width / zoomLevel);
            int sizeY = Math.round(width / zoomLevel);

            double x = (iX + 32 * sizeX - offsetX - defaultOffsetX) / sizeX;
            double y = (iY + 32 * sizeY - offsetY - defaultOffsetY) / sizeY;

            new Vector2f((float) x, (float) y);
        }
    }

    private void drawRectangle(MatrixStack ms, int x1, int y1, int x2, int y2, int color) {
        fill(ms, x1, y1, x2, y2, color);
    }

    private void drawText(MatrixStack ms, String s, int x, int y, int c, int fontSize) {
        drawCenteredText(ms, MinecraftClient.getInstance().textRenderer, s, x, y, c);
    }

    private void drawImage(MatrixStack ms, int xPos, int yPos, int blitOffset, float textureX, float textureY, int imgSizeX, int imgSizeY, int scaleX, int scaleY, Identifier image) {
        //Minecraft.getInstance().getTextureManager().bindForSetup(image);
        RenderSystem.setShaderTexture(0, image);
        drawTexture(ms, xPos, yPos, blitOffset, textureX, textureY, imgSizeX, imgSizeY, scaleX, scaleY);
    }

    @Override
    protected void init() {

        ScreenMouseEvents.allowMouseScroll(this).register(((screen, mouseX, mouseY, horizontalAmount, verticalAmount) -> {
            shiftZoomLevel(-Math.round(Math.round(verticalAmount)));
            return false;
        }));

        ScreenMouseEvents.allowMouseClick(this).register(((screen, mouseX, mouseY, button) -> {
            oldX = MinecraftClient.getInstance().mouse.getX();
            oldY = MinecraftClient.getInstance().mouse.getY();

            System.out.println(oldX + " - " + MinecraftClient.getInstance().mouse.getX());

            task = new DraggingTask();
            timer.scheduleAtFixedRate(task, 0, 10);

            return false;
        }));

        ScreenMouseEvents.allowMouseRelease(this).register(((screen, mouseX, mouseY, button) -> {
            if (task != null) task.cancel();
            return false;
        }));

    }

    double oldX;
    double oldY;

    private final Timer timer = new Timer();
    private DraggingTask task;

    private class DraggingTask extends TimerTask {
        public void run() {
            offsetX += MinecraftClient.getInstance().mouse.getX() - oldX;
            offsetY += MinecraftClient.getInstance().mouse.getY() - oldY;

            oldX = MinecraftClient.getInstance().mouse.getX();
            oldY = MinecraftClient.getInstance().mouse.getY();
        }
    }

}

