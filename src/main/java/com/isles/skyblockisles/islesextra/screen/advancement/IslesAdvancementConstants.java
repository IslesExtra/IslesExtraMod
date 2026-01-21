package com.isles.skyblockisles.islesextra.screen.advancement;

import static com.isles.skyblockisles.islesextra.IslesExtraClient.MOD_ID;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class IslesAdvancementConstants {
  private IslesAdvancementConstants() {}

  protected static final Identifier WINDOW_TEXTURE = Identifier.ofVanilla("textures/gui/advancements/window.png");
  protected static final Identifier RESIZE_TEXTURE = Identifier.of(MOD_ID, "textures/gui/advancements/resize.png");

  protected static final Text SAD_LABEL_TEXT = Text.translatable("advancements.sad_label");
  protected static final Text EMPTY_TEXT = Text.translatable("advancements.empty");
  protected static final Text ADVANCEMENTS_TEXT = Text.translatable("gui.advancements");

  protected static final int TEXTURE_SIZE = 256;
  protected static final int VISUAL_TEXTURE_WIDTH = 252;
  protected static final int PANE_SIZE = 16;
  protected static final int RESIZE_ICON_SIZE = 9;

  protected static int WINDOW_WIDTH = 340;
  protected static int WINDOW_HEIGHT = 224;
  protected static final int NO_OFFSET = 0;
  protected static final int BORDER_WIDTH = 9;
  protected static final int TOP_HEIGHT = 18;
  protected static final int BOTTOM_HEIGHT = 9;
  protected static final int BOTTOM_BORDER_V = 131;

  protected static final int TAB_HEIGHT = 28;

  protected static final int TITLE_OFFSET_X = 8;
  protected static final int TITLE_OFFSET_Y = 6;
}
