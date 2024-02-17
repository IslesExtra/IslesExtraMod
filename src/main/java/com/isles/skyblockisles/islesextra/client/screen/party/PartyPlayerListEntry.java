package com.isles.skyblockisles.islesextra.client.screen.party;

import com.google.common.collect.ImmutableList;
import com.isles.skyblockisles.islesextra.general.party.IslesParty;
import com.isles.skyblockisles.islesextra.utils.ClientUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.PlayerSkinDrawer;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.multiplayer.SocialInteractionsPlayerListEntry;
import net.minecraft.client.gui.screen.report.AbuseReportTypeScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Environment(value=EnvType.CLIENT)
public class PartyPlayerListEntry
extends ElementListWidget.Entry<PartyPlayerListEntry> {
    private final PartyScreen parent;
    private final MinecraftClient client;
    private final List<ClickableWidget> buttons;
    private final String name;
    private final Supplier<SkinTextures> skinSupplier;
    public static final int WHITE_COLOR = ColorHelper.Argb.getArgb(255, 255, 255, 255);
    public static final int PARTY_COLOR = 0x3ca4e6;

    private final ButtonWidget inviteButton;
    private static final ButtonTextures INVITE_BUTTON_TEXTURE = new ButtonTextures(new Identifier("icon/draft_report"), new Identifier("icon/draft_report"));

    public PartyPlayerListEntry(MinecraftClient client, PlayerListEntry entry, Supplier<SkinTextures> skinTexture, PartyScreen parent) {
        this.parent = parent;
        this.client = client;
        this.name = entry.getProfile().getName();
        this.skinSupplier = skinTexture;

        this.buttons = new ArrayList<>();
        this.inviteButton = new TexturedButtonWidget(0, 0, 20, 20, INVITE_BUTTON_TEXTURE, button -> {
            if (ClientUtils.getClient().player == null) return;
            ClientUtils.getClient().player.networkHandler.sendCommand("party invite " + name);
            button.visible = false;
            button.active = false;
        }, Text.translatable("gui.socialInteractions.report"));

        if (client.player == null) return;
        setButtonEnabled(this.inviteButton, // only show invite button if player is not the client and is not already in party
                !IslesParty.isInParty(entry.getProfile()) && !entry.getProfile().equals(client.player.getGameProfile()));
        this.inviteButton.setTooltip(Tooltip.of(Text.literal("Invite")));
        this.inviteButton.setTooltipDelay(10);

        this.buttons.add(inviteButton);
    }

    static void setButtonEnabled(ButtonWidget widget, boolean active) {
        widget.active = active;
        widget.visible = active;
    }

    @Override
    public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        int l = y + (entryHeight - this.client.textRenderer.fontHeight) / 2;
        int i = x + 4;
        int j = y + (entryHeight - 24) / 2;
        int k = i + 24 + 4;
        PlayerSkinDrawer.draw(context, this.skinSupplier.get(), i, j, 24);
        context.drawText(this.client.textRenderer, this.name, k, l, parent.currentTab.equals(PartyScreen.Tab.PARTY) ? PARTY_COLOR : WHITE_COLOR, false);
        inviteButton.setX(x + (entryWidth - this.inviteButton.getWidth() - 4));
        inviteButton.setY(y + (entryHeight - this.inviteButton.getHeight()) / 2);
        inviteButton.render(context, mouseX, mouseY, tickDelta);
    }

    @Override
    public List<? extends Element> children() {
        return this.buttons;
    }

    @Override
    public List<? extends Selectable> selectableChildren() {
        return this.buttons;
    }

    public String getName() {
        return this.name;
    }

}

