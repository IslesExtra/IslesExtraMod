package com.isles.skyblockisles.islesextra.client;

import com.isles.skyblockisles.islesextra.event.JoinedIslesCallback;
import com.isles.skyblockisles.islesextra.event.LeftIslesCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerInfo;

public class IslesEventHandler {

    public static void init() {

        ClientPlayConnectionEvents.JOIN.register(((handler, sender, client) -> {
            String ip = handler.getConnection().getAddress().toString();
            if (ip.contains("isles")) {
                if (!IslesExtraClient.isOnIsles()) JoinedIslesCallback.EVENT.invoker().interact();
                return;
            }
            if (!IslesExtraClient.isOnIsles()) return;
            LeftIslesCallback.EVENT.invoker().interact(); // just in case the client had already left isles but the event had somehow not yet been fired
        }));

        ClientPlayConnectionEvents.DISCONNECT.register(((handler, client) -> {
            if (!IslesExtraClient.isOnIsles()) return;
            ClientUtils.scheduleNextTick(() -> {
                ServerInfo serverInfo = MinecraftClient.getInstance().getCurrentServerEntry();
                if (serverInfo == null || !serverInfo.address.contains("isles")) LeftIslesCallback.EVENT.invoker().interact();
            });
        }));

    }

}
