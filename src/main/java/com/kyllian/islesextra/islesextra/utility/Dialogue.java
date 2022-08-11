package com.kyllian.islesextra.islesextra.utility;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Dialogue {

    private static int tick = 0;
    private static final int DIALOGUE_TICKS = 2;
    private static final int TIMEOUT_TICKS = 4;
    private static final List<DialogueComponent> components = new ArrayList<>();
    private static int dialogueTime = 0;

    public static void addDialogue(DialogueComponent component) {
        Dialogue.components.add(component);
    }

    public static void setupDialogue() {
        ClientTickEvents.END_CLIENT_TICK.register((client -> {
            if (client==null || client.player==null) return;
            if (components.size()==0) return;
            if (Dialogue.tick++ % Dialogue.DIALOGUE_TICKS != 0) return;
            DialogueComponent current = components.get(0);
            if (dialogueTime++ < current.text.length()) client.player.playSound(SoundEvents.ENTITY_ITEM_FRAME_ADD_ITEM, 1f, 1f);
            Dialogue.tick = 0;
            if (dialogueTime <= current.text.length() + TIMEOUT_TICKS) return;
            dialogueTime = 0;
            components.remove(0);
        }));
    }

    public static void render(MatrixStack matrices) {

        if (components.size()==0) return;
        DialogueComponent current = components.get(0);
        Text currentText = Text.literal(current.participant)
                .setStyle(Style.EMPTY.withColor(TextColor.fromRgb(new Color(100, 100, 100).getRGB())))
                .append(Text.literal(" >>> ").setStyle(Style.EMPTY.withColor(new Color(150, 150, 150).getRGB())))
                .append(Text.literal(current.text.substring(0, Math.min(dialogueTime, current.text.length())))
                .setStyle(Style.EMPTY.withColor(TextColor.fromRgb(new Color(250, 249, 250).getRGB())).withItalic(Boolean.TRUE)));

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player==null) return;
        TextRenderer textRenderer = client.textRenderer;

        int scaledWidth = client.getWindow().getScaledWidth();
        int scaledHeight = client.getWindow().getScaledHeight();

        float h = (float)client.player.getSleepTimer();
        int l = Math.min((int) (h * 255.0F / 20.0F), 255);

        matrices.push();
        matrices.translate(scaledWidth / 2f, scaledHeight - 68, 0.0);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        int k = 16777215;

        int m = l << 24 & -16777216;
        int n = textRenderer.getWidth(currentText);
        textRenderer.drawWithShadow(matrices, currentText, (float)(-n / 2), -8.0F, k | m);
        RenderSystem.disableBlend();
        matrices.pop();
    }


    public static class DialogueComponent {

        private final String text, participant;
        public DialogueComponent(String dialogueText, String participantName) { text = dialogueText; participant = participantName; }

    }

}
