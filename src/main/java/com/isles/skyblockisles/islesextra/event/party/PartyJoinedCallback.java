package com.isles.skyblockisles.islesextra.event.party;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface PartyJoinedCallback {

  Event<PartyJoinedCallback> EVENT = EventFactory.createArrayBacked(PartyJoinedCallback.class,
      (listeners) -> (playerName) -> {
        for (PartyJoinedCallback listener : listeners) {
          ActionResult result = listener.interact(playerName);
          if (result != ActionResult.PASS && result != ActionResult.SUCCESS) {
            return result;
          }
        }
        return ActionResult.PASS;
      });

  ActionResult interact(String playerName);

}
