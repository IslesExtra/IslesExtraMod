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
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.mob.MagmaCubeEntity;
import net.minecraft.util.ActionResult;

import java.awt.*;
import java.util.Optional;


public class InitUtils {
    public static void events() {

        ClientTickEvents.END_CLIENT_TICK.register((client -> {
            if (!IslesExtra.tasks.isEmpty()) {
                IslesExtra.tasks.forEach(Runnable::run);
                IslesExtra.tasks.clear();
            }
        }));

        ItemTooltipCallback.EVENT.register(((stack, context, type, lines) -> {
            var nbtComponent = stack.get(DataComponentTypes.CUSTOM_DATA);
            if (nbtComponent == null) return;
            var nbt = nbtComponent.copyNbt();
            // TODO; figure out wtf this does again
            for (int i = 0; nbt.contains(IslesExtra.MOD_ID + ".lore." + i); i++) {
                Optional<String> newline = nbt.getString(IslesExtra.MOD_ID + ".lore." + i);
                newline.ifPresent(s -> lines.add(new CustomText(s).getValue()));
            }
        }));

        UseItemCallback.EVENT.register(((player, world, hand) -> {
            LowAmmoWarning.init();
            return ActionResult.PASS;
        }));

        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (ClientUtils.getPlayer() == null || ClientUtils.getWorld() == null) return;
            if (!IslesHudHandler.inBoss) return;
            ClientUtils.getBoss().ifPresent(boss -> {
                switch (boss) {
                    case FROG -> StomachExplosionWarning.init();
                    case TURTLE -> CoconutBombWarning.init();
                }
            });
        });

        ClientEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            ClientUtils.getBoss().ifPresent(boss -> {
                if (entity instanceof MagmaCubeEntity && boss == IslesConstants.Boss.CRIMSON_DRAGON) {
                    MagmaBombWarning.init();
                }
            });
        });

    }

}
