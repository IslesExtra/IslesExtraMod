package com.isles.skyblockisles.islesextra.utils;

import java.util.Arrays;
import java.util.List;

public class IslesConstants {

    // Every screen seems to have 2 unicodes as a title, we'll just evaluate the first one and second as ints
    public enum Gui {
        NONE(0, 0),
        ESCAPE_MENU(57667, 60423),
        GAME_SELECTOR(57608, 60420),
        BOSS_RUSH_DIFFICULTY_SELECTOR(57608, 60422), // the menu that allows between choosing rookie, expert and master
        BOSS_RUSH_SELECTOR(57608, 60423); // the menu that allows you to pick a boss


        private final int primary;
        private final int secondary;
        Gui(int primary, int secondary) {
            this.primary = primary;
            this.secondary = secondary;
        }

        public boolean matches(String screenName) {
            if (screenName.length() < 2) return false;
            int first = screenName.charAt(0);
            int second = screenName.charAt(1);
            return primary == first && secondary == second;
        }
    }

    public static Gui guiFromScreenName(String screenName) {
        for (Gui gui : Gui.values()) {
            if (gui.matches(screenName)) return gui;
        }
        return Gui.NONE;
    }

    public static final List<String> rarities = List.of("Common","Uncommon","Rare","Epic","Legendary","Mythic");

}
