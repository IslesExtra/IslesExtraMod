package com.isles.skyblockisles.islesextra.mixin;

import com.isles.skyblockisles.islesextra.general.party.IslesParty;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {

    @Inject(method = "getTeamColorValue", at = @At("HEAD"), cancellable = true)
    void recolorGlow(CallbackInfoReturnable<Integer> cir) {
        Entity entity = (Entity) (Object) this;
        if (!(entity instanceof PlayerEntity playerEntity)) return;
        if (IslesParty.isInParty(playerEntity)) cir.setReturnValue(0x3ca4e6);
    }

}
