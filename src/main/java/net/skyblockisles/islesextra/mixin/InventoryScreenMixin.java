package net.skyblockisles.islesextra.mixin;

import net.minecraft.client.gui.DrawContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
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

    @Unique
    private final java.util.List<IslesShortcutWidget> isles$shortcuts = new java.util.ArrayList<>();

    protected InventoryScreenMixin(PlayerScreenHandler handler, RecipeBookWidget<?> widget, PlayerInventory inventory, Text text) {
		super(handler, widget, inventory, text);
	}

    @Inject(method = "init", at = @At("TAIL"))
    void addShortcutWidgets(CallbackInfo info) {
        isles$shortcuts.clear();

        for (IslesShortcutWidget shortcut : IslesShortcutWidget.getWidgets(this.x, this.y)) {
            isles$shortcuts.add(shortcut);
            this.addSelectableChild(shortcut);
        }
    }

    @Inject(method = "drawBackground", at = @At("HEAD"))
    void renderShortcutsBehind(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo info) {
        for (IslesShortcutWidget shortcut : isles$shortcuts)
            shortcut.render(context, mouseX, mouseY, delta);
    }
}
