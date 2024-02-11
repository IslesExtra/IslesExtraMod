package com.isles.skyblockisles.islesextra.event;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface SwitchedIslesServerCallback {

    Event<SwitchedIslesServerCallback> EVENT = EventFactory.createArrayBacked(SwitchedIslesServerCallback.class,
            (listeners) -> () -> {
                for (SwitchedIslesServerCallback listener : listeners) {
                    ActionResult result = listener.interact();
                    if (result != ActionResult.PASS && result != ActionResult.SUCCESS) return result;
                }
                return ActionResult.PASS;
            });

    ActionResult interact();

}
