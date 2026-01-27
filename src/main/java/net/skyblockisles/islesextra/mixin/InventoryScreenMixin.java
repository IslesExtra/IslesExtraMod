package net.skyblockisles.islesextra.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.ingame.RecipeBookScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import net.skyblockisles.islesextra.constants.IslesShortcutWidget;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends RecipeBookScreen<PlayerScreenHandler> {

    protected InventoryScreenMixin(PlayerScreenHandler handler, RecipeBookWidget<?> widget, PlayerInventory inventory, Text text) {
		super(handler, widget, inventory, text);
	}

    @Inject(method = "init", at = @At("TAIL"))
    void addShortcutWidgets(CallbackInfo info) {
        for (IslesShortcutWidget shortcut : IslesShortcutWidget.getWidgets(this.x, this.y))
            addDrawableChild(shortcut);
    }
}
