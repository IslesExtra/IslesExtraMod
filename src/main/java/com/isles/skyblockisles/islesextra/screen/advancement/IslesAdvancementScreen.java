package com.isles.skyblockisles.islesextra.screen.advancement;

import static com.isles.skyblockisles.islesextra.screen.advancement.IslesAdvancementConstants.ADVANCEMENTS_TEXT;
import static com.isles.skyblockisles.islesextra.screen.advancement.IslesAdvancementConstants.BORDER_WIDTH;
import static com.isles.skyblockisles.islesextra.screen.advancement.IslesAdvancementConstants.BOTTOM_BORDER_V;
import static com.isles.skyblockisles.islesextra.screen.advancement.IslesAdvancementConstants.BOTTOM_HEIGHT;
import static com.isles.skyblockisles.islesextra.screen.advancement.IslesAdvancementConstants.EMPTY_TEXT;
import static com.isles.skyblockisles.islesextra.screen.advancement.IslesAdvancementConstants.NO_OFFSET;
import static com.isles.skyblockisles.islesextra.screen.advancement.IslesAdvancementConstants.PANE_SIZE;
import static com.isles.skyblockisles.islesextra.screen.advancement.IslesAdvancementConstants.RESIZE_ICON_SIZE;
import static com.isles.skyblockisles.islesextra.screen.advancement.IslesAdvancementConstants.RESIZE_TEXTURE;
import static com.isles.skyblockisles.islesextra.screen.advancement.IslesAdvancementConstants.SAD_LABEL_TEXT;
import static com.isles.skyblockisles.islesextra.screen.advancement.IslesAdvancementConstants.TAB_HEIGHT;
import static com.isles.skyblockisles.islesextra.screen.advancement.IslesAdvancementConstants.TEXTURE_SIZE;
import static com.isles.skyblockisles.islesextra.screen.advancement.IslesAdvancementConstants.TITLE_OFFSET_X;
import static com.isles.skyblockisles.islesextra.screen.advancement.IslesAdvancementConstants.TITLE_OFFSET_Y;
import static com.isles.skyblockisles.islesextra.screen.advancement.IslesAdvancementConstants.TOP_HEIGHT;
import static com.isles.skyblockisles.islesextra.screen.advancement.IslesAdvancementConstants.VISUAL_TEXTURE_WIDTH;
import static com.isles.skyblockisles.islesextra.screen.advancement.IslesAdvancementConstants.WINDOW_HEIGHT;
import static com.isles.skyblockisles.islesextra.screen.advancement.IslesAdvancementConstants.WINDOW_TEXTURE;
import static com.isles.skyblockisles.islesextra.screen.advancement.IslesAdvancementConstants.WINDOW_WIDTH;

import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.advancement.PlacedAdvancement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.network.ClientAdvancementManager;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.network.packet.c2s.play.AdvancementTabC2SPacket;
import net.minecraft.util.Colors;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

@Environment(value = EnvType.CLIENT)
public class IslesAdvancementScreen extends Screen implements ClientAdvancementManager.Listener {

  private final MinecraftClient client;

  public double x;
  public double y;
  private double trueWidth = WINDOW_WIDTH;
  private double trueHeight = WINDOW_HEIGHT;

  private final ClientAdvancementManager advancementHandler;
  private final Map<AdvancementEntry, IslesAdvancementTab> tabs = Maps.newLinkedHashMap();

  @Nullable
  private IslesAdvancementTab selectedTab;

  // State for dragging
  private boolean movingTab = false;
  private boolean movingWindow = false;
  private boolean resizingWindow = false;

  public IslesAdvancementScreen(ClientAdvancementManager advancementHandler) {
    super(NarratorManager.EMPTY);
    this.advancementHandler = advancementHandler;
    this.client = MinecraftClient.getInstance();
  }

  @Override
  protected void init() {
    // Recalculate positions on init (handles window resize)
    this.x = (this.width - WINDOW_WIDTH) / 2.0;
    this.y = (this.height - WINDOW_HEIGHT) / 2.0;

    this.tabs.clear();
    this.selectedTab = null;
    this.advancementHandler.setListener(this);

    if (this.selectedTab == null && !this.tabs.isEmpty()) {
      IslesAdvancementTab advancementTab = this.tabs.values().iterator().next();
      this.advancementHandler.selectTab(advancementTab.getRoot().getAdvancementEntry(), true);
    } else {
      this.advancementHandler.selectTab(
          this.selectedTab == null ? null : this.selectedTab.getRoot().getAdvancementEntry(), true);
    }
  }

