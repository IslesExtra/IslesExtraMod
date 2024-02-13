package com.isles.skyblockisles.islesextra.utils;

import com.isles.skyblockisles.islesextra.IslesExtra;
import com.isles.skyblockisles.islesextra.bossrush.dragon.MagmaBombWarning;
import com.isles.skyblockisles.islesextra.bossrush.frog.StomachExplosionWarning;
import com.isles.skyblockisles.islesextra.bossrush.turtle.CoconutBombWarning;
import com.isles.skyblockisles.islesextra.client.CustomText;
import com.isles.skyblockisles.islesextra.bossrush.general.LowAmmoWarning;
import com.isles.skyblockisles.islesextra.client.screen.IslesHudHandler;
import com.isles.skyblockisles.islesextra.event.IslesLocationChangedCallback;
import com.isles.skyblockisles.islesextra.general.party.HighlightMembers;
import com.isles.skyblockisles.islesextra.general.party.LowHealthWarning;
import com.isles.skyblockisles.islesextra.general.party.PartyManagmentScreen;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.mob.MagmaCubeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;
import org.lwjgl.glfw.GLFW;

import java.util.Arrays;
import java.util.List;


public class InitUtils {
    public static final KeyBinding openPartyManagment = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "keybinds.islesextra.open_party",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_P,
            "category.islesextra.general"
    ));
    public static void events() {

        ClientTickEvents.END_CLIENT_TICK.register((client -> {
            if (!IslesExtra.tasks.isEmpty()) {
                IslesExtra.tasks.forEach(Runnable::run);
                IslesExtra.tasks.clear();
            }

            if (client.player == null) return;
            if (openPartyManagment != null && openPartyManagment.isPressed()) {
                client.setScreen(new PartyManagmentScreen(Text.translatable("party_managment.islesextra.name")));
            }

        }));

        IslesLocationChangedCallback.EVENT.register(location -> {
            HighlightMembers.init();
            return ActionResult.PASS;

        });

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
            LowHealthWarning.init();
        });

        ClientEntityEvents.ENTITY_LOAD.register((entity, world) -> {

            if (entity instanceof MagmaCubeEntity && ClientUtils.getBoss() == IslesConstants.Boss.CRIMSON_DRAGON) {
                MagmaBombWarning.init();
            }

            if (entity instanceof ServerPlayerEntity player && PartyUtils.getMembers().contains(player.getGameProfile())) {
                PartyUtils.partyMemberEntities.add((PlayerEntity) entity);
            }


        });

        ClientReceiveMessageEvents.GAME.register((message, overlay) -> {
            PartyUtils.handleMember(message.getString());
        });

        ClientSendMessageEvents.COMMAND.register(command -> {
            List<String> commandArray = Arrays.asList(command.split(" "));

            if (commandArray.get(0).equals(IslesExtra.MOD_ID)) {
                if (commandArray.get(1).equals("debug")) {
                    ClientUtils.getClient().setScreen(new PartyManagmentScreen(Text.translatable("party_managment_screen.islesextra.name")));
                }

            }

        });

    }

}
