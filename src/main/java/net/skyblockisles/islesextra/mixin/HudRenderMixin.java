package net.skyblockisles.islesextra.mixin;

import com.google.common.collect.Ordering;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.texture.MissingSprite;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.skyblockisles.islesextra.IslesClientState;
import net.skyblockisles.islesextra.IslesExtra;
import net.skyblockisles.islesextra.bosstimer.AlphaBoarTimer;
import net.skyblockisles.islesextra.config.IslesConfig;
import net.skyblockisles.islesextra.objects.DummyEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;

@Mixin(InGameHud.class)
public class HudRenderMixin {

    @Unique
    private static final Identifier EFFECT_BACKGROUND_AMBIENT_TEXTURE = Identifier.ofVanilla("hud/effect_background_ambient");
    @Unique
    private static final Identifier EFFECT_BACKGROUND_TEXTURE = Identifier.ofVanilla("hud/effect_background");
    @Unique
    private static final Identifier EFFECT_BACKGROUND_SPRITE_MENU = Identifier.ofVanilla("container/inventory/effect_background");
    @Unique
    private static final Identifier ALPHA_BOAR_ICON = Identifier.of(IslesExtra.MOD_ID, "textures/gui/alpha_boar.png");

    /**
     * @author wechandoit
     * @reason Overwrite the existing status effect method to add custom icons
     */
    @Overwrite
    private void renderStatusEffectOverlay(DrawContext drawContext, RenderTickCounter renderTickCounter) {
        if (MinecraftClient.getInstance().player == null) return;
        List<StatusEffectInstance> statusEffects = new ArrayList<>(Ordering.natural().sortedCopy(MinecraftClient.getInstance().player.getStatusEffects()).stream().toList());

        if (IslesClientState.isOnIsles() && !AlphaBoarTimer.alphaBoarTimerStr.isEmpty() && IslesConfig.HANDLER.instance().enableWorldBossTimer) {
            statusEffects.add(new DummyEffectInstance(ALPHA_BOAR_ICON, AlphaBoarTimer.alphaBoarTimerStr));
        }

        if (!statusEffects.isEmpty() && (MinecraftClient.getInstance().currentScreen == null || !MinecraftClient.getInstance().currentScreen.showsStatusEffects())) {
            int beneficialCount = 0;
            int nonBeneficialCount = 0;

            for(StatusEffectInstance statusEffectInstance : statusEffects) {
                RegistryEntry<StatusEffect> registryEntry = statusEffectInstance.getEffectType();
                int x = drawContext.getScaledWindowWidth();
                int y = 1;
                if (statusEffectInstance instanceof DummyEffectInstance dummyEffectInstance) {
                    int simDistance = getChunksBetweenPlayerAndBlock(MinecraftClient.getInstance().player.getBlockX(), MinecraftClient.getInstance().player.getBlockZ(), -335, 750);
                    int playerSimDistance = MinecraftClient.getInstance().options.getSimulationDistance().getValue();
                    if (AlphaBoarTimer.willSummonBoar) {
                        String parsedText = convertSecondsToTimeFormatConcise(AlphaBoarTimer.alphaBoarTimer);
                        if (!parsedText.isEmpty()) {
                            int width = 32 + MinecraftClient.getInstance().textRenderer.getWidth(parsedText) + 7;
                            x -= width;
                            drawContext.drawGuiTexture(RenderPipelines.GUI_TEXTURED, EFFECT_BACKGROUND_SPRITE_MENU, x, y, width, 32);

                            Text text = Text.literal(parsedText).formatted(Formatting.RED);
                            drawContext.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, text, x + 32, y + 7, -1);

                            drawContext.drawTexture(RenderPipelines.GUI_TEXTURED, dummyEffectInstance.getIconID(), x + 7, y + 7, 0, 0, 18, 18, 18, 18);
                        }
                    } else if (simDistance <= Math.min(12, playerSimDistance)) {
                        String parsedText = parseAlphaBoarText();
                        if (!parsedText.isEmpty()) {
                            int width = 32 + MinecraftClient.getInstance().textRenderer.getWidth(parsedText) + 7;
                            x -= width;
                            drawContext.drawGuiTexture(RenderPipelines.GUI_TEXTURED, EFFECT_BACKGROUND_SPRITE_MENU, x, y, width, 32);

                            Text text = Text.literal(parsedText).formatted(Formatting.GREEN);
                            drawContext.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, text, x + 32, y + 7, -1);

                            drawContext.drawTexture(RenderPipelines.GUI_TEXTURED, dummyEffectInstance.getIconID(), x + 7, y + 7, 0, 0, 18, 18, 18, 18);
                        }
                    }
                } else if (statusEffectInstance.shouldShowIcon()) {
                    if (MinecraftClient.getInstance().isDemo()) {
                        y += 15;
                    }

                    if (((StatusEffect)registryEntry.comp_349()).isBeneficial()) {
                        ++beneficialCount;
                        x -= 25 * beneficialCount;
                    } else {
                        ++nonBeneficialCount;
                        x -= 25 * nonBeneficialCount;
                        y += 26;
                    }

                    float f = 1.0F;
                    if (statusEffectInstance.isAmbient()) {
                        drawContext.drawGuiTexture(RenderPipelines.GUI_TEXTURED, EFFECT_BACKGROUND_AMBIENT_TEXTURE, x, y, 24, 24);
                    } else {
                        drawContext.drawGuiTexture(RenderPipelines.GUI_TEXTURED, EFFECT_BACKGROUND_TEXTURE, x, y, 24, 24);
                        if (statusEffectInstance.isDurationBelow(200)) {
                            int m = statusEffectInstance.getDuration();
                            int n = 10 - m / 20;
                            f = MathHelper.clamp((float)m / 10.0F / 5.0F * 0.5F, 0.0F, 0.5F) + MathHelper.cos((double)((float)m * (float)Math.PI / 5.0F)) * MathHelper.clamp((float)n / 10.0F * 0.25F, 0.0F, 0.25F);
                            f = MathHelper.clamp(f, 0.0F, 1.0F);
                        }
                    }

                    drawContext.drawGuiTexture(RenderPipelines.GUI_TEXTURED, getEffectTexture(registryEntry), x + 3, y + 3, 18, 18, ColorHelper.getWhite(f));
                }
            }

        }
    }

    @Unique
    private static Identifier getEffectTexture(RegistryEntry<StatusEffect> registryEntry) {
        return (Identifier)registryEntry.getKey().map(RegistryKey::getValue).map((identifier) -> identifier.withPrefixedPath("mob_effect/")).orElseGet(MissingSprite::getMissingSpriteId);
    }

    // calculate chunk distance between this display and the player, if the entity is out of simulation distance it will not update correctly
    // to fix this behavior, I want to make some flask server backend to host actual updates but if not just don't render the timer in the hud i guess
    public int getChunksBetweenPlayerAndBlock(int playerX, int playerZ, int targetX, int targetZ) {
        int diffX = Math.abs((playerX >> 4) - (targetX >> 4));
        int diffZ = Math.abs((playerZ >> 4) - (targetZ >> 4));

        return Math.max(diffX, diffZ) + 1; // also includes chunk target and player are in
    }

    private String parseAlphaBoarText() {
        if (AlphaBoarTimer.alphaBoarTimerStr.isEmpty()) return "";
        try {
            int totalSeconds = Integer.parseInt(AlphaBoarTimer.alphaBoarTimerStr.replaceAll("[^0-9]", ""));
            if (totalSeconds > 0) return convertSecondsToTimeFormatConcise(totalSeconds);
            else return "";
        } catch (NumberFormatException exception) {
            return "READY TO SPAWN";
        }
    }

    private String convertSecondsToTimeFormatConcise(int totalSeconds) {

        int h = totalSeconds / 3600;
        int m = (totalSeconds % 3600) / 60;
        int s = totalSeconds % 60;

        if (h > 0) {
            return String.format("%02d:%02d:%02d", h, m, s);
        } else {
            return String.format("%02d:%02d", m, s);
        }
    }
}
