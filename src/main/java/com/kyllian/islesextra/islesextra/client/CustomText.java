package com.kyllian.islesextra.islesextra.client;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;

import java.awt.*;
import java.util.ArrayList;

public class CustomText {
    private final Text value;
    public CustomText(String text, int r, int g, int b) {
        value = new LiteralText(text).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(new Color(r,g,b).getRGB())).withItalic(false));
    }
    public CustomText(Text... texts) {
        LiteralText finalText = new LiteralText("");
        for (Text text : texts) { finalText.append(text); }
        value = finalText;
    }
    public CustomText(String string) {
        if (!string.contains("&")) {
            value = new CustomText(string, 255, 255, 255).getValue();
            return;
        }
        String[] parts = string.split("&");
        ArrayList<Text> texts = new ArrayList<>();
        for (String part : parts) {
            if (part.length()==0) continue;
            char firstChar = part.charAt(0);
            if (firstChar=='#') {
                String hex = part.substring(0,6);
                Color color = Color.decode(hex);
                texts.add(new LiteralText(part.substring(7)).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(color.getRGB())).withItalic(false)));
            } else {
                texts.add(new LiteralText("§"+part));
            }
        }
        LiteralText finalText = new LiteralText("");
        for (Text text : texts) { finalText.append(text); }
        value = finalText;
    }
    public Text getValue() { return value; }
}