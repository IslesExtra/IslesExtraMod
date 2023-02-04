package com.kyllian.skyblockisles.islesextra.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin {

    @Shadow protected abstract @Nullable PlayerListEntry getPlayerListEntry();

    @Inject(method = "getCapeTexture", at = @At("TAIL"), cancellable = true)
    void getCape(CallbackInfoReturnable<Identifier> cir) {
        PlayerListEntry playerListEntry = this.getPlayerListEntry();
        cir.setReturnValue(playerListEntry == null ? null : playerListEntry.getCapeTexture());
    }

}
