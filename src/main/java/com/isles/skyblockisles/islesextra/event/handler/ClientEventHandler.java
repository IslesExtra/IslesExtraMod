package com.isles.skyblockisles.islesextra.event.handler;

import com.isles.skyblockisles.islesextra.bossrush.dragon.MagmaBombWarning;
import com.isles.skyblockisles.islesextra.bossrush.frog.StomachExplosionWarning;
import com.isles.skyblockisles.islesextra.bossrush.turtle.CoconutBombWarning;
import com.isles.skyblockisles.islesextra.client.screen.IslesHudHandler;
import com.isles.skyblockisles.islesextra.constants.IslesBoss;
import com.isles.skyblockisles.islesextra.constants.IslesKeybindings;
import com.isles.skyblockisles.islesextra.client.screen.party.PartyManagmentScreen;
import com.isles.skyblockisles.islesextra.party.IslesParty;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.entity.mob.MagmaCubeEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public final class ClientEventHandler implements EventHandler {

  @Override
  public void register() {
    // TODO: Move to less expensive alternative
    ClientTickEvents.START_CLIENT_TICK.register(client -> {
      if (client.player == null || client.world == null) {
        return;
      }

      if (IslesKeybindings.OPEN_PARTY_SCREEN.getBinding().isPressed()) {
        client.setScreen(new PartyManagmentScreen(Text.translatable("party_managment.islesextra.name")));
      }

      if (!IslesHudHandler.inBoss) {
        return;
      }

      IslesBoss.getBoss().ifPresent(boss -> {
        switch (boss) {
          case FROG -> StomachExplosionWarning.init();
          case TURTLE -> CoconutBombWarning.init();
          default -> {}
        }
      });

      IslesParty.INSTANCE.lowHealthWarning();
    });

    ClientEntityEvents.ENTITY_LOAD.register(
        (entity, world) -> IslesBoss.getBoss().ifPresent(boss -> {

          switch (entity) {
            case MagmaCubeEntity magma -> {
              if (boss == IslesBoss.CRIMSON_DRAGON) {
                MagmaBombWarning.init();
              }
            }
            case ServerPlayerEntity player -> {
              if (IslesParty.INSTANCE.getMembers().contains(player.getGameProfile())) {
                IslesParty.INSTANCE.addMember(player.getGameProfile());
              }
            }
            default -> {}
          }
        }));

    ClientReceiveMessageEvents.GAME.register((message, overlay) -> {
      IslesParty.INSTANCE.handleMember(message.getString());
    });
  }
}
