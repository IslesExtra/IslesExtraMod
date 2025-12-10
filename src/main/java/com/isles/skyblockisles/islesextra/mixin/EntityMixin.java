package com.isles.skyblockisles.islesextra.mixin;

import com.isles.skyblockisles.islesextra.party.IslesParty;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {

  @Inject(method = "isGlowing", at = @At("HEAD"), cancellable = true)
  void recolorGlow(CallbackInfoReturnable<Boolean> cir) {
    Entity entity = (Entity) (Object) this;
    if (!(entity instanceof PlayerEntity playerEntity)) {
      return;
    }

    if (IslesParty.INSTANCE.isMember(playerEntity.getGameProfile())) {
      cir.setReturnValue(true);
    }
  }
}
