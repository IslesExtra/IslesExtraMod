package com.isles.skyblockisles.islesextra.constants;

public enum IslesGui {
  NONE(0, 0),
  ESCAPE_MENU(57667, 60423),
  GAME_SELECTOR(57608, 60420),
  BOSS_RUSH_DIFFICULTY_SELECTOR(57608, 60422), // the menu that allows between choosing rookie, expert and master
  BOSS_RUSH_SELECTOR(57608, 60423); // the menu that allows you to pick a boss


  private final int primary;
  private final int secondary;

  static IslesGui openedGui = IslesGui.NONE;

  IslesGui(int primary, int secondary) {
    this.primary = primary;
    this.secondary = secondary;
  }

  public boolean matches(String screenName) {
    if (screenName.length() < 2) {
      return false;
    }
    int first = screenName.charAt(0);
    int second = screenName.charAt(1);
    return primary == first && secondary == second;
  }

  public static IslesGui guiFromScreenName(String screenName) {
    for (IslesGui gui : IslesGui.values()) {
      if (gui.matches(screenName)) {
        return gui;
      }
    }
    return IslesGui.NONE;
  }

  public static void openGui(IslesGui gui) {
    openedGui = gui;
  }
}