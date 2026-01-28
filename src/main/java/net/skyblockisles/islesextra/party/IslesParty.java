package net.skyblockisles.islesextra.party;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.network.PlayerListEntry;
import net.skyblockisles.islesextra.annotations.Init;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;

public class IslesParty {

  private static final Logger LOGGER = LogManager.getLogger();

  private static final Pattern JOIN_PATTERN = Pattern.compile("(\\w{3,16})\\s+has joined the party!");
  private static final Pattern LEAVE_PATTERN = Pattern.compile("(\\w{3,16})\\s+has left the party!");

  private final static Set<UUID> members = new HashSet<>();

  private IslesParty() { }

  @Init
  public static void init() {
    ClientReceiveMessageEvents.GAME.register((message, overlay) -> {
      onChat(message.getString());
    });
  }

  public static void addMember(UUID id) {
    members.add(id);
  }

  public static void removeMember(UUID profile) {
    members.remove(profile);
  }

  public static boolean isMember(UUID profile) {
    return members.contains(profile);
  }

  public static Set<UUID> getMembers() {
    return members;
  }

  public static Set<PlayerEntity> getMemberEntities() {
    ClientWorld world = MinecraftClient.getInstance().world;
    if (world == null) {
      return Set.of();
    }

    return world.getPlayers().stream()
        .filter(entity -> getMembers().contains(entity.getUuid()))
        .collect(Collectors.toSet());
  }

  public static void clearMembers() {
    members.clear();
  }

  public static void onChat(String message) {
    ClientPlayNetworkHandler networkHandler = MinecraftClient.getInstance().getNetworkHandler();
    if (networkHandler == null) {
      return;
    }

    Matcher joinMatcher = JOIN_PATTERN.matcher(message);
    Matcher leaveMatcher = LEAVE_PATTERN.matcher(message);

    if (joinMatcher.find()) {
      String username = joinMatcher.group(1);
      PlayerListEntry entry = networkHandler.getPlayerListEntry(username);
      if (entry != null)
        addMember(entry.getProfile().id());
    }

    if (leaveMatcher.find()) {
      String username = leaveMatcher.group(1);
      PlayerListEntry entry = networkHandler.getPlayerListEntry(username);
      if (entry != null)
        removeMember(entry.getProfile().id());
    }
  }
}
