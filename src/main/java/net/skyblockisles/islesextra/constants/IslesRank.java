package net.skyblockisles.islesextra.constants;

public enum IslesRank {
  ADVENTURER(""),
  FOUNDER("\uE045"),
  GAME_TESTER("\uE044"),
  DEVELOPER("\uE041"),
  OWNER("\uE049"),
  DISCORD("[Discord]");

  public final String icon;

  IslesRank(String icon) {
    this.icon = icon;
  }
}
