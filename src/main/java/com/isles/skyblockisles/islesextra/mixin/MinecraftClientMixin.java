package com.isles.skyblockisles.islesextra.mixin;

import com.isles.skyblockisles.islesextra.IslesClientState;
import com.isles.skyblockisles.islesextra.constants.IslesGui;
import com.isles.skyblockisles.islesextra.event.IslesLocationChangedCallback;
import com.isles.skyblockisles.islesextra.event.OpenedIslesGuiCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

  @Shadow
  @Nullable
  public ClientPlayerEntity player;

  @ModifyVariable(method = "setScreen", at = @At("HEAD"), argsOnly = true)
  Screen setScreen(Screen screen) {
    if (screen == null) {
      return null;
    }
    if (this.player == null) {
      return screen;
    }
    if (screen.getTitle() != null && (screen.getTitle().getString().startsWith("Reconfig")
        || screen.getTitle().getString().isEmpty())) {
      return screen;
    }

    String screenName = screen.getTitle().getString();
    OpenedIslesGuiCallback.EVENT.invoker().interact(IslesGui.guiFromScreenName(screenName));

    return screen;
  }

  @Inject(method = "joinWorld", at = @At("HEAD"))
  void onJoinWorld(ClientWorld world, CallbackInfo ci) {
    RegistryKey<World> key = world.getRegistryKey();
    String[] parts = key.getValue().getPath().split("-");
    if (parts.length < 2) {
      IslesClientState.updateLocationData("", "");
      IslesLocationChangedCallback.EVENT.invoker().interact("");
      return;
    }
    String location = parts[0];
    String instanceId = parts[1];
    IslesClientState.updateLocationData(location, instanceId);
    IslesLocationChangedCallback.EVENT.invoker().interact(location);
  }
}
