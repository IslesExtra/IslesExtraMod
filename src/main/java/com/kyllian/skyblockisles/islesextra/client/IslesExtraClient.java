package com.kyllian.skyblockisles.islesextra.client;

import com.kyllian.skyblockisles.islesextra.IslesExtra;
import com.kyllian.skyblockisles.islesextra.client.commands.TestCommand;
import com.kyllian.skyblockisles.islesextra.client.discord.DiscordHandler;
import com.kyllian.skyblockisles.islesextra.entity.custom.queen_bee.QueenBeeModel;
import com.kyllian.skyblockisles.islesextra.event.JoinedIslesCallback;
import com.kyllian.skyblockisles.islesextra.event.LeftIslesCallback;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;

@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
public class IslesExtraClient implements ClientModInitializer {

    private static boolean ON_ISLES = false;
    public static boolean isOnIsles() { return ON_ISLES; }

    @Override
    public void onInitializeClient() {

        new DiscordHandler();

        ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> {
            new TestCommand().register(dispatcher);
        }));

        // TODO; UNCOMMENT
        //EntityRendererRegistry.register(IslesEntities.QUEEN_BEE, QueenBeeRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(MODEL_QUEEN_BEE_LAYER, QueenBeeModel::getTexturedModelData);

        JoinedIslesCallback.EVENT.register(() -> {
            ON_ISLES = true;
            return ActionResult.SUCCESS;
        });

        LeftIslesCallback.EVENT.register(() -> {
            ON_ISLES = false;
            return ActionResult.SUCCESS;
        });

    }

    public static final EntityModelLayer MODEL_QUEEN_BEE_LAYER = new EntityModelLayer(new Identifier(IslesExtra.MOD_ID, "queen_bee"), "main");
}
