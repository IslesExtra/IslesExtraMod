package com.isles.skyblockisles.islesextra.client.screen.advancement;

import static com.isles.skyblockisles.islesextra.client.screen.advancement.AdvancementScreenConstants.BORDER_WIDTH;
import static com.isles.skyblockisles.islesextra.client.screen.advancement.AdvancementScreenConstants.BOTTOM_HEIGHT;
import static com.isles.skyblockisles.islesextra.client.screen.advancement.AdvancementScreenConstants.NO_OFFSET;
import static com.isles.skyblockisles.islesextra.client.screen.advancement.AdvancementScreenConstants.PANE_SIZE;
import static com.isles.skyblockisles.islesextra.client.screen.advancement.AdvancementScreenConstants.TOP_HEIGHT;

import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.advancement.AdvancementDisplay;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.PlacedAdvancement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.AssetInfo;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

@Environment(value = EnvType.CLIENT)
public class AdvancementTab {

  private final MinecraftClient client;
  private final AdvancementScreen screen;
  private final AdvancementTabType type;
  private final int index;
  private final PlacedAdvancement root;
  private final AdvancementDisplay display;
  private final ItemStack icon;
  private final Text title;
  private final AdvancementWidget rootWidget;
  private final Map<AdvancementEntry, AdvancementWidget> widgets = Maps.newLinkedHashMap();
  private double originX;
  private double originY;
  private int minPanX = Integer.MAX_VALUE;
  private int minPanY = Integer.MAX_VALUE;
  private int maxPanX = Integer.MIN_VALUE;
  private int maxPanY = Integer.MIN_VALUE;
  private float alpha;
  private boolean initialized;

  public AdvancementTab(MinecraftClient client, AdvancementScreen screen,
      AdvancementTabType type, int index, PlacedAdvancement root, AdvancementDisplay display) {
    this.client = client;
    this.screen = screen;
    this.type = type;
    this.index = index;
    this.root = root;
    this.display = display;
    this.icon = display.getIcon();
    this.title = display.getTitle();
    this.rootWidget = new AdvancementWidget(this, client, root, display);
    this.addWidget(this.rootWidget, root.getAdvancementEntry());
  }

  public PlacedAdvancement getRoot() {
    return this.root;
  }

  public Text getTitle() {
    return this.title;
  }

  public void drawBackground(DrawContext context, int x, int y, boolean selected) {
    this.type.drawBackground(context, x, y, selected, this.index);
  }

  public void drawIcon(DrawContext context, int x, int y) {
    this.type.drawIcon(context, x, y, this.index, this.icon);
  }

  public void render(DrawContext context, int x, int y, int width, int height) {
    if (!this.initialized) {
      this.originX = (width - 18) / 2.0 - (this.maxPanX + this.minPanX) / 2.0;
      this.originY = (height - 27) / 2.0 - (this.maxPanY + this.minPanY) / 2.0;
      this.initialized = true;
    }

    context.getMatrices().pushMatrix();
    context.getMatrices().translate(x, y);

    var background = this.display.getBackground();
    Identifier rawId = background.map(AssetInfo::comp_3626)
        .orElse(TextureManager.MISSING_IDENTIFIER); // Identifier
    Identifier identifier;
    if (rawId.getPath().startsWith("textures/") && rawId.getPath().endsWith(".png")) {
      identifier = rawId; // Already correct
    } else {
      identifier = Identifier.of(rawId.getNamespace(), "textures/" + rawId.getPath() + ".png");
    }

    int i = MathHelper.floor(this.originX);
    int j = MathHelper.floor(this.originY);
    int k = i % 16;
    int l = j % 16;

    // This loop causes UBO resizing lag because it issues hundreds of draw calls.
    // It is necessary for tiling, but we cache the limits to avoid extra math.
    int xCount = MathHelper.ceil(width / (float) PANE_SIZE);
    int yCount = MathHelper.ceil(height / (float) PANE_SIZE);

    for (int m = -1; m <= xCount; ++m) {
      for (int n = -1; n <= yCount; ++n) {
        context.drawTexture(RenderPipelines.GUI_TEXTURED, identifier, k + PANE_SIZE * m,
            l + PANE_SIZE * n, 0.0f, 0.0f, PANE_SIZE, PANE_SIZE, PANE_SIZE, PANE_SIZE);
      }
    }

    this.rootWidget.renderLines(context, i, j, true);
    this.rootWidget.renderLines(context, i, j, false);
    this.rootWidget.renderWidgets(context, i, j);
    context.getMatrices().popMatrix();
  }

