package com.isles.skyblockisles.islesextra.discord;

import com.google.gson.JsonObject;
import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.IPCListener;
import com.jagrosh.discordipc.entities.Packet;
import com.jagrosh.discordipc.entities.User;

public class IPCListenerImpl implements IPCListener {

  @Override
  public void onReady(IPCClient client) {
    DiscordHandler.setReady();
  }

  @Override
  public void onPacketSent(IPCClient client, Packet packet) {

  }

  @Override
  public void onPacketReceived(IPCClient client, Packet packet) {

  }

  @Override
  public void onActivityJoin(IPCClient client, String secret) {

  }

  @Override
  public void onActivitySpectate(IPCClient client, String secret) {

  }

  @Override
  public void onActivityJoinRequest(IPCClient client, String secret, User user) {

  }

  @Override
  public void onClose(IPCClient client, JsonObject json) {

  }

  @Override
  public void onDisconnect(IPCClient client, Throwable t) {

  }
}
