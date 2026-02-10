package net.skyblockisles.islesextra.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.skyblockisles.islesextra.constants.IslesRarity;
import net.skyblockisles.islesextra.services.ItemRarityService;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class HotbarMixin {

    @Inject(method = "renderHotbarItem", at = @At(value = "HEAD"))
    public void renderCustomItemText(DrawContext drawContext, int x, int y, RenderTickCounter renderTickCounter, PlayerEntity playerEntity, ItemStack itemStack, int k, CallbackInfo ci) {
        IslesRarity tier = ItemRarityService.getTierOfItem(itemStack);
        if (tier != IslesRarity.NONE) {
            int color = ItemRarityService.getColorForTier(tier);
            ItemRarityService.renderItemRarity(drawContext, x, y, color);
        }
    }

}
