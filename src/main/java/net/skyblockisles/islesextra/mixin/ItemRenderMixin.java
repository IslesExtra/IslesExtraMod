package net.skyblockisles.islesextra.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.skyblockisles.islesextra.IslesClientState;
import net.skyblockisles.islesextra.constants.IslesRarity;
import net.skyblockisles.islesextra.services.ItemRarityService;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public class ItemRenderMixin extends Screen {

    protected ItemRenderMixin(Text text) {
        super(text);
    }

    @Inject(method = "drawSlot", at = @At(value = "HEAD"))
    public void renderCustomItemRarity(DrawContext drawContext, Slot slot, int i, int j, CallbackInfo ci) {
        String serverString = IslesClientState.getIslesServerNameAndVersion();
        if (serverString != null && !serverString.contains("Hub")) {
            int x = slot.x, y = slot.y;
            IslesRarity tier = ItemRarityService.getTierOfItem(slot.getStack());
            if (tier != IslesRarity.NONE) {
                int color = ItemRarityService.getColorForTier(tier);
                ItemRarityService.renderItemRarity(drawContext, x, y, color);
            }
        }
    }

}
