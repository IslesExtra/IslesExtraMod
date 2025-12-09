package com.isles.skyblockisles.islesextra.client;

import net.minecraft.text.*;

import java.awt.*;
import java.util.ArrayList;

public class CustomText {

    // Custom text class allows for creating text components with colour coding, even HEX. (Currently broken due to 1.19.2 update)
    // this class shouldn't even exist, I'm an idiot

    private final Text value;
    public CustomText(String text, int r, int g, int b) {
        value = Text.literal(text).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(new Color(r,g,b).getRGB())).withItalic(false));
    }
    public CustomText(String string) {
        if (!string.contains("&")) {
            value = new CustomText(string, 255, 255, 255).getValue();
            return;
        }
        String[] parts = string.split("&");
        ArrayList<Text> texts = new ArrayList<>();
        for (String part : parts) {
            if (part.isEmpty()) continue;
            char firstChar = part.charAt(0);
            if (firstChar=='#') {
                String hex = part.substring(0,6);
                Color color = Color.decode(hex);
                texts.add(Text.literal(part.substring(7)).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(color.getRGB())).withItalic(false)));
            } else {
                texts.add(Text.literal("§"+part));
            }
        }
        MutableText finalText = Text.literal("");
        for (Text text : texts) { finalText.append(text); }
        value = finalText;
    }
    public Text getValue() { return value; }
}