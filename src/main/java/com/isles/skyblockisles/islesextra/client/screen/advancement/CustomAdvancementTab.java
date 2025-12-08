package com.isles.skyblockisles.islesextra.client.screen.advancement;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.advancement.AdvancementDisplay;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.PlacedAdvancement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class CustomAdvancementTab {
    private final MinecraftClient client;
    private final CustomAdvancementsScreen screen;
    private final AdvancementTabType type;
    private final int index;
    private final PlacedAdvancement root;
    private final AdvancementDisplay display;
    private final ItemStack icon;
    private final Text title;
    private final CustomAdvancementWidget rootWidget;
    private final Map<AdvancementEntry, CustomAdvancementWidget> widgets = Maps.newLinkedHashMap();
    private double originX;
    private double originY;
    private int minPanX = Integer.MAX_VALUE;
    private int minPanY = Integer.MAX_VALUE;
    private int maxPanX = Integer.MIN_VALUE;
    private int maxPanY = Integer.MIN_VALUE;
    private float alpha;
    private boolean initialized;

    public CustomAdvancementTab(MinecraftClient client, CustomAdvancementsScreen screen, AdvancementTabType type, int index, PlacedAdvancement root, AdvancementDisplay display) {
        this.client = client;
        this.screen = screen;
        this.type = type;
        this.index = index;
        this.root = root;
        this.display = display;
        this.icon = display.getIcon();
        this.title = display.getTitle();
        this.rootWidget = new CustomAdvancementWidget(this, client, root, display);
        this.addWidget(this.rootWidget, root.getAdvancementEntry());
    }

    public double getOriginX() {
        return originX;
    }

    public double getOriginY() {
        return originY;
    }

    public AdvancementTabType getType() {
        return this.type;
    }

    public int getIndex() {
        return this.index;
    }

    public PlacedAdvancement getRoot() {
        return this.root;
    }

    public Text getTitle() {
        return this.title;
    }

    public AdvancementDisplay getDisplay() {
        return this.display;
    }

    public void drawBackground(DrawContext context, int x, int y, boolean selected) {
        this.type.drawBackground(context, x, y, selected, this.index);
    }

    public void drawIcon(DrawContext context, int x, int y) {
        this.type.drawIcon(context, x, y, this.index, this.icon);
    }

    public void render(DrawContext context, int x, int y, int width, int height) {
        if (!this.initialized) {
            this.originX = (width-2*9)/2f - (this.maxPanX + this.minPanX) / 2f;
            this.originY = (height-3*9)/2f - (this.maxPanY + this.minPanY) / 2f;
            this.initialized = true;
        }
        context.enableScissor(x, y, x + width - 9*2, y + height - 9 - 18);
        context.getMatrices().pushMatrix();
        context.getMatrices().translate(x, y, context.getMatrices());
        var background = this.display.getBackground();
        Identifier identifier = background.isPresent() ? background.get().comp_3626() : TextureManager.MISSING_IDENTIFIER; // Identifier
        int i = MathHelper.floor(this.originX);
        int j = MathHelper.floor(this.originY);
        int k = i % 16;
        int l = j % 16;
        for (int m = -1; m <= MathHelper.ceil(width/16f); ++m) {
            for (int n = -1; n <= MathHelper.ceil(height/16f); ++n) {
                context.drawTexture(RenderPipelines.GUI_TEXTURED, identifier, k + 16 * m, l + 16 * n, 0.0f, 0.0f, 16, 16, 16, 16);
            }
        }
        this.rootWidget.renderLines(context, i, j, true);
        this.rootWidget.renderLines(context, i, j, false);
        this.rootWidget.renderWidgets(context, i, j);
        context.getMatrices().popMatrix();
        context.disableScissor();
    }

    public void drawWidgetTooltip(DrawContext context, int mouseX, int mouseY, int x, int y, int width, int height) {
        context.getMatrices().pushMatrix();
        context.getMatrices().translate(0.0f, 0.0f, context.getMatrices());
        context.fill(0, 0, (width - 2*9), (height - 3*9), MathHelper.floor(this.alpha * 255.0f) << 24);
        boolean bl = false;
        int i = MathHelper.floor(this.originX);
        int j = MathHelper.floor(this.originY);
        if (mouseX > 0 && mouseX < (width - 2*9) && mouseY > 0 && mouseY < (height - 3*9)) {
            for (CustomAdvancementWidget CustomAdvancementWidget : this.widgets.values()) {
                if (!CustomAdvancementWidget.shouldRender(i, j, mouseX, mouseY)) continue;
                bl = true;
                CustomAdvancementWidget.drawTooltip(context, i, j, this.alpha, x, y);
                break;
            }
        }
        context.getMatrices().popMatrix();
        this.alpha = bl ? MathHelper.clamp(this.alpha + 0.02f, 0.0f, 0.3f) : MathHelper.clamp(this.alpha - 0.04f, 0.0f, 1.0f);
    }

    public boolean isClickOnTab(double screenX, double screenY, double mouseX, double mouseY) {
        return this.type.isClickOnTab(screenX, screenY, this.index, mouseX, mouseY);
    }

    @Nullable
    public static CustomAdvancementTab create(MinecraftClient client, CustomAdvancementsScreen screen, int index, PlacedAdvancement root) {
        Optional<AdvancementDisplay> optional = root.getAdvancement().comp_1913(); // display
        if (optional.isEmpty()) {
            return null;
        }
        for (AdvancementTabType advancementTabType : AdvancementTabType.values()) {
            if (index >= advancementTabType.getTabCount()) {
                index -= advancementTabType.getTabCount();
                continue;
            }
            return new CustomAdvancementTab(client, screen, advancementTabType, index, root, optional.get());
        }
        return null;
    }

    public void move(double offsetX, double offsetY, int width, int height) {
        this.originX = MathHelper.clamp(this.originX + offsetX, -(this.maxPanX - (width - 2*9)) - width, width);
        this.originY = MathHelper.clamp(this.originY + offsetY, -(this.maxPanY - (height - 3*9)) - height, height);
    }

    public void addAdvancement(PlacedAdvancement advancement) {
        Optional<AdvancementDisplay> optional = advancement.getAdvancement().comp_1913(); // Display
        if (optional.isEmpty()) {
            return;
        }
        CustomAdvancementWidget CustomAdvancementWidget = new CustomAdvancementWidget(this, this.client, advancement, optional.get());
        this.addWidget(CustomAdvancementWidget, advancement.getAdvancementEntry());
    }

    private void addWidget(CustomAdvancementWidget widget, AdvancementEntry advancement) {
        this.widgets.put(advancement, widget);
        int i = widget.getX();
        int j = i + 28;
        int k = widget.getY();
        int l = k + 27;
        this.minPanX = Math.min(this.minPanX, i);
        this.maxPanX = Math.max(this.maxPanX, j);
        this.minPanY = Math.min(this.minPanY, k);
        this.maxPanY = Math.max(this.maxPanY, l);
        for (CustomAdvancementWidget CustomAdvancementWidget : this.widgets.values()) {
            CustomAdvancementWidget.addToTree();
        }
    }

    @Nullable
    public CustomAdvancementWidget getWidget(AdvancementEntry advancement) {
        return this.widgets.get(advancement);
    }

    public CustomAdvancementsScreen getScreen() {
        return this.screen;
    }
}

