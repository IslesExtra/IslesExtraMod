package com.isles.skyblockisles.islesextra.client.screen.advancement;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.advancement.PlacedAdvancement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientAdvancementManager;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.network.packet.c2s.play.AdvancementTabC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.Map;

@Environment(value=EnvType.CLIENT)
public class CustomAdvancementsScreen extends Screen implements ClientAdvancementManager.Listener {
    private final MinecraftClient client;
    private static final Identifier WINDOW_TEXTURE = new Identifier("textures/gui/advancements/window.png");
    public static int WINDOW_WIDTH = 340; // vanilla value: 252
    private double true_width = WINDOW_WIDTH; // represents the window's width as a double for smoother resizing
    public static int WINDOW_HEIGHT = 224; // vanilla value: 140
    private double true_height = WINDOW_HEIGHT; // represents the window's height as a double for smoother resizing
    public static double x = (MinecraftClient.getInstance().getWindow().getScaledWidth() - WINDOW_WIDTH) / 2f; // this is fucking ugly
    public static double y = (MinecraftClient.getInstance().getWindow().getScaledHeight() - WINDOW_HEIGHT) / 2f; // this is ugly too

    // constants:
    private static final int PAGE_OFFSET_X = 9;
    private static final int PAGE_OFFSET_Y = 18;
    private static final int TITLE_OFFSET_X = 8;
    private static final int TITLE_OFFSET_Y = 6;
    private static final Text SAD_LABEL_TEXT = Text.translatable("advancements.sad_label");
    private static final Text EMPTY_TEXT = Text.translatable("advancements.empty");
    private static final Text ADVANCEMENTS_TEXT = Text.translatable("gui.advancements");
    private final ClientAdvancementManager advancementHandler;
    private final Map<AdvancementEntry, CustomAdvancementTab> tabs = Maps.newLinkedHashMap();
    @Nullable
    private CustomAdvancementTab selectedTab;

    public CustomAdvancementsScreen(ClientAdvancementManager advancementHandler) {
        super(NarratorManager.EMPTY);
        this.advancementHandler = advancementHandler;
        client = MinecraftClient.getInstance();
    }

    @Override
    protected void init() {
        this.tabs.clear();
        this.selectedTab = null;
        this.advancementHandler.setListener(this);
        if (this.selectedTab == null && !this.tabs.isEmpty()) {
            CustomAdvancementTab advancementTab = this.tabs.values().iterator().next();
            this.advancementHandler.selectTab(advancementTab.getRoot().getAdvancementEntry(), true);
        } else {
            this.advancementHandler.selectTab(this.selectedTab == null ? null : this.selectedTab.getRoot().getAdvancementEntry(), true);
        }
    }

    @Override
    public void removed() {
        this.advancementHandler.setListener(null);
        if (this.client == null) return;
        ClientPlayNetworkHandler clientPlayNetworkHandler = this.client.getNetworkHandler();
        if (clientPlayNetworkHandler != null) {
            clientPlayNetworkHandler.sendPacket(AdvancementTabC2SPacket.close());
        }
    }

    public boolean isMouseOnDisplay(double mouseX, double mouseY) {
        return mouseX > x + PAGE_OFFSET_X && mouseX < x + PAGE_OFFSET_X + (WINDOW_WIDTH - 2*9) && mouseY > y + PAGE_OFFSET_Y && mouseY < y + PAGE_OFFSET_Y + (WINDOW_HEIGHT - 3*9);
    }

    public boolean isMouseOnWindow(double mouseX, double mouseY) {
        return mouseX > x && mouseX < x + WINDOW_WIDTH && mouseY > y && mouseY < y + WINDOW_HEIGHT;
    }

    public boolean isMouseOnResizeSquare(double mouseX, double mouseY) {
        return mouseX > x + WINDOW_WIDTH - 9 && mouseX < x + WINDOW_WIDTH && mouseY > y + WINDOW_HEIGHT - 9 && mouseY < y + WINDOW_HEIGHT;
    }

