package com.isles.skyblockisles.islesextra.mixin.chat;

import com.isles.skyblockisles.islesextra.chat.ChatSuggestions;
import com.isles.skyblockisles.islesextra.utils.ClientUtils;
import net.minecraft.client.network.ClientCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.Set;

@Mixin(ClientCommandSource.class)
public class ClientCommandSourceMixin {

    @Inject(method = "getChatSuggestions", at = @At("HEAD"), cancellable = true)
    void getChatSuggestions(CallbackInfoReturnable<Collection<String>> cir) {
        Set<String> suggestions = ChatSuggestions.getSuggestions();
        if (suggestions.isEmpty()) {
            cir.setReturnValue(ClientUtils.getPlayerNames(false));
        }
        else cir.setReturnValue(suggestions);
    }

}
