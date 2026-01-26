package net.skyblockisles.islesextra.debug;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.command.ServerCommandSource;
import net.skyblockisles.islesextra.annotations.Init;
import net.skyblockisles.islesextra.constants.Renderer;

public class Testing {
    @Init
    public static void init() {
        LiteralArgumentBuilder<ServerCommandSource> builder = LiteralArgumentBuilder.literal("isles_target");
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(builder.executes(context -> {
                MinecraftClient client = MinecraftClient.getInstance();
                if (client.player == null) return 1;
                Renderer.setTarget(client.targetedEntity);
                return 1;
            }));
        });
    }
}
