package com.isles.skyblockisles.islesextra.client.screen.party;

import com.isles.skyblockisles.islesextra.general.party.IslesParty;
import com.mojang.authlib.GameProfile;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

@Environment(value=EnvType.CLIENT)
public class PartyScreen
extends Screen {

    /*
    Possible textures usable for party related stuff:
    - minecraft/textures/gui/sprites/pending_invite
    - minecraft/textures/gui/sprites/notification
    - minecraft\textures\gui\sprites\icon\draft_report (envelope!)
     */

    private static final Identifier BACKGROUND_TEXTURE = new Identifier("social_interactions/background");
    private static final Identifier SEARCH_ICON_TEXTURE = new Identifier("icon/search");
    private static final Text ALL_TAB_TITLE = Text.translatable("gui.socialInteractions.tab_all");
    private static final Text PARTY_TAB_TITLE = Text.literal("Party");
    private static final Text SELECTED_ALL_TAB_TITLE = ALL_TAB_TITLE.copyContentOnly().formatted(Formatting.UNDERLINE);
    private static final Text SELECTED_PARTY_TAB_TITLE = PARTY_TAB_TITLE.copyContentOnly().formatted(Formatting.UNDERLINE);
    private static final Text SEARCH_TEXT = Text.translatable("gui.socialInteractions.search_hint").formatted(Formatting.ITALIC).formatted(Formatting.GRAY);
    static final Text EMPTY_SEARCH_TEXT = Text.translatable("gui.socialInteractions.search_empty").formatted(Formatting.GRAY);
    private static final Text EMPY_PARTY_TEXT = Text.literal("Currently not in a party.").formatted(Formatting.GRAY);
    PartyPlayerListWidget playerList;
    TextFieldWidget searchBox;
    private String currentSearch = "";
    protected Tab currentTab = Tab.ALL;
    private ButtonWidget allTabButton;
    private ButtonWidget hiddenTabButton;
    @Nullable
    private Text serverLabel;
    private int playerCount;
    private boolean initialized;

    public PartyScreen() {
        super(Text.translatable("party_management_screen.islesextra.name"));
        this.updateServerLabel(MinecraftClient.getInstance());
    }

    private int getScreenHeight() {
        return Math.max(52, this.height - 128 - 16);
    }

    private int getPlayerListBottom() {
        return 80 + this.getScreenHeight() - 8;
    }

    private int getSearchBoxX() {
        return (this.width - 238) / 2;
    }

    @Override
    public Text getNarratedTitle() {
        if (this.serverLabel != null) {
            return ScreenTexts.joinSentences(super.getNarratedTitle(), this.serverLabel);
        }
        return super.getNarratedTitle();
    }

    @Override
    protected void init() {
        if (this.initialized) {
            this.playerList.setDimensionsAndPosition(this.width, this.getPlayerListBottom() - 88, 0, 88);
        } else {
            this.playerList = new PartyPlayerListWidget(this.client, this.width, this.getPlayerListBottom() - 88, 88, 36, this);
        }
        int i = this.playerList.getRowWidth() / 3;
        int j = this.playerList.getRowLeft();
        int k = this.playerList.getRowRight();
        this.allTabButton = this.addDrawableChild(ButtonWidget.builder(ALL_TAB_TITLE, button -> this.setCurrentTab(Tab.ALL)).dimensions(j, 45, i, 20).build());
        this.hiddenTabButton = this.addDrawableChild(ButtonWidget.builder(PARTY_TAB_TITLE, button -> this.setCurrentTab(Tab.PARTY)).dimensions((j + k - i) / 2 + 1, 45, i, 20).build());
        String string = this.searchBox != null ? this.searchBox.getText() : "";
        this.searchBox = new TextFieldWidget(this.textRenderer, this.getSearchBoxX() + 28, 74, 200, 15, SEARCH_TEXT){

            @Override
            protected MutableText getNarrationMessage() {
                if (!PartyScreen.this.searchBox.getText().isEmpty() && PartyScreen.this.playerList.isEmpty()) {
                    return super.getNarrationMessage().append(", ").append(EMPTY_SEARCH_TEXT);
                }
                return super.getNarrationMessage();
            }
        };
        this.searchBox.setMaxLength(16);
        this.searchBox.setVisible(true);
        this.searchBox.setEditableColor(0xFFFFFF);
        this.searchBox.setText(string);
        this.searchBox.setPlaceholder(SEARCH_TEXT);
        this.searchBox.setChangedListener(this::onSearchChange);
        this.addSelectableChild(this.searchBox);
        this.addSelectableChild(this.playerList);
        this.initialized = true;
        this.setCurrentTab(this.currentTab);
    }

    private void setCurrentTab(Tab currentTab) {
        if (this.client == null) return;
        if (this.client.player == null) return;
        this.currentTab = currentTab;
        this.allTabButton.setMessage(ALL_TAB_TITLE);
        this.hiddenTabButton.setMessage(PARTY_TAB_TITLE);
        boolean bl = false;
        switch (currentTab) {
            case ALL: {
                this.allTabButton.setMessage(SELECTED_ALL_TAB_TITLE);
                Collection<UUID> collection = this.client.player.networkHandler.getPlayerUuids();
                collection.removeIf(uuid -> uuid.getMostSignificantBits() == 0);
                this.playerList.update(collection, this.playerList.getScrollAmount());
                break;
            }
            case PARTY: {
                this.hiddenTabButton.setMessage(SELECTED_PARTY_TAB_TITLE);
                Set<UUID> set = IslesParty.getMembers().stream().map(GameProfile::getId).collect(Collectors.toSet());
                bl = set.isEmpty();
                this.playerList.update(set, this.playerList.getScrollAmount());
                break;
            }
        }
        NarratorManager narratorManager = this.client.getNarratorManager();
        if (!this.searchBox.getText().isEmpty() && this.playerList.isEmpty() && !this.searchBox.isFocused()) {
            narratorManager.narrate(EMPTY_SEARCH_TEXT);
        } else if (bl) {
            narratorManager.narrate(EMPY_PARTY_TEXT);
        }
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        int i = this.getSearchBoxX() + 3;
        super.renderBackground(context, mouseX, mouseY, delta);
        context.drawGuiTexture(BACKGROUND_TEXTURE, i, 64, 236, this.getScreenHeight() + 16);
        context.drawGuiTexture(SEARCH_ICON_TEXTURE, i + 10, 76, 12, 12);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (this.client == null) return;
        super.render(context, mouseX, mouseY, delta);
        this.updateServerLabel(this.client);
        if (this.serverLabel != null) {
            context.drawTextWithShadow(this.client.textRenderer, this.serverLabel, this.getSearchBoxX() + 8, 35, Colors.WHITE);
        }
        if (!this.playerList.isEmpty()) {
            this.playerList.render(context, mouseX, mouseY, delta);
        } else if (!this.searchBox.getText().isEmpty()) {
            context.drawCenteredTextWithShadow(this.client.textRenderer, EMPTY_SEARCH_TEXT, this.width / 2, (72 + this.getPlayerListBottom()) / 2, Colors.WHITE);
        } else if (this.currentTab == Tab.PARTY) {
            context.drawCenteredTextWithShadow(this.client.textRenderer, EMPY_PARTY_TEXT, this.width / 2, (72 + this.getPlayerListBottom()) / 2, Colors.WHITE);
        }
        this.searchBox.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.client == null) return false;
        if (!this.searchBox.isFocused() && this.client.options.socialInteractionsKey.matchesKey(keyCode, scanCode)) {
            this.client.setScreen(null);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    private void onSearchChange(String currentSearch) {
        if (!(currentSearch = currentSearch.toLowerCase(Locale.ROOT)).equals(this.currentSearch)) {
            this.playerList.setCurrentSearch(currentSearch);
            this.currentSearch = currentSearch;
            this.setCurrentTab(this.currentTab);
        }
    }

    public int getFilteredPlayerCount() {
        if (this.client == null) return 0;
        Collection<PlayerListEntry> list = Objects.requireNonNull(client.getNetworkHandler()).getPlayerList();
        list.removeIf(player -> player.getProfile().getName().contains("slot_") || player.getProfile().getName().isEmpty());
        list.removeIf(player -> (int) player.getProfile().getName().charAt(0) == 167);
        return list.size();
    }

    private void updateServerLabel(MinecraftClient client) {
        if (this.client == null) return;
        int i = getFilteredPlayerCount();
        if (this.playerCount != i) {
            String string = "";
            ServerInfo serverInfo = client.getCurrentServerEntry();
            if (serverInfo != null) string = serverInfo.name;
            this.serverLabel = i > 1 ? Text.translatable("gui.socialInteractions.server_label.multiple", string, i) : Text.translatable("gui.socialInteractions.server_label.single", string, i);
            this.playerCount = i;
        }
    }

    @Environment(value=EnvType.CLIENT)
    public enum Tab {
        ALL,
        PARTY;

    }
}

