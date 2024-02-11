package com.isles.skyblockisles.islesextra.mixin;

import com.isles.skyblockisles.islesextra.client.screen.advancement.CustomAdvancementsScreen;
import com.isles.skyblockisles.islesextra.event.OpenedIslesGuiCallback;
import com.isles.skyblockisles.islesextra.utils.IslesConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.advancement.AdvancementsScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Shadow @Nullable public ClientPlayerEntity player;
    @ModifyVariable(method = "setScreen", at = @At("HEAD"), argsOnly = true)
    // intercept mojang goofy advancement screen and replace with cool as fuck advancement screen
    Screen setScreen(Screen screen) {
        if (screen == null) return null;
        if (this.player == null) return screen;
        if (screen instanceof AdvancementsScreen) return new CustomAdvancementsScreen(this.player.networkHandler.getAdvancementHandler());
        if (screen.getTitle() != null && (screen.getTitle().getString().startsWith("Reconfig") || screen.getTitle().getString().isEmpty())) return screen;

        String screenName = screen.getTitle().getString();
        // DEBUG
        /*char[] characters = screenName.toCharArray();
        for (char character : characters) System.out.println("Character: " + ((int) character));*/

        // call event
        OpenedIslesGuiCallback.EVENT.invoker().interact(IslesConstants.guiFromScreenName(screenName));

        return screen;
    }

}
