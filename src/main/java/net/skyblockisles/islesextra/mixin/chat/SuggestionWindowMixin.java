package net.skyblockisles.islesextra.mixin.chat;

import com.mojang.brigadier.suggestion.Suggestion;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import net.minecraft.client.util.math.Rect2i;
import net.skyblockisles.islesextra.chat.ChatPreview;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ChatInputSuggestor.SuggestionWindow.class)
public class SuggestionWindowMixin {

    @Shadow @Final private Rect2i area;

    @Inject(method = "<init>", at = @At("TAIL"))
    void shiftUpV2(ChatInputSuggestor chatInputSuggestor, int x, int y, int width, List<Suggestion> suggestions, boolean narrateFirstSuggestion, CallbackInfo ci) {
        this.area.setY(area.getY()-14);
        SuggestionWindowMixin.shiftedUp = true;
    }

    @Inject(method = "render", at = @At("HEAD"))
    void shiftUp(DrawContext context, int mouseX, int mouseY, CallbackInfo ci) {
        if (ChatPreview.shouldShiftUp() && !shiftedUp) {
            this.area.setY(area.getY()-14);
            SuggestionWindowMixin.shiftedUp = true;
        }
        else if (!ChatPreview.shouldShiftUp() && shiftedUp) {
            this.area.setY(area.getY()+14);
            SuggestionWindowMixin.shiftedUp = false;
        }
    }

    @Unique
    private static boolean shiftedUp = false;

}
