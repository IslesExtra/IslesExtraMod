package com.isles.skyblockisles.islesextra.party;

import com.isles.skyblockisles.islesextra.constants.IslesRank;
import com.isles.skyblockisles.islesextra.constants.MessageSender;
import com.mojang.authlib.GameProfile;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs.Game;
import net.minecraft.client.network.PlayerListEntry;
import java.util.List;
import java.util.Objects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IslesParty {

  public static IslesParty INSTANCE = new IslesParty();
  private static final Logger LOGGER = LogManager.getLogger();

  private static final String JOIN_MESSAGE = " has joined the party!";
  private static final String LEAVE_MESSAGE = " has left the party.";

  private Set<GameProfile> members = Set.of();

  private IslesParty() {
  }

  public void addMember(GameProfile profile) {
    this.members.add(profile);
  }

  public void removeMember(GameProfile profile) {
    members.remove(profile);
  }

  public Set<GameProfile> getMembers() {
    return members;
  }

  public Set<ServerPlayerEntity> getMemberEntities() {
    MinecraftServer server = MinecraftClient.getInstance().getServer();
    if (server == null) {
      return Set.of();
    }

    var memberUuids = getMembers().stream().map(GameProfile::id).collect(Collectors.toSet());

    return server.getPlayerManager().getPlayerList().stream()
        .filter(entity -> memberUuids.contains(entity.getUuid())).collect(Collectors.toSet());
  }

  public void clearMembers() {
    members.clear();
  }

  public void hightlightMembers() {
    if (getMembers().isEmpty() || MinecraftClient.getInstance().getNetworkHandler() == null) {
      return;
    }
    getMemberEntities().forEach(player -> {
      //TODO: Apply Glow Effect
    });
  }

  public void lowHealthWarning() {
    if (getMembers().isEmpty() || MinecraftClient.getInstance().getNetworkHandler() == null) {
      return;
    }

    for (PlayerEntity playerEntity : getMemberEntities()) {
      if (playerEntity.getHealth() < 20) {
        MessageSender.sendTitle("§4§l" + playerEntity.getName() + " is low!");
      }
    }
  }

  public void handleMember(String message) {
    var networkHandler = MinecraftClient.getInstance().getNetworkHandler();
    if (networkHandler == null) {
      return;
    }

    if (Arrays.stream(IslesRank.values()).anyMatch(it -> message.contains(it.icon))) {
      return;
    }

    final String username;
    final boolean isJoin;
    if (message.startsWith(JOIN_MESSAGE)) {
      isJoin = true;
      username = message.substring(JOIN_MESSAGE.length());
    } else if (message.startsWith(LEAVE_MESSAGE)) {
      isJoin = false;
      username = message.substring(LEAVE_MESSAGE.length());
    } else {
      return;
    }

    networkHandler.getPlayerList().stream().map(PlayerListEntry::getProfile)
        .filter(profile -> profile != null && profile.name().equalsIgnoreCase(username)).findFirst()
        .ifPresent(profile -> {
          LOGGER.info("Found Player: {}", profile.name());
          if (isJoin) {
            addMember(profile);
          } else {
            removeMember(profile);
          }
        });
  }
}
