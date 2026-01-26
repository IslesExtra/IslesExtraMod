package net.skyblockisles.islesextra.chat;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class ChatPreview {
    @Nullable
    private Text previewText;
    private long currentFadeTime;
    private long lastRenderTime;

    public static boolean shouldShiftUp = false;

    public static boolean shouldShiftUp() {
        return shouldShiftUp;
    }

    public static void setShouldShiftUp(boolean shouldShiftUp) {
        ChatPreview.shouldShiftUp = shouldShiftUp;
    }

    public void init(long currentTime) {
        this.previewText = null;
        this.currentFadeTime = 0L;
        this.lastRenderTime = currentTime;
    }

    public RenderData computeRenderData(long currentTime, @Nullable Text previewText) {
        long l = currentTime - this.lastRenderTime;
        this.lastRenderTime = currentTime;
        return previewText != null ? this.computeRenderDataWithText(l, previewText) : this.computeRenderDataWithoutText(l);
    }

    private RenderData computeRenderDataWithText(long timeDelta, Text previewText) {
        this.previewText = previewText;
        if (this.currentFadeTime < 200L) {
            this.currentFadeTime = Math.min(this.currentFadeTime + timeDelta, 200L);
        }

        return new RenderData(previewText, toAlpha(this.currentFadeTime));
    }

    private RenderData computeRenderDataWithoutText(long timeDelta) {
        if (this.currentFadeTime > 0L) {
            this.currentFadeTime = Math.max(this.currentFadeTime - timeDelta, 0L);
        }

        return this.currentFadeTime > 0L ? new RenderData(this.previewText, toAlpha(this.currentFadeTime)) : RenderData.EMPTY;
    }

    private static float toAlpha(long timeDelta) {
        return (float)timeDelta / 200.0F;
    }

    public record RenderData(@Nullable Text preview, float alpha) {
        public static final RenderData EMPTY = new RenderData(null, 0.0F);

        @Nullable
        public Text preview() {
            return this.preview;
        }

        public float alpha() {
            return this.alpha;
        }
    }
}

