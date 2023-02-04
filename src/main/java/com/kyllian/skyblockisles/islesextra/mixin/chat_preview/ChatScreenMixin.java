package com.kyllian.skyblockisles.islesextra.mixin.chat_preview;

import com.kyllian.skyblockisles.islesextra.preview.ChatPreview;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Objects;

import static net.minecraft.client.gui.DrawableHelper.fill;

@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin {

    private final ChatPreview previewBackground = new ChatPreview();
    private static Text previewText;
    private MinecraftClient client;

    @Inject(method = "render", at = @At("TAIL"))
    void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {

        if (client == null) return;
        if (previewText==null) return;

        ChatPreview.RenderData renderData = previewBackground.computeRenderData(Util.getMeasuringTimeMs(), previewText);
        renderPreview(matrices, renderData.preview(), renderData.alpha(), true/*client.getProfileKeys().isExpired() != null*/);
    }

    private final static Identifier previewIdentifier = new Identifier("isles", "preview");

    @Inject(method = "onChatFieldUpdate", at = @At("HEAD"))
    void onChatFieldUpdate(String chatText, CallbackInfo ci) {
        ClientPlayNetworking.send(previewIdentifier, PacketByteBufs.create().writeString(chatText));
    }

    @Inject(method = "init", at = @At("HEAD"))
    void init(CallbackInfo ci) {
        this.client = MinecraftClient.getInstance();
        previewBackground.init(Util.getMeasuringTimeMs());
        ClientPlayNetworking.registerGlobalReceiver(previewIdentifier, (client, handler, buf, responseSender) -> {
            Text text = buf.readText();
            if (text == null || text.getString().equals("")) previewText = null;
            else previewText = text;
        });
    }

    @Inject(method = "keyPressed", at = @At("HEAD"))
    void keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (keyCode == 256 || keyCode == 257 || keyCode == 335) previewText = null;
    }

    public void renderPreview(MatrixStack matrices, Text previewText, float alpha, boolean signable) {
        TextRenderer textRenderer = client.textRenderer;
        int i = (int)(255.0 * (client.options.getChatOpacity().getValue() * 0.8999999761581421 + 0.10000000149011612) * (double)alpha);
        int j = (int)((double)(255) * client.options.getTextBackgroundOpacity().getValue() * (double)alpha);
        int k = this.getPreviewWidth();
        List<OrderedText> list = this.wrapPreviewText(previewText, textRenderer);
        int l = this.getPreviewHeight(list);
        int m = this.getPreviewTop(l);
        RenderSystem.enableBlend();
        matrices.push();
        matrices.translate((double)this.getPreviewLeft(), (double)m, 0.0);
        fill(matrices, 0, 0, k, l, j << 24);
        int n;
        if (i > 0) {
            matrices.translate(2.0, 2.0, 0.0);

            for(n = 0; n < list.size(); ++n) {
                OrderedText orderedText = (OrderedText)list.get(n);
                Objects.requireNonNull(textRenderer);
                int o = n * 9;
                textRenderer.drawWithShadow(matrices, orderedText, 0.0F, (float)o, i << 24 | 16777215);
            }
        }

        matrices.pop();
        RenderSystem.disableBlend();
        if (signable && previewText != null) {
            n = 7844841;
            int p = (int)(255.0F * alpha);
            matrices.push();
            fill(matrices, 0, m, 2, this.getPreviewBottom(), p << 24 | n);
            matrices.pop();
        }

    }

    private List<OrderedText> wrapPreviewText(Text preview, TextRenderer textRenderer) {
        return textRenderer.wrapLines(preview, this.getPreviewWidth());
    }

    private int getPreviewWidth() {
        if (client.currentScreen == null) return 0;
        return client.currentScreen.width - 4;
    }

    private int getPreviewHeight(List<OrderedText> lines) {
        return Math.max(lines.size(), 1) * 9 + 4;
    }

    private int getPreviewBottom() {
        if (client.currentScreen == null) return 0;
        return client.currentScreen.height - 15;
    }

    private int getPreviewTop(int previewHeight) {
        return this.getPreviewBottom() - previewHeight;
    }

    private int getPreviewLeft() {
        return 2;
    }

}