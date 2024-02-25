package com.isles.skyblockisles.islesextra.client;

import com.isles.skyblockisles.islesextra.event.JoinedIslesCallback;
import com.isles.skyblockisles.islesextra.event.LeftIslesCallback;
import com.isles.skyblockisles.islesextra.event.SwitchedIslesServerCallback;
import com.isles.skyblockisles.islesextra.general.party.IslesParty;
import com.isles.skyblockisles.islesextra.utils.ClientUtils;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;

public class IslesEventHandler {

    public static void register() {

        ClientPlayConnectionEvents.JOIN.register(((handler, sender, client) -> {
            String ip = handler.getConnection().getAddress().toString();
            if (ip.contains("isles")) {
                if (!IslesExtraClient.isOnIsles()) JoinedIslesCallback.EVENT.invoker().interact();
                else SwitchedIslesServerCallback.EVENT.invoker().interact(); // already on isles, so server switch
                return;
            }
            if (!IslesExtraClient.isOnIsles()) return;
            LeftIslesCallback.EVENT.invoker().interact(); // just in case the client had already left isles but the event had somehow not yet been fired
        }));

        ClientPlayConnectionEvents.DISCONNECT.register(((handler, client) -> {
            if (!IslesExtraClient.isOnIsles()) return;
            ClientUtils.scheduleNextTick(() -> {
                ServerInfo serverInfo = MinecraftClient.getInstance().getCurrentServerEntry();
                System.out.println(serverInfo == null);
                if (serverInfo == null || !serverInfo.address.contains("isles")) LeftIslesCallback.EVENT.invoker().interact(); // no longer on isles so client left server
            });

            IslesParty.clearMembers();

        }));

        ClientSendMessageEvents.COMMAND.register(input -> {
            String command = input.split(" ")[0].toUpperCase();
            String[] args = input.substring(input.indexOf(" ")+1).split(" ");
            switch (command) {
                case "PARTY": IslesParty.handlePartyCommand(args);
                // more
            }
        });

    }

}
