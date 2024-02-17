package com.isles.skyblockisles.islesextra.general.party;

import com.isles.skyblockisles.islesextra.utils.ClientUtils;
import com.isles.skyblockisles.islesextra.utils.IslesConstants;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mutable;

import java.util.*;
import java.util.stream.Collectors;

public class IslesParty {


    private static final List<GameProfile> partyMembers = new ArrayList<>();
    public static List<GameProfile> getMembers() { return partyMembers; }
    public static void addMember(GameProfile profile) { partyMembers.add(profile); }
    public static void removeMember(GameProfile profile) { partyMembers.remove(profile); }
    public static void clearMembers() { partyMembers.clear(); }
    public static List<PlayerEntity> getEntities() {
        return partyMembers.stream()
                .map(profile -> ClientUtils.getWorld().getPlayerByUuid(profile.getId()))
                .collect(Collectors.toList());
    }

    public static boolean isInParty(PlayerEntity playerEntity) {
        return getMembers().stream().anyMatch(profile -> profile.getId().equals(playerEntity.getGameProfile().getId()));
    }

    public static boolean isInParty(GameProfile gameProfile) {
        return getMembers().stream().anyMatch(profile -> profile.getId().equals(gameProfile.getId()));
    }

    private static void leaveParty() {
        partyMembers.clear();
    }

    public static void handlePartyCommand(String[] args) {
        switch (args[0].toUpperCase()) {
            case "DISBAND": // fall through
            case "LEAVE": { leaveParty(); break; }
        }
    }

    public static void handleMember(String message) {
        if (ClientUtils.getClient().getNetworkHandler() == null) return;
        for (IslesConstants.Rank rank : IslesConstants.Rank.values()) {
            if (message.contains(rank.icon)) return;
        }

        String joinMessage = " has joined the party!";
        String leaveMessage = " has left the party.";
        String disbandMessage = "The party you were in has been disbanded.";

        if (message.equals(disbandMessage)) { clearMembers(); return; }
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
