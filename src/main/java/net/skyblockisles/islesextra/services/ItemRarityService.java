package net.skyblockisles.islesextra.services;

import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.skyblockisles.islesextra.IslesExtra;
import net.skyblockisles.islesextra.constants.IslesRarity;

public class ItemRarityService {

    public static final Identifier RARITY_ID = Identifier.of(IslesExtra.MOD_ID, "textures/gui/aura.png");

    public static void renderItemRarity(DrawContext drawContext, int x, int y, int color) {
        drawContext.drawTexture(RenderPipelines.GUI_TEXTURED, RARITY_ID, x - 1, y - 1, 0, 0, 18, 18, 18, 18, color);
    }

    public static IslesRarity getTierOfItem(ItemStack stack) {
        if (stack != null) {
            LoreComponent lore = stack.get(DataComponentTypes.LORE);
            if (lore != null && !lore.comp_2400().isEmpty()) {
                String checkLine = lore.comp_2400().getFirst().getString();
                if (checkLine.contains("\uEA43")) {
                    return IslesRarity.COMMON;
                } else if (checkLine.contains("\uEA44")) {
                    return IslesRarity.UNCOMMON;
                } else if (checkLine.contains("\uEA45")) {
                    return IslesRarity.RARE;
                } else if (checkLine.contains("\uEA46")) {
                    return IslesRarity.EPIC;
                } else if (checkLine.contains("\uEA47")) {
                    return IslesRarity.LEGENDARY;
                } else if (checkLine.contains("\uEA48")) {
                    return IslesRarity.MYTHIC;
                }
            }
        }
        return IslesRarity.NONE;
    }

    public static int getColorForTier(IslesRarity tier) {
        return switch (tier) {
            case COMMON -> RGBA(239, 192, 117, 255);
            case UNCOMMON -> RGBA(5, 159, 28, 255);
            case RARE -> RGBA(31, 142, 220, 255);
            case EPIC -> RGBA(132, 42, 151, 255);
            case LEGENDARY -> RGBA(255, 192, 58, 255);
            case MYTHIC -> RGBA(163, 43, 43, 255);
            default -> 0;
        };
    }

    public static int RGBA(int r, int g, int b, int a) {
        return (a << 24) | ((r & 255) << 16) | ((g & 255) << 8) | (b & 255);
    }

}
