package net.skyblockisles.islesextra.mixin;

import com.google.common.collect.Maps;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.gui.hud.ClientBossBar;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.network.packet.s2c.play.BossBarS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.profiler.Profilers;
import net.skyblockisles.islesextra.IslesClientState;
import net.skyblockisles.islesextra.config.IslesConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Mixin(BossBarHud.class)
public class BossBarRenderMixin {

    private static final Identifier[] BACKGROUND_TEXTURES = new Identifier[]{Identifier.ofVanilla("boss_bar/pink_background"), Identifier.ofVanilla("boss_bar/blue_background"), Identifier.ofVanilla("boss_bar/red_background"), Identifier.ofVanilla("boss_bar/green_background"), Identifier.ofVanilla("boss_bar/yellow_background"), Identifier.ofVanilla("boss_bar/purple_background"), Identifier.ofVanilla("boss_bar/white_background")};
    private static final Identifier[] PROGRESS_TEXTURES = new Identifier[]{Identifier.ofVanilla("boss_bar/pink_progress"), Identifier.ofVanilla("boss_bar/blue_progress"), Identifier.ofVanilla("boss_bar/red_progress"), Identifier.ofVanilla("boss_bar/green_progress"), Identifier.ofVanilla("boss_bar/yellow_progress"), Identifier.ofVanilla("boss_bar/purple_progress"), Identifier.ofVanilla("boss_bar/white_progress")};
    private static final Identifier[] NOTCHED_BACKGROUND_TEXTURES = new Identifier[]{Identifier.ofVanilla("boss_bar/notched_6_background"), Identifier.ofVanilla("boss_bar/notched_10_background"), Identifier.ofVanilla("boss_bar/notched_12_background"), Identifier.ofVanilla("boss_bar/notched_20_background")};
    private static final Identifier[] NOTCHED_PROGRESS_TEXTURES = new Identifier[]{Identifier.ofVanilla("boss_bar/notched_6_progress"), Identifier.ofVanilla("boss_bar/notched_10_progress"), Identifier.ofVanilla("boss_bar/notched_12_progress"), Identifier.ofVanilla("boss_bar/notched_20_progress")};
    final Map<UUID, ClientBossBar> bossBars = Maps.newLinkedHashMap();

    /**
     * @author wechandoit
     * @reason remove quest text on the top right
     */
    @Overwrite
    public void render(DrawContext drawContext) {
        if (!this.bossBars.isEmpty()) {
            drawContext.createNewRootLayer();
            Profiler profiler = Profilers.get();
            profiler.push("bossHealth");
            int i = drawContext.getScaledWindowWidth();
            int j = 12;

            for(ClientBossBar clientBossBar : this.bossBars.values()) {
                int k = i / 2 - 91;
                this.renderBossBar(drawContext, k, j, clientBossBar);
                Text text = clientBossBar.getName();
                int m = MinecraftClient.getInstance().textRenderer.getWidth(text);
                int n = i / 2 - m / 2;
                int o = j - 9;

                // redo this so we can account for the edge case that there is npc dialogue/quest info/hotbar
                // as this only accounts for quest info/hotbar

                if (IslesConfig.HANDLER.instance().hideQuestInfo && IslesClientState.isOnIsles() && text != null) {
                    if (text.getString().indexOf("\uF058") > 0) {
                        Text newText = text.copy();
                        newText.getSiblings().clear();

                        if (text.withoutStyle().size() > 1) {
                            boolean skip = false;
                            for (int a = 0; a < text.withoutStyle().size(); a++) {
                                if (!skip) {
                                    if (text.withoutStyle().get(a).getString().contains("\uF086")) {
                                        skip = true;
                                    } else {
                                        newText.getSiblings().add(text.withoutStyle().get(a));
                                    }
                                }
                                if (text.withoutStyle().get(a).getString().contains("\uF058")) {
                                    newText.getSiblings().add(text.withoutStyle().get(a));
                                    skip = false;
                                }
                            }
                        }
                        text = newText;
                    }
                }

                drawContext.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, text, n, o, -1);
                Objects.requireNonNull(MinecraftClient.getInstance().textRenderer);
                j += 10 + 9;
                if (j >= drawContext.getScaledWindowHeight() / 3) {
                    break;
                }
            }

            profiler.pop();
        }
    }

    private void renderBossBar(DrawContext drawContext, int i, int j, BossBar bossBar) {
        this.renderBossBar(drawContext, i, j, bossBar, 182, BACKGROUND_TEXTURES, NOTCHED_BACKGROUND_TEXTURES);
        int k = MathHelper.lerpPositive(bossBar.getPercent(), 0, 182);
        if (k > 0) {
            this.renderBossBar(drawContext, i, j, bossBar, k, PROGRESS_TEXTURES, NOTCHED_PROGRESS_TEXTURES);
        }

    }

    private void renderBossBar(DrawContext drawContext, int i, int j, BossBar bossBar, int k, Identifier[] identifiers, Identifier[] identifiers2) {
        drawContext.drawGuiTexture(RenderPipelines.GUI_TEXTURED, identifiers[bossBar.getColor().ordinal()], 182, 5, 0, 0, i, j, k, 5);
        if (bossBar.getStyle() != BossBar.Style.PROGRESS) {
            drawContext.drawGuiTexture(RenderPipelines.GUI_TEXTURED, identifiers2[bossBar.getStyle().ordinal() - 1], 182, 5, 0, 0, i, j, k, 5);
        }

    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void handlePacket(BossBarS2CPacket bossBarS2CPacket) {
        bossBarS2CPacket.accept(new BossBarS2CPacket.Consumer() {
            public void add(UUID uUID, Text text, float f, BossBar.Color color, BossBar.Style style, boolean bl, boolean bl2, boolean bl3) {
                bossBars.put(uUID, new ClientBossBar(uUID, text, f, color, style, bl, bl2, bl3));
            }

            public void remove(UUID uUID) {
                bossBars.remove(uUID);
            }

            public void updateProgress(UUID uUID, float f) {
                ((ClientBossBar)bossBars.get(uUID)).setPercent(f);
            }

            public void updateName(UUID uUID, Text text) {
                ((ClientBossBar)bossBars.get(uUID)).setName(text);
            }

            public void updateStyle(UUID uUID, BossBar.Color color, BossBar.Style style) {
                ClientBossBar clientBossBar = (ClientBossBar)bossBars.get(uUID);
                clientBossBar.setColor(color);
                clientBossBar.setStyle(style);
            }

            public void updateProperties(UUID uUID, boolean bl, boolean bl2, boolean bl3) {
                ClientBossBar clientBossBar = (ClientBossBar)bossBars.get(uUID);
                clientBossBar.setDarkenSky(bl);
                clientBossBar.setDragonMusic(bl2);
                clientBossBar.setThickenFog(bl3);
            }
        });
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void clear() {
        this.bossBars.clear();
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean shouldPlayDragonMusic() {
        if (!this.bossBars.isEmpty()) {
            for(BossBar bossBar : this.bossBars.values()) {
                if (bossBar.hasDragonMusic()) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean shouldDarkenSky() {
        if (!this.bossBars.isEmpty()) {
            for(BossBar bossBar : this.bossBars.values()) {
                if (bossBar.shouldDarkenSky()) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean shouldThickenFog() {
        if (!this.bossBars.isEmpty()) {
            for(BossBar bossBar : this.bossBars.values()) {
                if (bossBar.shouldThickenFog()) {
                    return true;
                }
            }
        }

        return false;
    }
}
