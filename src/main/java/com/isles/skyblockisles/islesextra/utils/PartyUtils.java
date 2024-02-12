package com.isles.skyblockisles.islesextra.utils;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import java.util.List;
import java.util.Objects;

public class PartyUtils {


    public static List<GameProfile> partyMembers = List.of(MinecraftClient.getInstance().getGameProfile());
    public static void addMember(GameProfile profile) {partyMembers.add(profile);}
    public static void removeMember(GameProfile profile) {partyMembers.remove(profile);}

    public static void handleMember(String message) {
        if (ClientUtils.getClient().getNetworkHandler() == null) return;
        for (IslesConstants.Rank rank : IslesConstants.Rank.values()) {
            if (message.contains(rank.icon)) return;
        }

        String joinMessage = " has joined the party!";
        String leaveMessage = " has left the party.";

        if (!message.contains(joinMessage) && !message.contains(leaveMessage)) return;

        String username = message.replace(joinMessage, "").replace(leaveMessage, "");
        // A weird String to not confuse with real messages
        if (!(Objects.equals(List.of((username + " {§klsdjfg}").split(" ")).get(1), "{§klsdjfg}"))) return;

        GameProfile player = null;
        for (PlayerListEntry playerListEntry : ClientUtils.getClient().getNetworkHandler().getPlayerList()) {
            if(playerListEntry.getProfile() != null)
                if(playerListEntry.getProfile().getName().equalsIgnoreCase(username)) {
                System.out.println("Found Player: " + playerListEntry.getProfile().getName());
                player = playerListEntry.getProfile();
            }
        }

        if (player == null) return;

        if(message.contains(joinMessage)) {
            //addMember(player);
            System.out.println("Add User from Party list: " + player.getName());
        } else //removeMember(player);
            System.out.println("Remove User from Party list: " + player.getName());
    }


}
