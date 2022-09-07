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
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
public class IslesExtraClient implements ClientModInitializer {

    public static MapGui map = new MapGui();

    @Override
    public void onInitializeClient() {

        Dialogue.setupDialogue();

        ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> {
            new TestCommand().register(dispatcher);
            new MapCommand().register(dispatcher);

            dispatcher.register(ClientCommandManager.literal("input").executes(context -> {
                ClientUtils.sendMessage("input");
                ClientUtils.sendMessage(new CustomText("&#4287f5Test &fHey &#5af542Welp").getValue());

                IslesExtra.nextScreen = new TextInputGui(MinecraftClient.getInstance().player.getInventory());
                return 1;
            }));
        }));

        EntityRendererRegistry.register(IslesEntities.QUEEN_BEE, QueenBeeRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(MODEL_QUEEN_BEE_LAYER, QueenBeeModel::getTexturedModelData);
    }

    public static final EntityModelLayer MODEL_QUEEN_BEE_LAYER = new EntityModelLayer(new Identifier(IslesExtra.MOD_ID, "queen_bee"), "main");
}
