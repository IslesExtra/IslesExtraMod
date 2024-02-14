package com.isles.skyblockisles.islesextra.mixin;

import com.isles.skyblockisles.islesextra.client.screen.advancement.CustomAdvancementsScreen;
import com.isles.skyblockisles.islesextra.event.IslesLocationChangedCallback;
import com.isles.skyblockisles.islesextra.event.OpenedIslesGuiCallback;
import com.isles.skyblockisles.islesextra.general.party.HighlightMembers;
import com.isles.skyblockisles.islesextra.utils.ClientUtils;
import com.isles.skyblockisles.islesextra.utils.IslesConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.advancement.AdvancementsScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

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

    @Inject(method = "joinWorld", at = @At("HEAD"))
    void onJoinWorld(ClientWorld world, CallbackInfo ci) {
        RegistryKey<World> key = world.getRegistryKey();
        String[] parts = key.getValue().getPath().split("-");
        if (parts.length < 2) {
            ClientUtils.updateLocationData("", "");
            IslesLocationChangedCallback.EVENT.invoker().interact("");
            return;
        }
        String location = parts[0];
        String instanceId = parts[1];
        ClientUtils.updateLocationData(location, instanceId);
        IslesLocationChangedCallback.EVENT.invoker().interact(location);
    }

    @Inject(method = "hasOutline", at = @At("HEAD"), cancellable = true)
    void glowPartyPlayers(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof PlayerEntity playerEntity) {
            if (HighlightMembers.getGlowingPlayers().contains(playerEntity)) cir.setReturnValue(true);
        }
    }

}
