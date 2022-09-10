package com.kyllian.skyblockisles.islesextra;

import com.kyllian.skyblockisles.islesextra.client.ClientData;
import com.kyllian.skyblockisles.islesextra.client.CustomText;
import com.kyllian.skyblockisles.islesextra.client.discord.DiscordHandler;
import com.kyllian.skyblockisles.islesextra.entity.IslesEntities;
import com.kyllian.skyblockisles.islesextra.entity.custom.queen_bee.QueenBeeEntity;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.nbt.NbtCompound;

public class IslesExtra implements ModInitializer {

    public static Screen nextScreen = null;

    public final static String MOD_ID = "islesextra";

    public static DiscordHandler discord;

    @Override
    public void onInitialize() {

        discord = new DiscordHandler();

        ClientTickEvents.END_CLIENT_TICK.register((client -> {
            if (nextScreen!=null) {
                client.setScreen(nextScreen);
                nextScreen = null;
            }

            if (client.player == null) return;
            ClientData.updatePickedUpItems();
        }));
        ItemTooltipCallback.EVENT.register(((stack, context, lines) -> {
            NbtCompound nbt = stack.getNbt();
            if (nbt != null) {
                int i = 0;
                while (nbt.contains(IslesExtra.MOD_ID + ".lore." + i)) {
                    lines.add(new CustomText(nbt.getString(IslesExtra.MOD_ID + ".lore." + i)).getValue());
                    i++;
                }
            }
        }));
        registerAttributes();
    }

    private static void registerAttributes() {
        FabricDefaultAttributeRegistry.register(IslesEntities.QUEEN_BEE, QueenBeeEntity.createMobAttributes());
    }
}
