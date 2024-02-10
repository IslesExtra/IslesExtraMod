package com.isles.skyblockisles.islesextra.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface LeftIslesCallback {

    Event<LeftIslesCallback> EVENT = EventFactory.createArrayBacked(LeftIslesCallback.class,
            (listeners) -> () -> {
                for (LeftIslesCallback listener : listeners) {
                    ActionResult result = listener.interact();
                    if(result != ActionResult.PASS && result != ActionResult.SUCCESS) return result;
                }
                return ActionResult.PASS;
            });

    ActionResult interact();

}
