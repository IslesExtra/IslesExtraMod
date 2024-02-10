package com.isles.skyblockisles.islesextra.chat;

import java.util.HashSet;
import java.util.Set;

public class ChatSuggestions {

    private static final Set<String> suggestions = new HashSet<>();
    private static boolean suggestingEmojis = false;

    public static boolean isSuggestingEmojis() {
        return suggestingEmojis;
    }

    public static void setSuggestingEmojis(boolean suggestingEmojis) {
        ChatSuggestions.suggestingEmojis = suggestingEmojis;
    }

    private static boolean suggestingPings = false;

    public static boolean isSuggestingPings() {
        return suggestingPings;
    }

    public static void setSuggestingPings(boolean suggestingPings) {
        ChatSuggestions.suggestingPings = suggestingPings;
    }

    public static void setSuggestions(Set<String> newSuggestions) {
        suggestions.clear();
        suggestions.addAll(newSuggestions);
    }

    public static Set<String> getSuggestions() {
        return suggestions;
    }
}
