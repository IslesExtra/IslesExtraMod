package com.isles.skyblockisles.islesextra.client;

import com.isles.skyblockisles.islesextra.client.commands.TestCommand;
import com.isles.skyblockisles.islesextra.client.discord.DiscordHandler;
import com.isles.skyblockisles.islesextra.client.screen.IslesHudHandler;
import com.isles.skyblockisles.islesextra.event.JoinedIslesCallback;
import com.isles.skyblockisles.islesextra.event.LeftIslesCallback;
import com.isles.skyblockisles.islesextra.event.OpenedIslesGuiCallback;
import com.isles.skyblockisles.islesextra.utils.IslesConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.util.ActionResult;

@Environment(EnvType.CLIENT)
public class IslesExtraClient implements ClientModInitializer {

    private static boolean ON_ISLES = false;
    public static boolean isOnIsles() { return ON_ISLES; }
    private static IslesConstants.Gui openedGui = IslesConstants.Gui.NONE;
    public static IslesConstants.Gui getOpenedGui() { return openedGui; }

    @Override
    public void onInitializeClient() {

        new DiscordHandler();
        IslesHudHandler.register();

    }

    public static void registerClientEvents() {
        ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> {
            new TestCommand().register(dispatcher);
        }));

        JoinedIslesCallback.EVENT.register(() -> {
            ON_ISLES = true;
            System.out.println("JOINED ISLES");
            return ActionResult.PASS;
        });

        LeftIslesCallback.EVENT.register(() -> {
            ON_ISLES = false;
            System.out.println("LEFT ISLES");
            return ActionResult.PASS;
        });

        OpenedIslesGuiCallback.EVENT.register((gui) -> {
            if (gui != IslesConstants.Gui.ESCAPE_MENU) openedGui = gui;
            return ActionResult.PASS;
        });
    }

}