  public void drawWidgetTooltip(DrawContext context, Click click, int x, int y, int width,
      int height, int borderWidth, int topHeight) {
    int mouseX = (int) click.comp_4798() - x - borderWidth;
    int mouseY = (int) click.comp_4799() - y - topHeight;

    context.getMatrices().pushMatrix();
    context.fill(NO_OFFSET, NO_OFFSET, width - 2 * BORDER_WIDTH,
        height - (BOTTOM_HEIGHT + TOP_HEIGHT), MathHelper.floor(this.alpha * 255.0f) << 24);

    boolean bl = false;
    int i = MathHelper.floor(this.originX);
    int j = MathHelper.floor(this.originY);

    if (mouseX > 0 && mouseX < (width - 18) && mouseY > 0 && mouseY < (height - 27)) {
      for (AdvancementWidget widget : this.widgets.values()) {
        if (!widget.shouldRender(i, j, mouseX, mouseY)) {
          continue;
        }
        bl = true;
        widget.drawTooltip(context, i, j, this.alpha, x, y);
        break;
      }
    }
    context.getMatrices().popMatrix();
    this.alpha = bl ? MathHelper.clamp(this.alpha + 0.02f, 0.0f, 0.3f)
        : MathHelper.clamp(this.alpha - 0.04f, 0.0f, 1.0f);
  }

  public boolean isClickOnTab(double screenX, double screenY, Click click) {
    return this.type.isClickOnTab(screenX, screenY, this.index, click);
  }

  @Nullable
  public static AdvancementTab create(MinecraftClient client, AdvancementScreen screen,
      int index, PlacedAdvancement root) {
    // MAPPING FIX: comp_1913 -> display()
    Optional<AdvancementDisplay> optional = root.getAdvancement().comp_1913(); // Display
    if (optional.isEmpty()) {
      return null;
    }

    for (AdvancementTabType type : AdvancementTabType.values()) {
      if (index >= type.getTabCount()) {
        index -= type.getTabCount();
        continue;
      }
      return new AdvancementTab(client, screen, type, index, root, optional.get());
    }
    return null;
  }

  public void move(double offsetX, double offsetY, int width, int height) {
    this.originX = MathHelper.clamp(this.originX + offsetX, -(this.maxPanX - (width - 18)) - width,
        width);
    this.originY = MathHelper.clamp(this.originY + offsetY,
        -(this.maxPanY - (height - 27)) - height, height);
  }

  public void addAdvancement(PlacedAdvancement advancement) {
    // MAPPING FIX: comp_1913 -> display()
    Optional<AdvancementDisplay> optional = advancement.getAdvancement().comp_1913(); // Display
    if (optional.isEmpty()) {
      return;
    }

    AdvancementWidget widget = new AdvancementWidget(this, this.client, advancement,
        optional.get());
    this.addWidget(widget, advancement.getAdvancementEntry());
  }

  private void addWidget(AdvancementWidget widget, AdvancementEntry advancement) {
    this.widgets.put(advancement, widget);
    int i = widget.getX();
    int j = i + 28;
    int k = widget.getY();
    int l = k + 27;
    this.minPanX = Math.min(this.minPanX, i);
    this.maxPanX = Math.max(this.maxPanX, j);
    this.minPanY = Math.min(this.minPanY, k);
    this.maxPanY = Math.max(this.maxPanY, l);
    for (AdvancementWidget w : this.widgets.values()) {
      w.addToTree();
    }
  }

  @Nullable
  public AdvancementWidget getWidget(AdvancementEntry advancement) {
    return this.widgets.get(advancement);
  }

  public AdvancementScreen getScreen() {
    return this.screen;
  }
}