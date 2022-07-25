package com.kyllian.islesextra.islesextra;

import ca.weblite.objc.Client;
import com.kyllian.islesextra.islesextra.client.ClientData;
import com.kyllian.islesextra.islesextra.client.CustomText;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import java.util.ArrayList;
import java.util.List;

public class IslesExtra implements ModInitializer {

    public static Screen nextScreen = null;

    public final static String MOD_ID = "islesextra";

    private List <ItemStack> oldItems;

    @Override
    public void onInitialize() {
        ClientTickEvents.END_CLIENT_TICK.register((client -> {
            if (nextScreen!=null) {
                client.setScreen(nextScreen);
                nextScreen = null;
            }

            ClientData.getInventoryDifference();
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

    }
}
