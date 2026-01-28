package net.skyblockisles.islesextra.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.skyblockisles.islesextra.config.IslesConfig;
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

    if (!IslesConfig.HANDLER.instance().partyGlowEnable) return;
    if (!(entity instanceof PlayerEntity player)) return;
    if (!IslesParty.isMember(player.getUuid())) return;

    cir.setReturnValue(true);
  }

  @Inject(method = "getTeamColorValue", at = @At("HEAD"), cancellable = true)
  void recolorGlow(CallbackInfoReturnable<Integer> cir) {
    Entity entity = (Entity) (Object) this;

    if (!IslesConfig.HANDLER.instance().partyGlowEnable) return;
    if (!(entity instanceof PlayerEntity player)) return;
    if (!IslesParty.isMember(player.getUuid())) return;

    int rgb = IslesConfig.HANDLER.instance().partyGlowColor.getRGB();

    cir.setReturnValue(rgb);
  }
}
