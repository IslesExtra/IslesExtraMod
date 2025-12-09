package com.isles.skyblockisles.islesextra.constants;

import java.util.Arrays;
import java.util.Optional;
import net.minecraft.client.MinecraftClient;

public enum IslesBoss {
  REAPER("Haunted Reaper"),
  QUEEN_BEE("Queen Bee"),
  NANOOK("Ice Bear Nanook"),
  FROG("Frog"),
  TURTLE("Torturious"),
  CRIMSON_DRAGON("Crimson Dragon Fafnir");

  public final String fullName;

  IslesBoss(String fullName) {
    this.fullName = fullName;
  }

  public static Optional<IslesBoss> getBoss() {
    var world = MinecraftClient.getInstance().world;
    if (world == null) return Optional.empty();
    return Arrays.stream(IslesBoss.values())
        .filter(boss -> world.getRegistryKey().toString().toLowerCase().contains(boss.toString().toLowerCase()))
        .findFirst();
  }
}