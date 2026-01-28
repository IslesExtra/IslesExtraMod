package net.skyblockisles.islesextra.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.skyblockisles.islesextra.party.IslesParty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {

  @Inject(method = "isGlowing", at = @At("HEAD"), cancellable = true)
  void addGlow(CallbackInfoReturnable<Boolean> cir) {
    Entity entity = (Entity) (Object) this;

    if (!(entity instanceof PlayerEntity player)) return;
    if (!IslesParty.isMember(player.getUuid())) return;

    cir.setReturnValue(true);
  }

  @Inject(method = "getTeamColorValue", at = @At("HEAD"), cancellable = true)
  void recolorGlow(CallbackInfoReturnable<Integer> cir) {
    Entity entity = (Entity) (Object) this;

    if (!(entity instanceof PlayerEntity player)) return;
    if (!IslesParty.isMember(player.getUuid())) return;

    long time = System.currentTimeMillis();
    double speed = 0.002;

    int r = (int) (Math.sin(speed * time + 0) * 127 + 128);
    int g = (int) (Math.sin(speed * time + 2) * 127 + 128);
    int b = (int) (Math.sin(speed * time + 4) * 127 + 128);

    int rgb = (r << 16) | (g << 8) | b;

    cir.setReturnValue(rgb);
  }
}
