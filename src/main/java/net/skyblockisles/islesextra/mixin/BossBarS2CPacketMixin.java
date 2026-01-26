package net.skyblockisles.islesextra.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.entity.boss.BossBar;
import net.minecraft.network.packet.s2c.play.BossBarS2CPacket;
import net.minecraft.text.Text;

@Mixin(BossBarS2CPacket.class)
public abstract class BossBarS2CPacketMixin {

    /*
        Apparently, Isles has an Issue where they send a `null` name for the bossbar and vanilla cannot handle it.
        This should fix it by injecting and replacing it with a placeholder
    */
    @Redirect(
        method = "updateName", 
        at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/boss/BossBar;getName()Lnet/minecraft/text/Text;")
    )
    private static Text handleNullName(BossBar bossBar) {
        Text name = bossBar.getName();
        return (name != null) ? name : Text.literal("Unknown Boss");
    }
    
}
