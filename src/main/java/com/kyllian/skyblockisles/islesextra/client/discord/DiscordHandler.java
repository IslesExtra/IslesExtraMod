package com.kyllian.skyblockisles.islesextra.client.discord;

import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.IPCListener;
import com.jagrosh.discordipc.entities.RichPresence;
import com.jagrosh.discordipc.entities.RichPresenceButton;
import com.jagrosh.discordipc.exceptions.NoDiscordClientException;
import com.kyllian.skyblockisles.islesextra.annotation.OnIslesJoin;
import com.kyllian.skyblockisles.islesextra.annotation.OnIslesLeave;
import com.kyllian.skyblockisles.islesextra.client.ClientUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

public class DiscordHandler {

    // Discord Rich Presence handler

    public static IPCClient discordClient;
    private static boolean ready = false;

    @OnIslesJoin
    public static void start() {
        if (discordClient == null) {
            discordClient = new IPCClient(1015667892601241640L);
            discordClient.setListener(new IPCListener(){
                @Override
                public void onReady(IPCClient client) { ready = true; }
            });
        }
        enable();
        setRichPresence(true, "In Wharfmolo", "Killing innocent citizens");
    }

    @OnIslesLeave
    public static void stop() {
        if (active) disable();
    }

    private static final RichPresence.Builder richPresenceBuilder = new RichPresence.Builder().setStartTimestamp(System.currentTimeMillis());
    public static void setRichPresence(boolean reset, String... lines) {
        if (!ready) {
            if (reset) ClientUtils.sendMessage("§4Discord not found.");
            return;
        }
        assert MinecraftClient.getInstance().player != null;
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        richPresenceBuilder
                .setDetails(lines.length > 0 ? lines[0] : "")
                .setState(lines.length > 1 ? lines[1] : "")
                .setLargeImage(
                        "https://cdn.discordapp.com/app-icons/1015667892601241640/5beeb6c9d0196f7d8a45ea8b6123b13b.png",
                        "play.skyblockisles.com"
                )
                .setSmallImage(
                        "https://crafatar.com/renders/head/" + player.getUuidAsString() + "?overlay&default=MHF_Steve",
                                    player.getName().getString()
                )
                .setButtons(new RichPresenceButton[]{BUTTON, new RichPresenceButton("https://www.youtube.com/watch?v=lpiB2wMc49g", player.getName().getString() + " - Stats")});
        if (reset) richPresenceBuilder.setStartTimestamp(System.currentTimeMillis());
        discordClient.sendRichPresence(richPresenceBuilder.build());
    }

    private static boolean active;
    public static void disable() {
        discordClient.close();
        ready = active = false;
    }
    public static void enable() {
        try {
            discordClient.connect();
            active = true;
        } catch (NoDiscordClientException e) {
            e.printStackTrace();
        }
    }

    private static final RichPresenceButton BUTTON = new RichPresenceButton("https://skyblockisles.com", "Skyblock Isles");

}
