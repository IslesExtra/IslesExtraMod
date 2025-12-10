package com.isles.skyblockisles.islesextra.client.screen.party;

import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Environment(value=EnvType.CLIENT)
public class PartyPlayerListWidget
extends ElementListWidget<PartyPlayerListEntry> {
    private final PartyScreen parent;
    private final List<PartyPlayerListEntry> players = Lists.newArrayList();
    @Nullable
    private String currentSearch;

    public PartyPlayerListWidget(MinecraftClient client, int width, int height, int y, int itemHeight, PartyScreen parent) {
        super(client, width, height, y, itemHeight);
        this.parent = parent;
        this.setRenderBackground(false);
    }

    @Override
    protected void enableScissor(DrawContext context) {
        context.enableScissor(this.getX(), this.getY() + 4, this.getRight(), this.getBottom());
    }

    public void update(Collection<UUID> uuids, double scrollAmount) {
        HashMap<UUID, PartyPlayerListEntry> map = new HashMap<>();
        this.setPlayers(uuids, map);
        this.refresh(map.values(), scrollAmount);
    }

    private void setPlayers(Collection<UUID> playerUuids, Map<UUID, PartyPlayerListEntry> entriesByUuids) {
        if (this.client == null || this.client.player == null) return;
        ClientPlayNetworkHandler clientPlayNetworkHandler = this.client.player.networkHandler;
        for (UUID uUID : playerUuids) {
            PlayerListEntry playerListEntry = clientPlayNetworkHandler.getPlayerListEntry(uUID);
            if (playerListEntry == null) continue;
            if (playerListEntry.getProfile().getName().isEmpty()) continue;
            if ((int) playerListEntry.getProfile().getName().charAt(0) == 167) continue; // ignore npc
            entriesByUuids.put(uUID, new PartyPlayerListEntry(this.client, playerListEntry, playerListEntry::getSkinTextures, this.parent));
        }
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

    public void setCurrentSearch(@Nullable String currentSearch) {
        this.currentSearch = currentSearch;
    }

    public boolean isEmpty() {
        return this.players.isEmpty();
    }

}

