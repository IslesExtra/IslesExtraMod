package net.skyblockisles.islesextra.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget.PressAction;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {
  @Unique
    private static final ServerInfo islesInfo = new ServerInfo("Skyblock Isles", "play.skyblockisles.net", ServerInfo.ServerType.OTHER);
    static { islesInfo.setResourcePackPolicy(ServerInfo.ResourcePackPolicy.ENABLED); }

  protected TitleScreenMixin(Text text) {
    super(text);
  }

  @Inject(at = @At("RETURN"), method = "addNormalWidgets")
    private void addConnectButton(int y, int spacing, CallbackInfoReturnable<Integer> cir) {
        int buttonWidth = 150;
        int buttonHeight = 20;
        int xPos = this.width / 2 + buttonWidth;
        int yPos = y + spacing;

        var address = new ServerAddress("play.skyblockisles.net", 25565);
        var text = Text.translatable("text.islesextra.connectButton");
        PressAction onPress = button -> ConnectScreen.connect(this, MinecraftClient.getInstance(), address, islesInfo, false, null);

        var widget = ButtonWidget
            .builder(text, onPress)
            .size(buttonWidth, buttonHeight)
            .position(xPos, yPos)
            .build();

        this.addDrawableChild(widget);
    }

}