package com.isles.skyblockisles.islesextra.mixin.chat;

import net.minecraft.client.gui.screen.ChatInputSuggestor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ChatInputSuggestor.class)
public interface CompletingSuggestionsAccessor {
    @Accessor("completingSuggestions")
    void setCompletingSuggestions(boolean completingSuggestions);

    @Accessor("completingSuggestions")
    boolean isCompletingSuggestions();

}
