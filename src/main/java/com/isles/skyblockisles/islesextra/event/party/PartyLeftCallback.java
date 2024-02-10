package com.isles.skyblockisles.islesextra.event.party;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface PartyLeftCallback {

    Event<PartyLeftCallback> EVENT = EventFactory.createArrayBacked(PartyLeftCallback.class,
            (listeners) -> (playerName, kicked) -> {
                for (PartyLeftCallback listener : listeners) {
                    ActionResult result = listener.interact(playerName, kicked);
                    if (result != ActionResult.PASS && result != ActionResult.SUCCESS) return result;
                }
                return ActionResult.PASS;
            });

    ActionResult interact(String playerName, boolean forced);

}
