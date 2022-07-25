package com.kyllian.islesextra.islesextra.client;

import com.kyllian.islesextra.islesextra.IslesExtra;
import com.kyllian.islesextra.islesextra.gui.MobScreen;
import com.kyllian.islesextra.islesextra.gui.TextInputGui;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
public class IslesExtraClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        ClientCommandManager.DISPATCHER.register(ClientCommandManager.literal("foo").executes(context -> {
            ClientUtils.sendMessage("foo");
            assert MinecraftClient.getInstance().player != null;
            ClientUtils.setTracker(MinecraftClient.getInstance().player.getPos().add(10, 0, 0));
            return 1;
        }));
        ClientCommandManager.DISPATCHER.register(ClientCommandManager.literal("test").executes(context -> {
            ClientUtils.sendMessage("test");
            ClientUtils.openScreen(new MobScreen());
            return 1;
        }));
        ClientCommandManager.DISPATCHER.register(ClientCommandManager.literal("input").executes(context -> {
            ClientUtils.sendMessage("input");
            ClientUtils.sendMessage(new CustomText("&#4287f5Test &fHey &#5af542Welp").getValue());

            IslesExtra.nextScreen = new TextInputGui(MinecraftClient.getInstance().player.getInventory());

            return 1;
        }));
    }
}
