package com.isles.skyblockisles.islesextra.client;

import com.isles.skyblockisles.islesextra.client.commands.TestCommand;
import com.isles.skyblockisles.islesextra.client.discord.DiscordHandler;
import com.isles.skyblockisles.islesextra.client.discord.DiscordRPPayload;
import com.isles.skyblockisles.islesextra.client.screen.IslesHudHandler;
import com.isles.skyblockisles.islesextra.event.JoinedIslesCallback;
import com.isles.skyblockisles.islesextra.event.LeftIslesCallback;
import com.isles.skyblockisles.islesextra.event.OpenedIslesGuiCallback;
import com.isles.skyblockisles.islesextra.utils.ChatPreviewPayload;
import com.isles.skyblockisles.islesextra.utils.IslesConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.util.ActionResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Environment(EnvType.CLIENT)
public class IslesExtraClient implements ClientModInitializer {

    private static final Logger LOGGER = LogManager.getLogger();
    private static boolean ON_ISLES = false;
    public static boolean isOnIsles() { return ON_ISLES; }
    private static IslesConstants.Gui openedGui = IslesConstants.Gui.NONE;
    public static IslesConstants.Gui getOpenedGui() { return openedGui; }

    @Override
    public void onInitializeClient() {

        PayloadTypeRegistry.playS2C().register(DiscordRPPayload.ID, DiscordRPPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(ChatPreviewPayload.ID, ChatPreviewPayload.CODEC);

        DiscordHandler.start();
        IslesHudHandler.register();

    }

    public static void registerClientEvents() {
        ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> new TestCommand().register(dispatcher)));

        JoinedIslesCallback.EVENT.register(() -> {
            ON_ISLES = true;
            LOGGER.info("JOINED ISLES");
            return ActionResult.PASS;
        });

        LeftIslesCallback.EVENT.register(() -> {
            ON_ISLES = false;
            LOGGER.info("LEFT ISLES");
            return ActionResult.PASS;
        });

        OpenedIslesGuiCallback.EVENT.register((gui) -> {
            if (gui != IslesConstants.Gui.ESCAPE_MENU) openedGui = gui;
            return ActionResult.PASS;
        });
    }

}
