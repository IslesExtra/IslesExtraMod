package com.isles.skyblockisles.islesextra.client;

import com.isles.skyblockisles.islesextra.IslesExtra;
import com.isles.skyblockisles.islesextra.client.commands.TestCommand;
import com.isles.skyblockisles.islesextra.client.discord.DiscordHandler;
import com.isles.skyblockisles.islesextra.event.JoinedIslesCallback;
import com.isles.skyblockisles.islesextra.event.LeftIslesCallback;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class IslesExtraClient implements ClientModInitializer {

    private static boolean ON_ISLES = false;
    public static boolean isOnIsles() { return ON_ISLES; }

    @Override
    public void onInitializeClient() {

        new DiscordHandler();

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
    }

}
