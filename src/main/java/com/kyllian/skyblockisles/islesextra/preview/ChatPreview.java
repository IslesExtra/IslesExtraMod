package com.kyllian.skyblockisles.islesextra.preview;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextHandler;
import net.minecraft.text.CharacterVisitor;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

@Environment(EnvType.CLIENT)
public class ChatPreview {
    @Nullable
    private Text previewText;
    private long currentFadeTime;
    private long lastRenderTime;

    public void init(long currentTime) {
        this.previewText = null;
        this.currentFadeTime = 0L;
        this.lastRenderTime = currentTime;
    }

    public ChatPreview.RenderData computeRenderData(long currentTime, @Nullable Text previewText) {
        long l = currentTime - this.lastRenderTime;
        this.lastRenderTime = currentTime;
        return previewText != null ? this.computeRenderDataWithText(l, previewText) : this.computeRenderDataWithoutText(l);
    }

    private ChatPreview.RenderData computeRenderDataWithText(long timeDelta, Text previewText) {
        this.previewText = previewText;
        if (this.currentFadeTime < 200L) {
            this.currentFadeTime = Math.min(this.currentFadeTime + timeDelta, 200L);
        }

        return new ChatPreview.RenderData(previewText, toAlpha(this.currentFadeTime));
    }

    private ChatPreview.RenderData computeRenderDataWithoutText(long timeDelta) {
        if (this.currentFadeTime > 0L) {
            this.currentFadeTime = Math.max(this.currentFadeTime - timeDelta, 0L);
        }

        return this.currentFadeTime > 0L ? new ChatPreview.RenderData(this.previewText, toAlpha(this.currentFadeTime)) : ChatPreview.RenderData.EMPTY;
    }

    private static float toAlpha(long timeDelta) {
        return (float)timeDelta / 200.0F;
    }

    public record RenderData(@Nullable Text preview, float alpha) {
        public static final ChatPreview.RenderData EMPTY = new ChatPreview.RenderData((Text)null, 0.0F);

        @Nullable
        public Text preview() {
            return this.preview;
        }

        public float alpha() {
            return this.alpha;
        }
    }
}

