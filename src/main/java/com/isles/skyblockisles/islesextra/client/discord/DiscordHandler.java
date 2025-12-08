package com.isles.skyblockisles.islesextra.client.discord;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.IPCListener;
import com.jagrosh.discordipc.entities.RichPresence;
import com.jagrosh.discordipc.exceptions.NoDiscordClientException;
import com.isles.skyblockisles.islesextra.utils.ClientUtils;
import com.isles.skyblockisles.islesextra.event.JoinedIslesCallback;
import com.isles.skyblockisles.islesextra.event.LeftIslesCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;

public class DiscordHandler {

    // Discord Rich Presence handler

    public IPCClient discordClient;
    protected boolean ready = false;

    public DiscordHandler() {
        JoinedIslesCallback.EVENT.register(() -> {
            if (discordClient == null) {
                discordClient = new IPCClient(1128526559016394874L); // is this key public? I fricking hope so, should be fine tho
                discordClient.setListener(new IPCListenerImpl(this));
            }
            enable();
            setRichPresence(true, "In Wharfmolo", "Killing innocent citizens");
            return ActionResult.PASS;
        });

        LeftIslesCallback.EVENT.register(() -> {
            if (active) disable();
            return ActionResult.PASS;
        });

        // TODO; test this
        /*
        Format: just a single string representing two lines, seperated by ";"
         */
        ClientPlayNetworking.registerGlobalReceiver(DiscordRPPayload.ID, (payload, context) -> {
            String s = payload.text();
            String[] lines = s.split(";");
            if (lines.length == 2) {
                setRichPresence(false, lines[0], lines[1]);
            }
        });

    }


    private static final RichPresence.Builder richPresenceBuilder = new RichPresence.Builder().setStartTimestamp(System.currentTimeMillis());
    public void setRichPresence(boolean reset, String... lines) {
        if (!ready) {
            if (reset) ClientUtils.sendMessage("§4Discord not found.");
            return;
        }
        if (MinecraftClient.getInstance().player == null) return;
        final ClientPlayerEntity player = MinecraftClient.getInstance().player;

        /* Buttons */
        JsonObject playerButton = new JsonObject();
        playerButton.addProperty("label", player.getName().getString());
        playerButton.addProperty("url", "https://www.youtube.com/watch?v=lpiB2wMc49g");
        JsonArray buttons = new JsonArray(2);
        buttons.add(BUTTON);
        buttons.add(playerButton);

        richPresenceBuilder
                .setDetails(lines.length > 0 ? lines[0] : "")
                .setState(lines.length > 1 ? lines[1] : "")
                .setLargeImage(
                        "https://cdn.discordapp.com/app-icons/1015667892601241640/5beeb6c9d0196f7d8a45ea8b6123b13b.png",
                        "play.skyblockisles.com"
                )
                .setSmallImage(
                        "https://crafatar.com/renders/head/" + player.getUuidAsString() + "?overlay&default=MHF_Steve.png",
                                    player.getName().getString()
                )
                .setButtons(buttons);
                //.setButtons(new RichPresenceButton[]{BUTTON, new RichPresenceButton("https://www.youtube.com/watch?v=lpiB2wMc49g", player.getName().getString() + " - Stats")});
        if (reset) richPresenceBuilder.setStartTimestamp(System.currentTimeMillis());
        discordClient.sendRichPresence(richPresenceBuilder.build());
        System.out.println("RICH PRESENCE SET");
    }

    private static boolean active;
    public void disable() {
        discordClient.close();
        ready = active = false;
    }
    public void enable() {
        try {
            discordClient.connect();
            active = true;
        } catch (NoDiscordClientException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static final JsonObject BUTTON = new JsonObject();
    static {
        BUTTON.addProperty("label", "Skyblock Isles");
        BUTTON.addProperty("url", "https://skyblockisles.com");
    }

}
