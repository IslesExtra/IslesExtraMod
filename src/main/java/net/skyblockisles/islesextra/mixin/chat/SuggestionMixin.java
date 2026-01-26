package net.skyblockisles.islesextra.mixin.chat;

import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.suggestion.Suggestion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Suggestion.class)
public abstract class SuggestionMixin {

    @Shadow(remap = false) @Final private String text;

    @Shadow(remap = false) @Final private StringRange range;

    @Inject(method = "apply", at = @At("HEAD"), cancellable = true, remap = false)
    void applyEmoji(final String input, CallbackInfoReturnable<String> cir) {
        if (this.text.endsWith(":")) {
            String unicode = text.split(" ")[1];
            if (range.getStart() == 0 && range.getEnd() == input.length()) {
                cir.setReturnValue(unicode);
            }
            final StringBuilder result = new StringBuilder();
            if (range.getStart() > 0) {
                result.append(input, 0, range.getStart());
            }
            result.append(unicode);
            if (range.getEnd() < input.length()) {
                result.append(input.substring(range.getEnd()));
            }
            cir.setReturnValue(result.toString());
        }
    }

}
