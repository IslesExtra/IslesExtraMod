package net.skyblockisles.islesextra.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.skyblockisles.islesextra.config.IslesConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.entity.decoration.DisplayEntity.TextDisplayEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.skyblockisles.islesextra.annotations.Init;
import net.skyblockisles.islesextra.callback.SwitchedIslesServerCallback;
import net.skyblockisles.islesextra.constants.MessageScheduler;
import net.skyblockisles.islesextra.constants.Renderer;

public class QuickTimeEventHelper {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final ArrayList<TextDisplayEntity> pendingTexts = new ArrayList<>();
    private static final Pattern BONUS_PATTERN = Pattern.compile("(?s)CLICK ME\\s+BONUS! \\+(\\d+%?)\\s*(.*)");
    private static final Map<String, Formatting> colorMap = Map.of(
        "Luck", Formatting.LIGHT_PURPLE,
        "Coins", Formatting.YELLOW,
        "Exp", Formatting.GREEN,
        "Chance", Formatting.GREEN
    );
    
    @Init
    public static void init() {
        ClientEntityEvents.ENTITY_LOAD.register((entity, world) -> registerEntity(entity));
        ClientTickEvents.END_WORLD_TICK.register(world -> processEntities(pendingTexts));
        SwitchedIslesServerCallback.EVENT.register(() -> { Renderer.setTarget(null); return ActionResult.PASS; });
    }

    private static void registerEntity(Entity entity) {
        if (entity instanceof DisplayEntity.TextDisplayEntity textEntity ) {
            pendingTexts.add(textEntity);
        }
    }

    public static void processEntities(List<TextDisplayEntity> texts) {
        if (!IslesConfig.HANDLER.instance().enableQTEHelper) return;
        if (texts.isEmpty()) return;
        texts.removeIf(textEntity -> {
            if (textEntity.isRemoved()) return true;

            String content = textEntity.getText().getString();
            if (content.isEmpty()) return false;

            Matcher bonusMatcher = BONUS_PATTERN.matcher(content);

            if (bonusMatcher.find()) {
                String amount = bonusMatcher.group(1);
                String type = bonusMatcher.group(2);
                LOGGER.info("Found Bonus {} {} from {}", amount, type, content);
                int color = 0xFF000000 | colorMap.getOrDefault(type, Formatting.GREEN).getColorValue();
                if (IslesConfig.HANDLER.instance().enableQTEHelperTitle)
                    MessageScheduler.scheduleTitle(Text.literal("BONUS: " + amount + " " + type).styled(style -> style.withColor(color)));
                Renderer.setColor(color);
                Renderer.setTarget(textEntity);
            }

            return true;
        });
    }
}
