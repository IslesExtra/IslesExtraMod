package com.kyllian.skyblockisles.islesextra.client;

import com.kyllian.skyblockisles.islesextra.IslesExtra;
import com.kyllian.skyblockisles.islesextra.annotation.OnIsles;
import com.kyllian.skyblockisles.islesextra.annotation.OnIslesJoin;
import com.kyllian.skyblockisles.islesextra.annotation.OnIslesLeave;
import com.kyllian.skyblockisles.islesextra.client.commands.TestCommand;
import com.kyllian.skyblockisles.islesextra.entity.IslesEntities;
import com.kyllian.skyblockisles.islesextra.entity.custom.queen_bee.QueenBeeModel;
import com.kyllian.skyblockisles.islesextra.entity.custom.queen_bee.QueenBeeRenderer;
import com.kyllian.skyblockisles.islesextra.utility.Dialogue;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
public class IslesExtraClient implements ClientModInitializer {

    private static boolean ON_ISLES = false;
    public static boolean isOnIsles() { return ON_ISLES; }

    @Override
    public void onInitializeClient() {

        Dialogue.setupDialogue();
        LockSlots.init();

        ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> {
            new TestCommand().register(dispatcher);
        }));

        // TODO; UNCOMMENT
        //EntityRendererRegistry.register(IslesEntities.QUEEN_BEE, QueenBeeRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(MODEL_QUEEN_BEE_LAYER, QueenBeeModel::getTexturedModelData);
    }


    @OnIslesJoin
    static void clientJoinedIsles() { ON_ISLES = true; }

    @OnIslesLeave
    static void clientLeftIsles() { ON_ISLES = false; }

    @OnIsles("ALL")
    static void onIslesAction(String action) {
        System.out.println("Isles event: " + action);
    }

    public static final EntityModelLayer MODEL_QUEEN_BEE_LAYER = new EntityModelLayer(new Identifier(IslesExtra.MOD_ID, "queen_bee"), "main");
}
