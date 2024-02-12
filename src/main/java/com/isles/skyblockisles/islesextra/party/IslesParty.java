package com.isles.skyblockisles.islesextra.party;

import com.isles.skyblockisles.islesextra.constants.IslesRank;
import com.mojang.authlib.GameProfile;
import java.util.ArrayList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import java.util.List;
import java.util.Objects;
import org.spongepowered.asm.mixin.Mutable;

public class IslesParty {

  @Mutable
  public static List<GameProfile> partyMembers = new ArrayList<>();

  public static void addMember(GameProfile profile) {
    partyMembers.add(profile);
  }

  public static void removeMember(GameProfile profile) {
    partyMembers.remove(profile);
  }

  public static void handleMember(String message) {
    var networkHandler = MinecraftClient.getInstance().getNetworkHandler();
    if (networkHandler == null) {
      return;
    }

    for (IslesRank rank : IslesRank.values()) {
      if (message.contains(rank.icon)) {
        return;
      }
    }

    String joinMessage = " has joined the party!";
    String leaveMessage = " has left the party.";

    if (!message.contains(joinMessage) && !message.contains(leaveMessage)) {
      return;
    }

    String username = message.replace(joinMessage, "").replace(leaveMessage, "");
    // A weird String to not confuse with real messages
    // TODO: Better Implementation
    if (!(Objects.equals(List.of((username + " {§klsdjfg}").split(" ")).get(1), "{§klsdjfg}"))) {
      return;
    }

    GameProfile player = null;
    for (PlayerListEntry playerListEntry : networkHandler
        .getPlayerList()) {
      if (playerListEntry.getProfile() != null) {
        if (playerListEntry.getProfile().name().equalsIgnoreCase(username)) {
          System.out.println("Found Player: " + playerListEntry.getProfile().name());
          player = playerListEntry.getProfile();
        }
      }
    }

    if (player == null) {
      return;
    }

    if (message.contains(joinMessage)) {
      //addMember(player);
      System.out.println("Add User from Party list: " + player.name());
    } else //removeMember(player);
    {
      System.out.println("Remove User from Party list: " + player.name());
    }
  }


}
