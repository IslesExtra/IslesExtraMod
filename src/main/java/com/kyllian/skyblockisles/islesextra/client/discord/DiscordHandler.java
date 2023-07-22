package com.kyllian.skyblockisles.islesextra.client.discord;

import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.IPCListener;
import com.jagrosh.discordipc.entities.RichPresence;
import com.jagrosh.discordipc.entities.RichPresenceButton;
import com.jagrosh.discordipc.exceptions.NoDiscordClientException;
import com.kyllian.skyblockisles.islesextra.client.ClientUtils;
import com.kyllian.skyblockisles.islesextra.event.JoinedIslesCallback;
import com.kyllian.skyblockisles.islesextra.event.LeftIslesCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.ActionResult;

public class DiscordHandler {

    // Discord Rich Presence handler

    public IPCClient discordClient;
    private boolean ready = false;

    public DiscordHandler() {
        JoinedIslesCallback.EVENT.register(() -> {
            if (discordClient == null) {
                discordClient = new IPCClient(1015667892601241640L);
                discordClient.setListener(new IPCListener(){
                    @Override
                    public void onReady(IPCClient client) { ready = true; }
                });
            }
            enable();
            setRichPresence(true, "In Wharfmolo", "Killing innocent citizens");
            return ActionResult.SUCCESS;
        });

        LeftIslesCallback.EVENT.register(() -> {
            if (active) disable();
            return ActionResult.SUCCESS;
        });
    }


    private static final RichPresence.Builder richPresenceBuilder = new RichPresence.Builder().setStartTimestamp(System.currentTimeMillis());
    public void setRichPresence(boolean reset, String... lines) {
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
    public void disable() {
        discordClient.close();
        ready = active = false;
    }
    public void enable() {
        try {
            discordClient.connect();
            active = true;
        } catch (NoDiscordClientException e) {
            e.printStackTrace();
        }
    }

    private static final RichPresenceButton BUTTON = new RichPresenceButton("https://skyblockisles.com", "Skyblock Isles");

}
