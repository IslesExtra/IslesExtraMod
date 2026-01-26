package net.skyblockisles.islesextra.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import net.skyblockisles.islesextra.callback.IslesLocationChangedCallback;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

  @Shadow
  @Nullable
  public ClientPlayerEntity player;


  @Inject(method = "joinWorld", at = @At("HEAD"))
  void onJoinWorld(ClientWorld world, CallbackInfo ci) {
    RegistryKey<World> key = world.getRegistryKey();
    String[] parts = key.getValue().getPath().split("-");
    if (parts.length < 2) {
      IslesLocationChangedCallback.EVENT.invoker().interact("", "");
      return;
    }
    String location = parts[0];
    String instanceId = parts[1];
    IslesLocationChangedCallback.EVENT.invoker().interact(location, instanceId);
  }
}
