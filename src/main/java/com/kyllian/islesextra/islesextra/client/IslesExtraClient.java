package com.kyllian.islesextra.islesextra.client;

import com.kyllian.islesextra.islesextra.IslesExtra;
import com.kyllian.islesextra.islesextra.client.commands.TestCommand;
import com.kyllian.islesextra.islesextra.gui.MapGui;
import com.kyllian.islesextra.islesextra.gui.MobScreen;
import com.kyllian.islesextra.islesextra.gui.TextInputGui;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.minecraft.client.MinecraftClient;

@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
public class IslesExtraClient implements ClientModInitializer {

    public static MapGui map = new MapGui();

    @Override
    public void onInitializeClient() {

        new TestCommand().register(ClientCommandManager.DISPATCHER);

        ClientCommandManager.DISPATCHER.register(ClientCommandManager.literal("input").executes(context -> {
            ClientUtils.sendMessage("input");
            ClientUtils.sendMessage(new CustomText("&#4287f5Test &fHey &#5af542Welp").getValue());

            IslesExtra.nextScreen = new TextInputGui(MinecraftClient.getInstance().player.getInventory());
            return 1;
        }));
    }
}
