package net.skyblockisles.islesextra.mixin.chat;

import net.minecraft.command.CommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CommandSource.class)
public interface CommandSourceMixin {

    @Inject(method = "shouldSuggest", at = @At("HEAD"), cancellable = true)
    private static void shouldSuggest(String remaining, String candidate, CallbackInfoReturnable<Boolean> cir) {
        if (remaining.isEmpty()) {
            cir.setReturnValue(true);
            return;
        }
        if (candidate.endsWith(":") && candidate.startsWith(":")) {
            String cleanRemaining = remaining.startsWith(":") ? remaining.substring(1) : remaining;
            String cleanCandidate = candidate.substring(1, candidate.length() - 1);
            cir.setReturnValue(cleanCandidate.toLowerCase().contains(cleanRemaining.toLowerCase()));
        }
        else if (remaining.startsWith("@")) {
            String query = remaining.length() > 1 ? remaining.substring(1).toLowerCase() : "";
            String target = candidate.toLowerCase();
            cir.setReturnValue(query.isEmpty() || target.contains(query) || target.startsWith(remaining.toLowerCase()));
        }

    }

}
