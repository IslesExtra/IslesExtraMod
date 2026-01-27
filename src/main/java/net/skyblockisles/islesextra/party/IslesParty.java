package net.skyblockisles.islesextra.party;

import com.mojang.authlib.GameProfile;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.StringHelper;
import net.skyblockisles.islesextra.annotations.Init;
import net.skyblockisles.islesextra.constants.MessageScheduler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IslesParty {

  private static final Logger LOGGER = LogManager.getLogger();

  private static final Pattern JOIN_PATTERN = Pattern.compile("^\\s*(\\w{3,16})" + Pattern.quote(" has joined the party!"));
  private static final Pattern LEAVE_PATTERN = Pattern.compile("^\\s*(\\w{3,16})" + Pattern.quote(" has left the party."));

  private static Set<GameProfile> members = Set.of();

  private IslesParty() { }

  @Init
  public static void init() {
    ClientTickEvents.START_CLIENT_TICK.register(client -> {
      if (client.player == null || client.world == null) return;

      IslesParty.lowHealthWarning();
    });
  }

  public static void addMember(GameProfile profile) {
    members.add(profile);
  }

  public static void removeMember(GameProfile profile) {
    members.remove(profile);
  }

  public static boolean isMember(GameProfile profile) {
    return members.contains(profile);
  }

  public static Set<GameProfile> getMembers() {
    return members;
  }

  public static Set<PlayerEntity> getMemberEntities() {
    ClientWorld world = MinecraftClient.getInstance().world;
    if (world == null) {
      return Set.of();
    }

    Set<UUID> memberUuids = getMembers().stream()
        .map(GameProfile::id)
        .collect(Collectors.toSet());

    return world.getPlayers().stream()
        .filter(entity -> memberUuids.contains(entity.getUuid()))
        .collect(Collectors.toSet());
  }

  public static void clearMembers() {
    members.clear();
  }

  public static void lowHealthWarning() {
    if (getMembers().isEmpty() || MinecraftClient.getInstance().getNetworkHandler() == null) {
      return;
    }

    for (PlayerEntity playerEntity : getMemberEntities()) {
      if (playerEntity.getHealth() < 20) {
        MessageScheduler.scheduleTitle("§4§l" + playerEntity.getName() + " is low!");
      }
    }
  }

  public static void onChat(String message) {
    ClientPlayNetworkHandler networkHandler = MinecraftClient.getInstance().getNetworkHandler();
    if (networkHandler == null) {
      return;
    }

    String cleanMessage = StringHelper.stripTextFormat(message);
    String username = null;
    
    boolean isJoin = false;
    Matcher joinMatcher = JOIN_PATTERN.matcher(cleanMessage);
    Matcher leaveMatcher = LEAVE_PATTERN.matcher(cleanMessage);
    try {
      if (joinMatcher.find()) {
          username = joinMatcher.group(1);
          isJoin = true;
      } else if (leaveMatcher.find()) {
          username = leaveMatcher.group(1);
          isJoin = false;
      }
    } catch (Exception e) {
      //TODO: Find out how to actually resolve these errors, cannot test since no alt
      return;
    }

    if (username == null) return;

    PlayerListEntry entry = networkHandler.getPlayerListEntry(username);

    if (entry != null) {
        GameProfile profile = entry.getProfile();
        LOGGER.info("Found Player: {}", profile.name());
        if (isJoin) addMember(profile); 
        else removeMember(profile);
    }
  }
}