  @Override
  public void removed() {
    this.advancementHandler.setListener(null);
    if (this.client == null) {
      return;
    }
    ClientPlayNetworkHandler clientPlayNetworkHandler = this.client.getNetworkHandler();
    if (clientPlayNetworkHandler != null) {
      clientPlayNetworkHandler.sendPacket(AdvancementTabC2SPacket.close());
    }
  }

  @Override
  public boolean keyPressed(KeyInput input) {
    if (this.client == null) {
      return false;
    }

    if (this.client.options.advancementsKey.matchesKey(input)) {
      this.client.setScreen(null);
      this.client.mouse.lockCursor();
      return true;
    }

    if (input.isTab()) {
      cycleTabs(input.hasCtrl());
      return true;
    }

    if (handleScrolling(input)) {
      return true;
    }

    return super.keyPressed(input);
  }

  @Override
  public void render(DrawContext context, int mouseX, int mouseY, float delta) {
    if (this.client == null) {
      return;
    }

    final var click = new Click(mouseX, mouseY, null);

    this.renderDarkening(context);
    this.drawAdvancementTree(context, (int) x, (int) y);
    this.drawWindow(context, (int) x, (int) y);

    if (!resizingWindow && !movingWindow && !movingTab) {
      this.drawWidgetTooltip(context, click, (int) x, (int) y);
    }

    if (!(movingTab || movingWindow) && (isMouseOnResizeSquare(click) || resizingWindow)) {
      context.drawTexture(RenderPipelines.GUI_TEXTURED, RESIZE_TEXTURE,
          (int) x + WINDOW_WIDTH - RESIZE_ICON_SIZE, (int) y + WINDOW_HEIGHT - RESIZE_ICON_SIZE, 0,
          0, RESIZE_ICON_SIZE, RESIZE_ICON_SIZE, RESIZE_ICON_SIZE, RESIZE_ICON_SIZE);
    }
  }

  @Override
  public boolean mouseClicked(Click click, boolean bl) {
    final var button = click.button();

    if (button != 0) {
      return super.mouseClicked(click, bl);
    }

    // Check tabs
    for (IslesAdvancementTab advancementTab : this.tabs.values()) {
      if (advancementTab.isClickOnTab(x, y, click)) {
        this.advancementHandler.selectTab(advancementTab.getRoot().getAdvancementEntry(), true);
        break;
      }
    }

    // Check drag areas
    if (!resizingWindow && isMouseOnResizeSquare(click)) {
      resizingWindow = true;
    } else if (!movingTab && isMouseOnDisplay(click)) {
      movingTab = true;
    } else if (!movingWindow && !movingTab && isMouseOnWindow(click) && !isMouseOnDisplay(click)) {
      movingWindow = true;
    }

    return super.mouseClicked(click, bl);
  }

  @Override
  public boolean mouseDragged(Click click, double deltaX, double deltaY) {
    final var button = click.button();

    if (button != 0) {
      this.movingTab = false;
      this.movingWindow = false;
      this.resizingWindow = false;
      return false;
    }

    if (movingWindow) {
      x = MathHelper.clamp(x + deltaX, NO_OFFSET, this.width - WINDOW_WIDTH);
      y = MathHelper.clamp(y + deltaY, tabs.isEmpty() ? NO_OFFSET : TAB_HEIGHT,
          this.height - WINDOW_HEIGHT);
    } else if (movingTab && selectedTab != null) {
      selectedTab.move(deltaX, deltaY, WINDOW_WIDTH, WINDOW_HEIGHT);
    } else if (resizingWindow) {
      int tabCount = this.tabs.size();
      trueWidth = MathHelper.clamp(trueWidth + deltaX, Math.max(50, tabCount * 32), 1000);
      trueHeight = MathHelper.clamp(trueHeight + deltaY, Math.max(50, tabCount * 32), 1000);

      WINDOW_WIDTH = (int) trueWidth;
      WINDOW_HEIGHT = (int) trueHeight;
    }
    return movingWindow || movingTab || resizingWindow;
  }

  @Override
  public boolean mouseReleased(Click click) {
    if (movingTab || movingWindow || resizingWindow) {
      movingTab = false;
      movingWindow = false;
      resizingWindow = false;

      return true;
    }
    return super.mouseReleased(click);
  }

