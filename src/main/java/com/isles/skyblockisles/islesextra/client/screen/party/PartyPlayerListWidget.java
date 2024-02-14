/*
 * Decompiled with CFR 0.2.1 (FabricMC 53fa44c9).
 */
package com.isles.skyblockisles.islesextra.client.screen.party;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.session.report.log.ChatLog;
import net.minecraft.client.session.report.log.ChatLogEntry;
import net.minecraft.client.session.report.log.ReceivedMessage;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Environment(value=EnvType.CLIENT)
public class PartyPlayerListWidget
extends ElementListWidget<PartyPlayerListEntry> {
    private final PartyScreen parent;
    private final List<PartyPlayerListEntry> players = Lists.newArrayList();
    @Nullable
    private String currentSearch;

    public PartyPlayerListWidget(PartyScreen parent, MinecraftClient client, int width, int height, int y, int itemHeight) {
        super(client, width, height, y, itemHeight);
        this.parent = parent;
        this.setRenderBackground(false);
    }

    @Override
    protected void enableScissor(DrawContext context) {
        context.enableScissor(this.getX(), this.getY() + 4, this.getRight(), this.getBottom());
    }

    public void update(Collection<UUID> uuids, double scrollAmount, boolean includeOffline) {
        HashMap<UUID, PartyPlayerListEntry> map = new HashMap<UUID, PartyPlayerListEntry>();
        this.setPlayers(uuids, map);
        this.markOfflineMembers(map, includeOffline);
        this.refresh(map.values(), scrollAmount);
    }

    private void setPlayers(Collection<UUID> playerUuids, Map<UUID, PartyPlayerListEntry> entriesByUuids) {
        ClientPlayNetworkHandler clientPlayNetworkHandler = this.client.player.networkHandler;
        for (UUID uUID : playerUuids) {
            PlayerListEntry playerListEntry = clientPlayNetworkHandler.getPlayerListEntry(uUID);
            if (playerListEntry == null) continue;
            boolean bl = playerListEntry.hasPublicKey();
            entriesByUuids.put(uUID, new PartyPlayerListEntry(this.client, this.parent, uUID, playerListEntry.getProfile().getName(), playerListEntry::getSkinTextures, bl));
        }
    }

    private void markOfflineMembers(Map<UUID, PartyPlayerListEntry> entries, boolean includeOffline) {
        Collection<GameProfile> collection = PartyPlayerListWidget.collectReportableProfiles(this.client.getAbuseReportContext().getChatLog());
        for (GameProfile gameProfile : collection) {
            PartyPlayerListEntry socialInteractionsPlayerListEntry;
            if (includeOffline) {
                socialInteractionsPlayerListEntry = entries.computeIfAbsent(gameProfile.getId(), uuid -> {
                    PartyPlayerListEntry entry = new PartyPlayerListEntry(this.client, this.parent, gameProfile.getId(), gameProfile.getName(), this.client.getSkinProvider().getSkinTexturesSupplier(gameProfile), true);
                    entry.setOffline(true);
                    return entry;
                });
            } else {
                socialInteractionsPlayerListEntry = entries.get(gameProfile.getId());
                if (socialInteractionsPlayerListEntry == null) continue;
            }
            socialInteractionsPlayerListEntry.setSentMessage(true);
        }
    }

    private static Collection<GameProfile> collectReportableProfiles(ChatLog log) {
        ObjectLinkedOpenHashSet<GameProfile> set = new ObjectLinkedOpenHashSet<GameProfile>();
        for (int i = log.getMaxIndex(); i >= log.getMinIndex(); --i) {
            ReceivedMessage.ChatMessage chatMessage;
            ChatLogEntry chatLogEntry = log.get(i);
            if (!(chatLogEntry instanceof ReceivedMessage.ChatMessage) || !(chatMessage = (ReceivedMessage.ChatMessage)chatLogEntry).message().hasSignature()) continue;
            set.add(chatMessage.profile());
        }
        return set;
    }

    private void refresh(Collection<PartyPlayerListEntry> players, double scrollAmount) {
        this.players.clear();
        this.players.addAll(players);
        // TODO; reimpl
        //this.sortPlayers();
        this.filterPlayers();
        this.replaceEntries(this.players);
        this.setScrollAmount(scrollAmount);
    }

    private void filterPlayers() {
        if (this.currentSearch != null) {
            this.players.removeIf(player -> !player.getName().toLowerCase(Locale.ROOT).contains(this.currentSearch));
            // remove isles npcs
            this.players.removeIf(player -> player.getName().contains("slot_") || player.getName().isEmpty());
            this.replaceEntries(this.players);
        }
    }

    public void setCurrentSearch(String currentSearch) {
        this.currentSearch = currentSearch;
    }

    public boolean isEmpty() {
        return this.players.isEmpty();
    }

    public void setPlayerOnline(PlayerListEntry player, PartyScreen.Tab tab) {
        UUID uUID = player.getProfile().getId();
        for (PartyPlayerListEntry socialInteractionsPlayerListEntry : this.players) {
            if (!socialInteractionsPlayerListEntry.getUuid().equals(uUID)) continue;
            socialInteractionsPlayerListEntry.setOffline(false);
            return;
        }
        if ((tab == PartyScreen.Tab.ALL || this.client.getSocialInteractionsManager().isPlayerMuted(uUID)) && (Strings.isNullOrEmpty(this.currentSearch) || player.getProfile().getName().toLowerCase(Locale.ROOT).contains(this.currentSearch))) {
            PartyPlayerListEntry socialInteractionsPlayerListEntry;
            boolean bl = player.hasPublicKey();
            socialInteractionsPlayerListEntry = new PartyPlayerListEntry(this.client, this.parent, player.getProfile().getId(), player.getProfile().getName(), player::getSkinTextures, bl);
            this.addEntry(socialInteractionsPlayerListEntry);
            this.players.add(socialInteractionsPlayerListEntry);
        }
    }

    public void setPlayerOffline(UUID uuid) {
        for (PartyPlayerListEntry socialInteractionsPlayerListEntry : this.players) {
            if (!socialInteractionsPlayerListEntry.getUuid().equals(uuid)) continue;
            socialInteractionsPlayerListEntry.setOffline(true);
            return;
        }
    }
}

