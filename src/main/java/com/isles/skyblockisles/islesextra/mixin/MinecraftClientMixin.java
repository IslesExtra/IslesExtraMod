package com.isles.skyblockisles.islesextra.mixin;

import com.isles.skyblockisles.islesextra.client.screen.CustomAdvancementsScreen;
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
        if (!(screen instanceof AdvancementsScreen)) return screen;
        if (this.player == null) return null;
        return new CustomAdvancementsScreen(this.player.networkHandler.getAdvancementHandler());
    }

}