  @Override
  public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount,
      double verticalAmount) {
    if (this.selectedTab != null) {
      this.selectedTab.move(horizontalAmount * PANE_SIZE, verticalAmount * PANE_SIZE, WINDOW_WIDTH,
          WINDOW_HEIGHT);
      return true;
    }
    return false;
  }

  private void drawAdvancementTree(DrawContext context, int x, int y) {
    IslesAdvancementTab advancementTab = this.selectedTab;

    final int innerX = x + BORDER_WIDTH;
    final int innerY = y + TOP_HEIGHT;
    final int innerWidth = WINDOW_WIDTH - (2 * BORDER_WIDTH);
    final int innerHeight = WINDOW_HEIGHT - (TOP_HEIGHT + BOTTOM_HEIGHT);

    if (advancementTab == null) {
      final int centerX = innerX + innerWidth / 2;
      final int innerPadding = 10;

      context.fill(innerX, innerY, innerX + innerWidth, innerY + innerHeight, 0xFF000000); // Black

      context.drawCenteredTextWithShadow(this.textRenderer, EMPTY_TEXT, centerX,
          innerY + (2 * TAB_HEIGHT) - this.textRenderer.fontHeight / 2, Colors.WHITE);

      context.drawCenteredTextWithShadow(this.textRenderer, SAD_LABEL_TEXT, centerX,
          innerY + innerHeight - this.textRenderer.fontHeight - innerPadding, Colors.WHITE);
      return;
    }

    context.enableScissor(innerX, innerY, innerX + innerWidth, innerY + innerHeight);
    context.getMatrices().pushMatrix();
    advancementTab.render(context, innerX, innerY, innerWidth, innerHeight);
    context.getMatrices().popMatrix();
    context.disableScissor();
  }

  public void drawWindow(DrawContext context, int x, int y) {
    final int xPos = x + WINDOW_WIDTH - BORDER_WIDTH;
    final int yPos = y + WINDOW_HEIGHT - TOP_HEIGHT;
    final int rightEdge = VISUAL_TEXTURE_WIDTH - BORDER_WIDTH;

    final int innerWidth = WINDOW_WIDTH - (BORDER_WIDTH * 2);
    final int innerHeight = WINDOW_HEIGHT - (TOP_HEIGHT + BOTTOM_HEIGHT);
    final int texInnerWidth = rightEdge - BORDER_WIDTH;
    final int texInnerHeight = BOTTOM_BORDER_V - TOP_HEIGHT;

    // Top Edge
    draw(context, x + BORDER_WIDTH, y, BORDER_WIDTH, NO_OFFSET, innerWidth, TOP_HEIGHT,
        texInnerWidth, TOP_HEIGHT);
    // Bottom Edge
    draw(context, x + BORDER_WIDTH, yPos, BORDER_WIDTH, BOTTOM_BORDER_V, innerWidth, BOTTOM_HEIGHT,
        texInnerWidth, BOTTOM_HEIGHT);
    // Left Edge
    draw(context, x, y + TOP_HEIGHT, NO_OFFSET, TOP_HEIGHT, BORDER_WIDTH, innerHeight, BORDER_WIDTH,
        texInnerHeight);
    // Right Edge
    draw(context, xPos, y + TOP_HEIGHT, rightEdge, TOP_HEIGHT, BORDER_WIDTH, innerHeight,
        BORDER_WIDTH, texInnerHeight);

    // Top-Left
    draw(context, x, y, NO_OFFSET, NO_OFFSET, BORDER_WIDTH, TOP_HEIGHT);
    // Top-Right
    draw(context, xPos, y, rightEdge, NO_OFFSET, BORDER_WIDTH, TOP_HEIGHT);
    // Bottom-Left
    draw(context, x, yPos, NO_OFFSET, BOTTOM_BORDER_V, BORDER_WIDTH, BOTTOM_HEIGHT);
    // Bottom-Right
    draw(context, xPos, yPos, rightEdge, BOTTOM_BORDER_V, BORDER_WIDTH, BOTTOM_HEIGHT);

    if (this.tabs.size() > 1) {
      for (IslesAdvancementTab advancementTab : this.tabs.values()) {
        advancementTab.drawBackground(context, x, y, advancementTab == this.selectedTab);
      }
      for (IslesAdvancementTab advancementTab : this.tabs.values()) {
        advancementTab.drawIcon(context, x, y);
      }
    }
    context.drawText(this.textRenderer, ADVANCEMENTS_TEXT, x + TITLE_OFFSET_X, y + TITLE_OFFSET_Y,
        0x404040, false);
  }

  private void drawWidgetTooltip(DrawContext context, Click click, int x, int y) {
    if (this.selectedTab != null) {
      context.getMatrices().pushMatrix();
      this.selectedTab.drawWidgetTooltip(context, click, x, y, WINDOW_WIDTH, WINDOW_HEIGHT,
          BORDER_WIDTH, TOP_HEIGHT);
      context.getMatrices().popMatrix();
    }

    if (this.tabs.size() > 1) {
      for (IslesAdvancementTab advancementTab : this.tabs.values()) {
        if (advancementTab.isClickOnTab(x, y, click)) {
          context.drawTooltip(this.textRenderer, advancementTab.getTitle(), (int) click.comp_4798(),
              (int) click.comp_4799());
          break;
        }
      }
    }
  }

  public boolean isMouseOnDisplay(Click click) {
    double mouseX = click.comp_4798();
    double mouseY = click.comp_4799();
    return mouseX > x + BORDER_WIDTH && mouseX < x + BORDER_WIDTH + WINDOW_WIDTH - BORDER_WIDTH
        && mouseY > y + TOP_HEIGHT && mouseY < y + TOP_HEIGHT + WINDOW_HEIGHT - BOTTOM_HEIGHT;
  }

  public boolean isMouseOnWindow(Click click) {
    double mouseX = click.comp_4798();
    double mouseY = click.comp_4799();
    return mouseX > x && mouseX < x + WINDOW_WIDTH && mouseY > y && mouseY < y + WINDOW_HEIGHT;
  }

  public boolean isMouseOnResizeSquare(Click click) {
    double mouseX = click.comp_4798();
    double mouseY = click.comp_4799();
    return mouseX > x + WINDOW_WIDTH - BORDER_WIDTH && mouseX < x + WINDOW_WIDTH
        && mouseY > y + WINDOW_HEIGHT - BOTTOM_HEIGHT && mouseY < y + WINDOW_HEIGHT;
  }

  private void cycleTabs(boolean reverse) {
    List<AdvancementEntry> entries = this.tabs.keySet().stream().toList();
    if (entries.isEmpty()) {
      return;
    }

    int currentIndex =
        (selectedTab == null) ? -1 : entries.indexOf(selectedTab.getRoot().getAdvancementEntry());
    int direction = reverse ? -1 : 1;

    // The Math: (current + direction + size) % size wraps around correctly
    int nextIndex = (currentIndex + direction + entries.size()) % entries.size();
    this.selectTab(entries.get(nextIndex));
  }

  private boolean handleScrolling(KeyInput input) {
    final double speed = PANE_SIZE;
    double dx = NO_OFFSET;
    double dy = NO_OFFSET;

    // Map keys to movement vectors
    if (input.isRight()) {
      dx = -speed;
    } else if (input.isLeft()) {
      dx = speed;
    } else if (input.isDown()) {
      dy = -speed;
    } else if (input.isUp()) {
      dy = speed;
    } else {
      return false; // Not a scroll key
    }

    if (this.selectedTab == null) {
      return false;
    }

    this.selectedTab.move(dx, dy, WINDOW_WIDTH, WINDOW_HEIGHT);
    return true;
  }

  private void draw(DrawContext context, int x, int y, int xOffset, int yOffset, int width,
      int height) {
    context.drawTexture(RenderPipelines.GUI_TEXTURED, WINDOW_TEXTURE, x, y, xOffset, yOffset, width,
        height, TEXTURE_SIZE, TEXTURE_SIZE);
  }

  private void draw(DrawContext context, int x, int y, int xOffset, int yOffset, int width,
      int height, int texWidth, int texHeight) {
    context.drawTexture(RenderPipelines.GUI_TEXTURED, WINDOW_TEXTURE, x, y, xOffset, yOffset, width,
        height, texWidth, texHeight, TEXTURE_SIZE, TEXTURE_SIZE);
  }

  // Boilerplate for Interface
  @Override
  public void onRootAdded(PlacedAdvancement root) {
    IslesAdvancementTab advancementTab = IslesAdvancementTab.create(this.client, this,
        this.tabs.size(), root);
    if (advancementTab == null) {
      return;
    }
    this.tabs.put(root.getAdvancementEntry(), advancementTab);
  }

  @Override
  public void onRootRemoved(PlacedAdvancement root) {
  }

  @Override
  public void onDependentAdded(PlacedAdvancement dependent) {
    IslesAdvancementTab advancementTab = this.getTab(dependent);
    if (advancementTab != null) {
      advancementTab.addAdvancement(dependent);
    }
  }

  @Override
  public void onDependentRemoved(PlacedAdvancement dependent) {
  }

  @Override
  public void setProgress(PlacedAdvancement advancement, AdvancementProgress progress) {
    IslesAdvancementWidget widget = this.getAdvancementWidget(advancement);
    if (widget != null) {
      widget.setProgress(progress);
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
  public IslesAdvancementWidget getAdvancementWidget(PlacedAdvancement advancement) {
    IslesAdvancementTab tab = this.getTab(advancement);
    return tab == null ? null : tab.getWidget(advancement.getAdvancementEntry());
  }

  @Nullable
  private IslesAdvancementTab getTab(PlacedAdvancement advancement) {
    return this.tabs.get(advancement.getRoot().getAdvancementEntry());
  }

  @Override
  public boolean shouldPause() {
    return false;
  }
}