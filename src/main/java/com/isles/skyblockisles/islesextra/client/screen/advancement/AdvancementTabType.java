package com.isles.skyblockisles.islesextra.client.screen.advancement;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public enum AdvancementTabType {
    ABOVE(new AdvancementTabType.Textures(new Identifier("advancements/tab_above_left_selected"), new Identifier("advancements/tab_above_middle_selected"), new Identifier("advancements/tab_above_right_selected")), new AdvancementTabType.Textures(new Identifier("advancements/tab_above_left"), new Identifier("advancements/tab_above_middle"), new Identifier("advancements/tab_above_right")), 28, 32, 8),
    BELOW(new AdvancementTabType.Textures(new Identifier("advancements/tab_below_left_selected"), new Identifier("advancements/tab_below_middle_selected"), new Identifier("advancements/tab_below_right_selected")), new AdvancementTabType.Textures(new Identifier("advancements/tab_below_left"), new Identifier("advancements/tab_below_middle"), new Identifier("advancements/tab_below_right")), 28, 32, 8),
    LEFT(new AdvancementTabType.Textures(new Identifier("advancements/tab_left_top_selected"), new Identifier("advancements/tab_left_middle_selected"), new Identifier("advancements/tab_left_bottom_selected")), new AdvancementTabType.Textures(new Identifier("advancements/tab_left_top"), new Identifier("advancements/tab_left_middle"), new Identifier("advancements/tab_left_bottom")), 32, 28, 5),
    RIGHT(new AdvancementTabType.Textures(new Identifier("advancements/tab_right_top_selected"), new Identifier("advancements/tab_right_middle_selected"), new Identifier("advancements/tab_right_bottom_selected")), new AdvancementTabType.Textures(new Identifier("advancements/tab_right_top"), new Identifier("advancements/tab_right_middle"), new Identifier("advancements/tab_right_bottom")), 32, 28, 5);

    private final AdvancementTabType.Textures selectedTextures;
    private final AdvancementTabType.Textures unselectedTextures;
    private final int width;
    private final int height;
    private final int tabCount;

    AdvancementTabType(AdvancementTabType.Textures selectedTextures, AdvancementTabType.Textures unselectedTextures, int width, int height, int tabCount) {
        this.selectedTextures = selectedTextures;
        this.unselectedTextures = unselectedTextures;
        this.width = width;
        this.height = height;
        this.tabCount = tabCount;
    }

    public int getTabCount() {
        return this.tabCount;
    }

    public void drawBackground(DrawContext context, int x, int y, boolean selected, int index) {
        AdvancementTabType.Textures textures;
        AdvancementTabType.Textures textures2 = textures = selected ? this.selectedTextures : this.unselectedTextures;
        Identifier identifier = index == 0 ? textures.first() : (index == this.tabCount - 1 ? textures.last() : textures.middle());
        context.drawGuiTexture(identifier, x + this.getTabX(index), y + this.getTabY(index), this.width, this.height);
    }

    public void drawIcon(DrawContext context, int x, int y, int index, ItemStack stack) {
        int i = x + this.getTabX(index);
        int j = y + this.getTabY(index);
        switch (this) {
            case ABOVE: {
                i += 6;
                j += 9;
                break;
            }
            case BELOW: {
                i += 6;
                j += 6;
                break;
            }
            case LEFT: {
                i += 10;
                j += 5;
                break;
            }
            case RIGHT: {
                i += 6;
                j += 5;
            }
        }
        context.drawItemWithoutEntity(stack, i, j);
    }

    public int getTabX(int index) {
        return switch (this) {
            case ABOVE, BELOW -> (this.width + 4) * index;
            case LEFT -> -this.width + 4;
            case RIGHT -> 248;
        };
    }

    public int getTabY(int index) {
        return switch (this) {
            case ABOVE -> -this.height + 4;
            case BELOW -> 136;
            case LEFT, RIGHT -> this.height * index;
        };
    }

    public boolean isClickOnTab(double screenX, double screenY, int index, double mouseX, double mouseY) {
        double i = screenX + this.getTabX(index);
        double j = screenY + this.getTabY(index);
        return mouseX > i && mouseX < (i + this.width) && mouseY > j && mouseY < (j + this.height);
    }

    @Environment(value=EnvType.CLIENT)
    record Textures(Identifier first, Identifier middle, Identifier last) {
    }
}

