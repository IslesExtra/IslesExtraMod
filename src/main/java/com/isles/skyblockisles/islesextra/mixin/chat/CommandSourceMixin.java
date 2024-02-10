package com.isles.skyblockisles.islesextra.mixin.chat;

import net.minecraft.command.CommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CommandSource.class)
public interface CommandSourceMixin {

    @Inject(method = "shouldSuggest", at = @At("HEAD"), cancellable = true)
    private static void shouldSuggest(String remaining, String candidate, CallbackInfoReturnable<Boolean> cir) {
        if (candidate.endsWith(":")) {
            candidate = candidate.split(" ")[1];
            if (candidate.startsWith(remaining)) cir.setReturnValue(true);
            else if (candidate.substring(1, candidate.length()-1).contains(remaining.substring(1))) cir.setReturnValue(true);
            else cir.setReturnValue(false);
        }
        else if (remaining.startsWith("@")) {
            candidate = candidate.substring(1);
            if (candidate.startsWith(remaining)) cir.setReturnValue(true);
            else if (candidate.contains(remaining.substring(1))) cir.setReturnValue(true);
            else cir.setReturnValue(false);
        }

    }

}
