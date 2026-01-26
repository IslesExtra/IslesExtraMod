package net.skyblockisles.islesextra.keybindings;

public enum IslesKeybindingsCategory {
  GENERAL("general");

  private final String name;

  IslesKeybindingsCategory(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
