package com.isles.skyblockisles.islesextra.utils;

import com.isles.skyblockisles.islesextra.IslesExtra;
import com.isles.skyblockisles.islesextra.bossrush.dragon.MagmaBombWarning;
import com.isles.skyblockisles.islesextra.bossrush.frog.StomachExplosionWarning;
import com.isles.skyblockisles.islesextra.bossrush.turtle.CoconutBombWarning;
import com.isles.skyblockisles.islesextra.client.CustomText;
import com.isles.skyblockisles.islesextra.bossrush.general.LowAmmoWarning;
import com.isles.skyblockisles.islesextra.client.screen.IslesHudHandler;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.mob.MagmaCubeEntity;
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
            LowAmmoWarning.init();
            return TypedActionResult.pass(itemStack);
        }));

        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (ClientUtils.getPlayer() == null || ClientUtils.getWorld() == null) return;
            if (!IslesHudHandler.inBoss) return;
            if (ClientUtils.getBoss() == IslesConstants.Boss.FROG) StomachExplosionWarning.init();
            if (ClientUtils.getBoss() == IslesConstants.Boss.TURTLE) CoconutBombWarning.init();
        });

        ClientEntityEvents.ENTITY_LOAD.register((entity, world) -> {

            if (entity instanceof MagmaCubeEntity && ClientUtils.getBoss() == IslesConstants.Boss.CRIMSON_DRAGON) {
                MagmaBombWarning.init();
            }

        });

    }

}
