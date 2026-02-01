package net.skyblockisles.islesextra;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.skyblockisles.islesextra.annotations.Init;
import net.skyblockisles.islesextra.constants.Renderer;
import net.skyblockisles.islesextra.party.IslesParty;

import java.util.UUID;

public class Debug {

    @Init
    public static void init() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
            dispatcher.register(ClientCommandManager.literal("isles_debug")
            .then(ClientCommandManager.literal("party_add_self").executes(context -> {
                MinecraftClient mc = MinecraftClient.getInstance();
                if (mc.player != null)
                    togglePartyMember(mc.player.getUuid());
                return 1;
            }))
            .then(ClientCommandManager.literal("party_add_other").executes(context -> {
                MinecraftClient mc = MinecraftClient.getInstance();
                if (mc.targetedEntity instanceof PlayerEntity)
                    togglePartyMember(mc.targetedEntity.getUuid());
                else context.getSource().sendError(Text.literal("Not targeting a Player!"));
                return 1;
            }))
            .then(ClientCommandManager.literal("set_render_target").executes(context -> {
                MinecraftClient mc = MinecraftClient.getInstance();
                if (Renderer.getTarget() != null) Renderer.setTarget(null);
                else if (mc.targetedEntity != null) Renderer.setTarget(mc.targetedEntity);
                else context.getSource().sendError(Text.literal("Not targeting a valid Entity!"));
                return 1;
            }))
        ));
    }

    private static void togglePartyMember(UUID id) {
        if (IslesParty.isMember(id)) IslesParty.removeMember(id);
        else IslesParty.addMember(id);
    }
}