package com.isles.skyblockisles.islesextra.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {

    protected TitleScreenMixin (Text title) {
        super(title);
    }


    @Unique
    private static final ServerInfo islesInfo = new ServerInfo("Skyblock Isles", "play.skyblockisles.net", ServerInfo.ServerType.OTHER);
    @Inject(at = @At("RETURN"), method = "initWidgetsNormal")
    private void addConnectButton(int y, int spacingY, CallbackInfo ci) {
        this.addDrawableChild(ButtonWidget.builder(Text.translatable("text.islesextra.connectButton"), (button) ->
                ConnectScreen.connect(new MultiplayerScreen(new TitleScreen()),
                MinecraftClient.getInstance(),
                new ServerAddress("play.skyblockisles.net", 25565),
                islesInfo,
                false)).dimensions(352, 291, 150, 20).build());
    }

}