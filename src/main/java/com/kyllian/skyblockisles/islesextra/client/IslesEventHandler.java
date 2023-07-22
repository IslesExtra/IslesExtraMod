package com.kyllian.skyblockisles.islesextra.client;

import com.kyllian.skyblockisles.islesextra.event.JoinedIslesCallback;
import com.kyllian.skyblockisles.islesextra.event.LeftIslesCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
public class IslesEventHandler {

    public static void init() {

        ClientPlayConnectionEvents.JOIN.register(((handler, sender, client) -> {
            String ip = handler.getConnection().getAddress().toString();
            if (ip.contains("isles")) {
                if (IslesExtraClient.isOnIsles()) LeftIslesCallback.EVENT.invoker().interact();
                else JoinedIslesCallback.EVENT.invoker().interact();
            }
        }));

        ClientPlayConnectionEvents.DISCONNECT.register(((handler, client) -> {
            String ip = handler.getConnection().getAddress().toString();
            if (ip.contains("isles")) LeftIslesCallback.EVENT.invoker().interact();
        }));

    }

}
