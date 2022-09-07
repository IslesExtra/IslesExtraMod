package com.kyllian.skyblockisles.islesextra.entity.custom.queen_bee;

import com.kyllian.skyblockisles.islesextra.IslesExtra;
import com.kyllian.skyblockisles.islesextra.client.IslesExtraClient;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class QueenBeeRenderer extends MobEntityRenderer<QueenBeeEntity, QueenBeeModel> {
    public QueenBeeRenderer(EntityRendererFactory.Context context) {
        super(context, new QueenBeeModel(context.getPart(IslesExtraClient.MODEL_QUEEN_BEE_LAYER)), 2);

        ClientTickEvents.END_CLIENT_TICK.register((client -> {
            flap = !flap;
        }));
    }

    boolean flap = false;
    @Override
    public Identifier getTexture(QueenBeeEntity entity) {
        return !flap ? new Identifier(IslesExtra.MOD_ID, "textures/entity/queen_bee/queen_bee.png") : new Identifier(IslesExtra.MOD_ID, "textures/entity/queen_bee/queen_bee2.png");
    }
}
