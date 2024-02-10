package com.isles.skyblockisles.islesextra.utils;

import com.isles.skyblockisles.islesextra.IslesExtra;
import com.isles.skyblockisles.islesextra.bossrush.general.OnlyPartyMessages;
import com.isles.skyblockisles.islesextra.client.CustomText;
import com.isles.skyblockisles.islesextra.bossrush.general.LowAmmoWarning;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.TypedActionResult;


public class InitUtils {
    public static void events() {

        ClientTickEvents.END_CLIENT_TICK.register((client -> {
            if (!IslesExtra.tasks.isEmpty()) {
                IslesExtra.tasks.forEach(Runnable::run);
                IslesExtra.tasks.clear();
            }
        }));

        ItemTooltipCallback.EVENT.register(((stack, context, lines) -> {
            NbtCompound nbt = stack.getNbt();
            if (nbt == null) return;
            // TODO; figure out wtf this does again
            for (int i = 0; nbt.contains(IslesExtra.MOD_ID + ".lore." + i); i++) {
                lines.add(new CustomText(nbt.getString(IslesExtra.MOD_ID + ".lore." + i)).getValue());
            }
        }));

        UseItemCallback.EVENT.register(((player, world, hand) -> {
            ItemStack itemStack = player.getStackInHand(hand);
            LowAmmoWarning.warnOnLowAmmo();
            return TypedActionResult.pass(itemStack);
        }));

        ClientReceiveMessageEvents.ALLOW_GAME.register(((message, overlay) -> {
        return true;
        //To enable, just remove Comment
        //Don't quite know if it works yet, but you can definitely see messages with your name in it tho
        //return OnlyPartyMessages.onlyPartyMessages(message.getString());

        }));

        ClientSendMessageEvents.MODIFY_CHAT.register(message -> {
            return "&6" + message;
        });

    }

}
