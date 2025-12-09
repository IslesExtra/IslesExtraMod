package com.isles.skyblockisles.islesextra.client.screen.advancement;

import static com.isles.skyblockisles.islesextra.client.screen.advancement.IslesAdvancementConstants.WINDOW_HEIGHT;
import static com.isles.skyblockisles.islesextra.client.screen.advancement.IslesAdvancementConstants.WINDOW_WIDTH;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

@Environment(value = EnvType.CLIENT)
public enum IslesAdvancementTabType {
  ABOVE(
      new Textures(
          Identifier.of("minecraft", "advancements/tab_above_left_selected"),
          Identifier.of("minecraft", "advancements/tab_above_middle_selected"),
          Identifier.of("minecraft", "advancements/tab_above_right_selected")
      ),
      new Textures(
          Identifier.of("minecraft", "advancements/tab_above_left"),
          Identifier.of("minecraft", "advancements/tab_above_middle"),
          Identifier.of("minecraft", "advancements/tab_above_right")
      ), 28, 32, 8),

  BELOW(
      new Textures(
          Identifier.of("minecraft", "advancements/tab_below_left_selected"),
          Identifier.of("minecraft", "advancements/tab_below_middle_selected"),
          Identifier.of("minecraft", "advancements/tab_below_right_selected")
      ),
      new Textures(
          Identifier.of("minecraft", "advancements/tab_below_left"),
          Identifier.of("minecraft", "advancements/tab_below_middle"),
          Identifier.of("minecraft", "advancements/tab_below_right")
      ), 28, 32, 8),

  LEFT(
      new Textures(
          Identifier.of("minecraft","advancements/tab_left_top_selected"),
          Identifier.of("minecraft","advancements/tab_left_middle_selected"),
          Identifier.of("minecraft","advancements/tab_left_bottom_selected")
      ),
      new Textures(
          Identifier.of("minecraft","advancements/tab_left_top"),
          Identifier.of("minecraft","advancements/tab_left_middle"),
          Identifier.of("minecraft","advancements/tab_left_bottom")
      ), 32, 28, 5),

  RIGHT(
      new Textures(
          Identifier.of("minecraft","advancements/tab_right_top_selected"),
          Identifier.of("minecraft","advancements/tab_right_middle_selected"),
          Identifier.of("minecraft","advancements/tab_right_bottom_selected")
      ),
      new Textures(
          Identifier.of("minecraft","advancements/tab_right_top"),
          Identifier.of("minecraft","advancements/tab_right_middle"),
          Identifier.of("minecraft","advancements/tab_right_bottom")
      ), 32, 28, 5);

  private final int width;
  private final int height;
  private final int tabCount;
  private final Textures selectedTextures;
  private final Textures unselectedTextures;

  IslesAdvancementTabType(Textures selected, Textures unselected, int width, int height, int tabCount) {
    this.selectedTextures = selected;
    this.unselectedTextures = unselected;
    this.width = width;
    this.height = height;
    this.tabCount = tabCount;
  }

  public int getTabCount() {
    return this.tabCount;
  }

  public void drawBackground(DrawContext context, int x, int y, boolean selected, int index) {
    Textures textures = selected ? this.selectedTextures : this.unselectedTextures;
    Identifier identifier = index == 0 ? textures.first()
        : (index == this.tabCount - 1 ? textures.last() : textures.middle());

    int drawX = x + this.getTabX(index);
    int drawY = y + this.getTabY(index);

    context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, identifier, drawX, drawY, width, height);
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
      case RIGHT -> WINDOW_WIDTH - 4;
    };
  }

  public int getTabY(int index) {
    return switch (this) {
      case ABOVE -> -this.height + 4;
      case BELOW -> WINDOW_HEIGHT - 4;
      case LEFT, RIGHT -> this.height * index;
    };
  }

  public boolean isClickOnTab(double screenX, double screenY, int index, Click click) {
    double mouseX = click.comp_4798();
    double mouseY = click.comp_4799();
    double i = screenX + this.getTabX(index);
    double j = screenY + this.getTabY(index);
    return mouseX > i && mouseX < (i + this.width) && mouseY > j && mouseY < (j + this.height);
  }

  @Environment(value = EnvType.CLIENT)
  record Textures(Identifier first, Identifier middle, Identifier last) {

  }
}