    // 258 - TAB: cycle tabs (and control for backwards because why the fuck not)
    // 262 - RIGHT KEY: pan right
    // 263 - LEFT KEY: pan left
    // 264 - DOWN KEY: pan down
    // 265 - UP KEY: pan up
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.client == null) return false;
        if (this.client.options.advancementsKey.matchesKey(keyCode, scanCode)) {
            this.client.setScreen(null);
            this.client.mouse.lockCursor();
            return true;
        }
        else if (keyCode == 258) {
            AdvancementEntry current = null;
            for (AdvancementEntry entry : this.tabs.keySet()) {
                if (this.tabs.get(entry).equals(this.selectedTab)) current = entry;
            }
            if (current != null) {
                List<AdvancementEntry> entries = this.tabs.keySet().stream().toList();
                this.selectTab(entries.get((entries.indexOf(current) + (modifiers == GLFW.GLFW_MOD_CONTROL ? -1 : 1) + entries.size()) % entries.size())); // (current index +/- 1 + size)%size will always result in a valid index
            }
        }
        else if ((keyCode == 262 || client.options.rightKey.matchesKey(keyCode, scanCode)) && selectedTab != null) selectedTab.move(-16f, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        else if ((keyCode == 263 || client.options.leftKey.matchesKey(keyCode, scanCode)) && selectedTab != null) selectedTab.move(16f, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        else if ((keyCode == 264 || client.options.backKey.matchesKey(keyCode, scanCode)) && selectedTab != null) selectedTab.move(0, -16f, WINDOW_WIDTH, WINDOW_HEIGHT);
        else if ((keyCode == 265 || client.options.forwardKey.matchesKey(keyCode, scanCode)) && selectedTab != null) selectedTab.move(0, 16f, WINDOW_WIDTH, WINDOW_HEIGHT);
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (this.client == null) return;
        this.renderBackground(context, mouseX, mouseY, delta);
        this.drawAdvancementTree(context, mouseX, mouseY, (int) x, (int) y);
        this.drawWindow(context, (int) x, (int) y);
        if (!resizingWindow && !movingWindow && !movingTab) this.drawWidgetTooltip(context, mouseX, mouseY, (int) x, (int) y);
        if (isMouseOnResizeSquare(mouseX, mouseY) || resizingWindow) {
            context.fill((int) x + WINDOW_WIDTH - 9, (int) y + WINDOW_HEIGHT - 9, (int) x + WINDOW_WIDTH, (int) y + WINDOW_HEIGHT, MathHelper.floor(155.0f) << 24);
        }
    }

    private boolean movingTab = false, movingWindow = false, resizingWindow = false;
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            for (CustomAdvancementTab advancementTab : this.tabs.values()) {
                if (!advancementTab.isClickOnTab(x, y, mouseX, mouseY)) continue;
                this.advancementHandler.selectTab(advancementTab.getRoot().getAdvancementEntry(), true);
                break;
            }

            boolean startDrag = true;
            if (!resizingWindow && isMouseOnResizeSquare(mouseX, mouseY)) {
                resizingWindow = true;
            }
            else if (!movingTab && isMouseOnDisplay(mouseX, mouseY)) {
                movingTab = true;
            }
            else if (!movingWindow && !movingTab && isMouseOnWindow(mouseX, mouseY) && !isMouseOnDisplay(mouseX, mouseY)) {
                movingWindow = true;
            }
            else startDrag = false;
            if (startDrag) InputUtil.setCursorParameters(client.getWindow().getHandle(), InputUtil.GLFW_CURSOR_DISABLED, client.mouse.getX(), client.mouse.getY());
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (button != 0) {
            this.movingTab = false;
            this.movingWindow = false;
            this.resizingWindow = false;
            return false;
        }

        // moving of window and contents
        if (movingWindow) {
            x = MathHelper.clamp(x + deltaX, 0, this.width - WINDOW_WIDTH);
            y = MathHelper.clamp(y + deltaY, tabs.isEmpty() ? 0 : 28, this.height - WINDOW_HEIGHT);
        }
        else if (movingTab && selectedTab != null) {
            selectedTab.move(deltaX, deltaY, WINDOW_WIDTH, WINDOW_HEIGHT);
        }
        else if (resizingWindow) {
            int tabCount = this.tabs.size();
            true_width = MathHelper.clamp(true_width + deltaX, Math.max(50, tabCount*32), 1000);
            true_height = MathHelper.clamp(true_height + deltaY, Math.max(50, tabCount*32), 1000);

            WINDOW_WIDTH = (int) true_width;
            WINDOW_HEIGHT = (int) true_height;
        }
        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (movingTab || movingWindow || resizingWindow) {
            long ptr = client.getWindow().getHandle();
            GLFW.glfwSetInputMode(ptr, GLFW.GLFW_CURSOR, InputUtil.GLFW_CURSOR_NORMAL);
            if (movingWindow) GLFW.glfwSetCursorPos(client.getWindow().getHandle(),
                    (x+true_width/2f)/width * client.getWindow().getWidth(),
                    (y/height) * client.getWindow().getHeight() + 18);
            else if (resizingWindow) GLFW.glfwSetCursorPos(ptr,
                    ((x+true_width)/width) * client.getWindow().getWidth() - 18,
                    ((y+true_height)/height) * client.getWindow().getHeight() - 18);

            movingTab = false;
            movingWindow = false;
            resizingWindow = false;
        }

        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (this.selectedTab != null) {
            this.selectedTab.move(horizontalAmount * 16.0, verticalAmount * 16.0, WINDOW_WIDTH, WINDOW_HEIGHT);
            return true;
        }
        return false;
    }

    private void drawAdvancementTree(DrawContext context, int mouseX, int mouseY, int x, int y) {
        CustomAdvancementTab advancementTab = this.selectedTab;
        if (advancementTab == null) {
            context.fill(x + PAGE_OFFSET_X, y + PAGE_OFFSET_Y, x + PAGE_OFFSET_X + (WINDOW_WIDTH - 2*9), y + PAGE_OFFSET_Y + (WINDOW_HEIGHT - 3*9), -16777216);
            int i = x + PAGE_OFFSET_X + 117;
            context.drawCenteredTextWithShadow(this.textRenderer, EMPTY_TEXT, i, y + PAGE_OFFSET_Y + 56 - this.textRenderer.fontHeight / 2, Colors.WHITE);
            context.drawCenteredTextWithShadow(this.textRenderer, SAD_LABEL_TEXT, i, y + PAGE_OFFSET_Y + (WINDOW_HEIGHT - 3*9) - this.textRenderer.fontHeight, Colors.WHITE);
            return;
        }
        advancementTab.render(context, x + PAGE_OFFSET_X, y + PAGE_OFFSET_Y, WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    public void drawWindow(DrawContext context, int x, int y) {
        RenderSystem.enableBlend();
        //context.drawTexture(WINDOW_TEXTURE, x, y, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

        /*
        note: holy magic number fest what in the jesus christ's name have I done
        I kinda forgot about using the constants instead of magnums but I'm too lazy to change it now, in general:
        - 4 is the empty space mojang put there for the fun
        - 9 is the size of the border
        - 18 is the size of the border because of course the top border has to be bigger
        - 256 is texture size
        - 0 is zero
        - 144 is the height of the thing, but NOT the height of the other thing
         */

        // Draw top left corner
        context.drawTexture(WINDOW_TEXTURE, x, y, 0, 0, 9, 18);

        // Draw top right corner
        context.drawTexture(WINDOW_TEXTURE, x + WINDOW_WIDTH - 9, y, 9, 18, 256 - 4 - 9, 0, 9, 18, 256, 256);

        // Draw bottom left corner
        context.drawTexture(WINDOW_TEXTURE, x, y + WINDOW_HEIGHT - 9, 9, 9, 0, 144 - 9 - 4, 9, 9, 256, 256);

        // Draw bottom right corner
        context.drawTexture(WINDOW_TEXTURE, x + WINDOW_WIDTH - 9, y + WINDOW_HEIGHT - 9, 9, 9, 256 - 4 - 9, 144 - 9 - 4, 9, 9, 256, 256);

        // Draw top edge
        context.drawTexture(WINDOW_TEXTURE, x + 9, y, WINDOW_WIDTH - 9 * 2, 18, 9, 0, 256 - 4 - 9 * 2, 18, 256, 256);

        // Draw bottom edge
        context.drawTexture(WINDOW_TEXTURE, x + 9, y + WINDOW_HEIGHT - 9, WINDOW_WIDTH - 9 * 2, 9, 9, 144 - 9 - 4, 256 - 4 - 9 * 2, 9, 256, 256);

        // Draw left edge
        context.drawTexture(WINDOW_TEXTURE, x, y + 18, 9, WINDOW_HEIGHT - 18 - 9, 0, 18, 9, 144 - 18 - 9 - 4, 256, 256);

        // Draw right edge
        context.drawTexture(WINDOW_TEXTURE, x + WINDOW_WIDTH - 9, y + 18, 9, WINDOW_HEIGHT - 18 - 9, 256 - 9 - 4, 18, 9, 144 - 18 - 9 - 4, 256, 256);

        if (this.tabs.size() > 1) {
            for (CustomAdvancementTab advancementTab : this.tabs.values()) {
                advancementTab.drawBackground(context, x, y, advancementTab == this.selectedTab);
            }
            for (CustomAdvancementTab advancementTab : this.tabs.values()) {
                advancementTab.drawIcon(context, x, y);
            }
        }
        context.drawText(this.textRenderer, ADVANCEMENTS_TEXT, x + TITLE_OFFSET_X, y + TITLE_OFFSET_Y, 0x404040, false);
    }

    private void drawWidgetTooltip(DrawContext context, int mouseX, int mouseY, int x, int y) {
        if (this.selectedTab != null) {
            context.getMatrices().push();
            context.getMatrices().translate(x + PAGE_OFFSET_X, y + PAGE_OFFSET_Y, 400.0f);
            RenderSystem.enableDepthTest();
            this.selectedTab.drawWidgetTooltip(context, mouseX - x - PAGE_OFFSET_X, mouseY - y - PAGE_OFFSET_Y, x, y, WINDOW_WIDTH, WINDOW_HEIGHT);
            RenderSystem.disableDepthTest();
            context.getMatrices().pop();
        }
        if (this.tabs.size() > 1) {
            for (CustomAdvancementTab advancementTab : this.tabs.values()) {
                if (!advancementTab.isClickOnTab(x, y, mouseX, mouseY)) continue;
                context.drawTooltip(this.textRenderer, advancementTab.getTitle(), mouseX, mouseY);
            }
        }
    }

    @Override
    public void onRootAdded(PlacedAdvancement root) {
        CustomAdvancementTab advancementTab = CustomAdvancementTab.create(this.client, this, this.tabs.size(), root);
        if (advancementTab == null) return;
        this.tabs.put(root.getAdvancementEntry(), advancementTab);
    }

    @Override
    public void onRootRemoved(PlacedAdvancement root) {
    }

    @Override
    public void onDependentAdded(PlacedAdvancement dependent) {
        CustomAdvancementTab advancementTab = this.getTab(dependent);
        if (advancementTab != null) {
            advancementTab.addAdvancement(dependent);
        }
    }

    @Override
    public void onDependentRemoved(PlacedAdvancement dependent) {
    }

    @Override
    public void setProgress(PlacedAdvancement advancement, AdvancementProgress progress) {
        CustomAdvancementWidget advancementWidget = this.getAdvancementWidget(advancement);
        if (advancementWidget != null) {
            advancementWidget.setProgress(progress);
        }
    }

    @Override
    public void selectTab(@Nullable AdvancementEntry advancement) {
        this.selectedTab = this.tabs.get(advancement);
    }

    @Override
    public void onClear() {
        this.tabs.clear();
        this.selectedTab = null;
    }

    @Nullable
    public CustomAdvancementWidget getAdvancementWidget(PlacedAdvancement advancement) {
        CustomAdvancementTab advancementTab = this.getTab(advancement);
        return advancementTab == null ? null : advancementTab.getWidget(advancement.getAdvancementEntry());
    }

    @Nullable
    private CustomAdvancementTab getTab(PlacedAdvancement advancement) {
        PlacedAdvancement placedAdvancement = advancement.getRoot();
        return this.tabs.get(placedAdvancement.getAdvancementEntry());
    }


    // TODO; remove this when done
    @Override
    public boolean shouldPause() {
        return false;
    }
}

