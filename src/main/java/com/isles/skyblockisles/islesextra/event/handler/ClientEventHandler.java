package com.isles.skyblockisles.islesextra.event.handler;

import com.isles.skyblockisles.islesextra.bossrush.dragon.MagmaBombWarning;
import com.isles.skyblockisles.islesextra.bossrush.frog.StomachExplosionWarning;
import com.isles.skyblockisles.islesextra.bossrush.turtle.CoconutBombWarning;
import com.isles.skyblockisles.islesextra.client.screen.IslesHudHandler;
import com.isles.skyblockisles.islesextra.constants.IslesBoss;
import com.isles.skyblockisles.islesextra.party.IslesParty;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.entity.mob.MagmaCubeEntity;

public final class ClientEventHandler implements EventHandler {

  @Override
  public void register() {
    // TODO: Move to less expensive alternative
    ClientTickEvents.START_CLIENT_TICK.register(client -> {
      if (client.player == null || client.world == null) {
        return;
      }
      if (!IslesHudHandler.inBoss) {
        return;
      }
      IslesBoss.getBoss().ifPresent(boss -> {
        switch (boss) {
          case FROG -> StomachExplosionWarning.init();
          case TURTLE -> CoconutBombWarning.init();
        }
      });
    });

    ClientEntityEvents.ENTITY_LOAD.register(
        (entity, world) -> IslesBoss.getBoss().ifPresent(boss -> {
          if (entity instanceof MagmaCubeEntity && boss == IslesBoss.CRIMSON_DRAGON) {
            MagmaBombWarning.init();
          }
        }));

    ClientReceiveMessageEvents.GAME.register((message, overlay) -> {
      IslesParty.handleMember(message.getString());
    });
  }
}
