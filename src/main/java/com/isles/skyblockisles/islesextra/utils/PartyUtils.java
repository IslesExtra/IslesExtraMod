package com.isles.skyblockisles.islesextra.utils;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.PlayerListEntry;
import org.spongepowered.asm.mixin.Mutable;

import java.util.*;

public class PartyUtils {


    @Mutable
    public static List<GameProfile> partyMembers = new ArrayList<>();
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
                player = playerListEntry.getProfile();
            }
        }

        if (player == null) return;

        if(message.contains(joinMessage)) {
            addMember(player);
        } else
            removeMember(player);
    }


}
