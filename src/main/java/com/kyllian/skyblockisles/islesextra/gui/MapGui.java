package com.kyllian.skyblockisles.islesextra.gui;

import com.kyllian.skyblockisles.islesextra.IslesExtra;
import com.kyllian.skyblockisles.islesextra.rendering.TrackerRenderer;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector2f;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

public class MapGui extends Screen {

    /* The 3D map screen, has many issues:
     * - takes up large size inside the mod because it stores the entire PNG of the render
     * - static, would have to be updated with each update to the map
     * - performance: it renders the entire map instead of parts of it, even when those parts are not visible
     * - Mouse dragging is bad due to a lack of drag events in Fabric
     */

    private final static Identifier MAP_IMAGE = new Identifier(IslesExtra.MOD_ID, "/images/map.png");
    private final static Identifier PLAYER_IMAGE = new Identifier(IslesExtra.MOD_ID, "/images/tracker.png");
    public double offsetX = 0;
    public double offsetY = 0;
    private final double defaultScale = 2176;

    public void shiftZoomLevel(int shift, double x, double y) {
        if (!(zoomLevel+shift<=2 || zoomLevel+shift>=56)) {
            Vector2f v1 = getPoint(x, y);
            zoomLevel += shift;
            Vector2f v2 = translatePoint(v1.getX(), v1.getY());

            offsetX -= v2.getX()-x;
            offsetY -= v2.getY()-y;
        }
    }
    private int zoomLevel = 36;

    public MapGui() {
        super(Text.literal("MapGui"));
    }

    public void onClick(int button, double x, double y) {
        if (button!=0) return;
        Vector2f point = getPoint(x,y);
        double a = point.getX();
        double b = point.getY();
        System.out.println(a + " / " + b);
        assert client != null && client.player != null;
        TrackerRenderer.getInstance().setTracker(new Vec3d(a, client.player.getY(), b));
    }

    private Vector2f getPoint(double x, double y) {
        assert client != null;
        int w  = client.getWindow().getScaledWidth();
        int h = client.getWindow().getScaledHeight();
        double size = (double) 24*w/zoomLevel;
        double f = size/defaultScale;
        double a = (x-(double)w/2-size/2-offsetX)/f + 1839;
        double b = (y-(double)h/2-size/2-offsetY)/f + 719;
        return new Vector2f((float) a, (float) b);
    }

    private Vector2f translatePoint(double a, double b) {
        assert client != null;
        int w  = client.getWindow().getScaledWidth();
        int h = client.getWindow().getScaledHeight();
        double size = (double) 24*w/zoomLevel;
        double f = size/defaultScale;
        double x = (a - 1839)*f + (float) w/2 +size/2 + offsetX;
        double y = (b - 719)*f + (float) h/2 +size/2 + offsetY;
        return new Vector2f((float) x, (float) y);
    }

    @Override
    public void render(MatrixStack ms, int mouseX, int mouseY, float pTicks) {
        assert client != null && client.player != null;

        int width = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight();

        drawBackdrop(ms, width, height);
        drawMap(ms, width, height);
        drawPlayer(ms, width, height, client.player.getX(), client.player.getZ());
    }

    private void drawBackdrop(MatrixStack ms, int width, int height) {
        fill(ms, 0,0, width, height, 0xFF000000);
    }

    public void drawMap(MatrixStack ms, int w, int h) {
        RenderSystem.setShaderTexture(0, MAP_IMAGE);

        int size = 24*w/zoomLevel;

        int xPos = (int) (w/2-size/2+offsetX);
        int yPos = (int) (h/2-size/2+offsetY);
        drawTexture(ms, xPos, yPos, 0, 0, 0, size, size, size, size);
    }

    private void drawPlayer(MatrixStack ms, int w, int h, double x, double y) {
        RenderSystem.setShaderTexture(0, PLAYER_IMAGE);

        int size = 10;

        Vector2f PlayerCoords = translatePoint(x, y);
        float xPos = PlayerCoords.getX() - (float) size/2;
        float yPos = PlayerCoords.getY() - (float) size/2;
        ms.push();

        ms.translate((xPos+(double)size/2), (yPos+(double)size/2), 0);
        assert client != null && client.player != null;
        ms.multiply(Vec3f.NEGATIVE_Z.getDegreesQuaternion(-client.player.getHeadYaw()));
        ms.translate(-(xPos+(float)size/2), -(yPos+(float)size/2), 0);

        drawTexture(ms, Math.round(xPos), Math.round(yPos), 1, 0, 0, size, size, size, size);

        ms.pop();
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        this.offsetX += deltaX;
        this.offsetY += deltaY;
        return this.getFocused() != null && this.isDragging() && button == 0 && this.getFocused().mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    protected void init() {

        ScreenMouseEvents.allowMouseScroll(this).register(((screen, mouseX, mouseY, horizontalAmount, verticalAmount) -> {
            shiftZoomLevel(-Math.round(Math.round(verticalAmount)), mouseX, mouseY);
            return false;
        }));

        ScreenMouseEvents.allowMouseClick(this).register(((screen, mouseX, mouseY, button) -> {
            this.onClick(button, mouseX, mouseY);
            return false;
        }));

    }

}

