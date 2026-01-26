package net.skyblockisles.islesextra.keybindings;

import org.lwjgl.glfw.GLFW;

public enum IslesKeybinding {
  OPEN_PARTY_SCREEN(
      "open_party",
      GLFW.GLFW_KEY_P,
      IslesKeybindingsCategory.GENERAL
  );

  private final String name;
  private final int defaultKey;
  private final IslesKeybindingsCategory category;

  IslesKeybinding(String name, int defaultKey, IslesKeybindingsCategory category) {
    this.name = name;
    this.defaultKey = defaultKey;
    this.category = category;
  }

  public String getName() {
    return name;
  }

  public IslesKeybindingsCategory getCategory() {
    return category;
  }

  public int getDefaultKey() {
    return defaultKey;
  }

}
