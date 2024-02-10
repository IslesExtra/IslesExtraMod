package com.isles.skyblockisles.islesextra.mixin.chat;

import com.isles.skyblockisles.islesextra.IslesExtra;
import com.isles.skyblockisles.islesextra.chat.ChatPreview;
import com.isles.skyblockisles.islesextra.chat.ChatSuggestions;
import com.isles.skyblockisles.islesextra.client.ClientUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCrashException;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin {

    @Shadow protected TextFieldWidget chatField;
    @Shadow ChatInputSuggestor chatInputSuggestor;
    @Unique
    private final ChatPreview previewBackground = new ChatPreview();
    @Unique
    private static Text previewText;
    @Unique
    private MinecraftClient client;

    @Inject(method = "render", at = @At("HEAD"))
    void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {

        if (this.client == null) return;
        if (previewText==null) return;
        if (this.chatField.getText().startsWith("/")) return;

        ChatPreview.RenderData renderData = previewBackground.computeRenderData(Util.getMeasuringTimeMs(), previewText);
        renderPreview(context, renderData.preview(), renderData.alpha(), true/*client.getProfileKeys().isExpired() != null*/); // I have no idea wtf this commented shit is but keeping it here just in case
    }

    @Unique
    private final static Identifier previewIdentifier = new Identifier("isles", "preview");

    @Inject(method = "onChatFieldUpdate", at = @At("TAIL"))
    void onChatFieldUpdate(String chatText, CallbackInfo ci) {
        CompletingSuggestionsAccessor completingSuggestions = ((CompletingSuggestionsAccessor) this.chatInputSuggestor);
        String currentWord = getCurrentWord(chatText);
        // emoji completing
        if (currentWord.startsWith(":")) {
            ChatSuggestions.setSuggestions(IslesExtra.emojis);
            ChatSuggestions.setSuggestingEmojis(true);
            completingSuggestions.setCompletingSuggestions(true);
            this.chatInputSuggestor.refresh();
            this.chatInputSuggestor.show(true);
        }
        // ping completing
        else if (currentWord.startsWith("@")) {
            ChatSuggestions.setSuggestions(ClientUtils.getPlayerNames(true));
            ChatSuggestions.setSuggestingPings(true);
            completingSuggestions.setCompletingSuggestions(true);
            this.chatInputSuggestor.refresh();
            this.chatInputSuggestor.show(true);
        }
        // cancel
        else if (completingSuggestions.isCompletingSuggestions() &&
                (ChatSuggestions.isSuggestingEmojis() || ChatSuggestions.isSuggestingPings())) {
            ChatSuggestions.setSuggestions(Set.of());
            ChatSuggestions.setSuggestingEmojis(false);
            ChatSuggestions.setSuggestingPings(false);
            completingSuggestions.setCompletingSuggestions(false);
            this.chatInputSuggestor.refresh();
        }
        if (client.player == null) return;
        // TODO; add more exceptions to decrease packets
        boolean flag1 = !chatText.startsWith("/"); // not for commands
        if (flag1) {
            ClientPlayNetworking.send(previewIdentifier, PacketByteBufs.create().writeString(chatText));
            return;
        }
        ChatScreenMixin.previewText = null;
        ChatPreview.setShouldShiftUp(false);
    }

    @Inject(method = "init", at = @At("HEAD"))
    void init(CallbackInfo ci) {
        this.client = MinecraftClient.getInstance();
        previewBackground.init(Util.getMeasuringTimeMs());
        ClientPlayNetworking.registerGlobalReceiver(previewIdentifier, (client, handler, buf, responseSender) -> {
            String s = buf.readString();
            Text text = Text.Serialization.fromJson(s);
            if (text == null || text.getString().isEmpty() || !(client.currentScreen instanceof ChatScreen)) {
                previewText = null;
                ChatPreview.setShouldShiftUp(false);
                return;
            }
            previewText = text;
            ChatPreview.setShouldShiftUp(true);
        });
    }

    @Inject(method = "keyPressed", at = @At("HEAD"))
    void keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        // if chat gets closed
        if (keyCode == 256 || keyCode == 257 || keyCode == 335) {
            previewText = null;
            ChatSuggestions.setSuggestions(Set.of());
            ChatSuggestions.setSuggestingPings(false);
            ChatSuggestions.setSuggestingEmojis(false);
            ChatPreview.setShouldShiftUp(false);
        }
    }

    @Unique
    public void renderPreview(DrawContext context, Text previewText, float alpha, boolean signable) {
        TextRenderer textRenderer = client.textRenderer;
        MatrixStack matrices = context.getMatrices();
        int i = (int)(255.0 * (client.options.getChatOpacity().getValue() * 0.8999999761581421 + 0.10000000149011612) * (double)alpha);
        int j = (int)((double)(255) * client.options.getTextBackgroundOpacity().getValue() * (double)alpha);
        int k = this.getPreviewWidth();
        List<OrderedText> list = textRenderer.wrapLines(previewText, this.getPreviewWidth());
        int l = (Math.max(list.size(), 1) * 9 + 4);
        int m = this.getPreviewBottom() - l;
        RenderSystem.enableBlend();
        matrices.push();
        matrices.translate(2, m, 0.0);
        context.fill(0, 0, k, l, j << 24);
        int n;
        if (i > 0) {
            matrices.translate(2.0, 2.0, 0.0);

            for(n = 0; n < list.size(); ++n) {
                OrderedText orderedText = list.get(n);
                Objects.requireNonNull(textRenderer);
                int o = n * 9;
                context.drawText(textRenderer, orderedText, 0, o, i << 24 | 16777215, true);
            }
        }

        matrices.pop();
        RenderSystem.disableBlend();
        if (signable && previewText != null) {
            n = 7844841;
            int p = (int)(255.0F * alpha);
            matrices.push();
            context.fill(0, m, 2, this.getPreviewBottom(), p << 24 | n);
            matrices.pop();
        }

    }

    @Unique
    private int getPreviewWidth() {
        return client.currentScreen == null ? 0 : client.currentScreen.width - 4;
    }

    @Unique
    private int getPreviewBottom() {
        if (client.currentScreen == null) return 0;
        return client.currentScreen.height - 15;
    }

    @Unique
    public String getCurrentWord(String chatText) {
        int startSelection = this.chatField.getCursor();
        String selectedWord = "";
        int length = 0;

        for(String currentWord : chatText.split(" ")) {
            length = length + currentWord.length() + 1;
            if(length > startSelection) {
                selectedWord = currentWord;
                break;
            }
        }
        return selectedWord;
    }

}