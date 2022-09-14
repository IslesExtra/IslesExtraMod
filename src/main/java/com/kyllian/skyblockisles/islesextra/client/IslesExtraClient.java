package com.kyllian.skyblockisles.islesextra.client;

import com.kyllian.skyblockisles.islesextra.IslesExtra;
import com.kyllian.skyblockisles.islesextra.client.commands.MapCommand;
import com.kyllian.skyblockisles.islesextra.client.commands.TestCommand;
import com.kyllian.skyblockisles.islesextra.entity.IslesEntities;
import com.kyllian.skyblockisles.islesextra.entity.custom.queen_bee.QueenBeeModel;
import com.kyllian.skyblockisles.islesextra.entity.custom.queen_bee.QueenBeeRenderer;
import com.kyllian.skyblockisles.islesextra.gui.MapGui;
import com.kyllian.skyblockisles.islesextra.gui.TextInputGui;
import com.kyllian.skyblockisles.islesextra.utility.Dialogue;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

import java.nio.charset.StandardCharsets;

@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
public class IslesExtraClient implements ClientModInitializer {

    private static boolean ISLES = false;
    public static boolean isOnIsles() { return ISLES; }
    public static void setOnIsles(boolean onIsles) { ISLES = onIsles; }

    @Override
    public void onInitializeClient() {

        Dialogue.setupDialogue();

        ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> {
            new TestCommand().register(dispatcher);
            new MapCommand().register(dispatcher);
        }));

        EntityRendererRegistry.register(IslesEntities.QUEEN_BEE, QueenBeeRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(MODEL_QUEEN_BEE_LAYER, QueenBeeModel::getTexturedModelData);
    }

    public static final EntityModelLayer MODEL_QUEEN_BEE_LAYER = new EntityModelLayer(new Identifier(IslesExtra.MOD_ID, "queen_bee"), "main");
}
