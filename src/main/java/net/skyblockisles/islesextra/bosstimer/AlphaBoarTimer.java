package net.skyblockisles.islesextra.bosstimer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.DisplayEntity;
import net.skyblockisles.islesextra.IslesClientState;
import net.skyblockisles.islesextra.annotations.Init;

import java.util.ArrayList;
import java.util.List;

public class AlphaBoarTimer {

    private static final ArrayList<DisplayEntity.TextDisplayEntity> pendingTextDisplays = new ArrayList<>();

    public static String alphaBoarTimerStr = "";
    public static boolean willSummonBoar = false;
    public static int alphaBoarTimer = 0; // seconds

    private static int clientTick = 1;

    @Init
    public static void init() {
        ClientEntityEvents.ENTITY_LOAD.register((entity, world) -> registerEntity(entity));
        ClientTickEvents.END_WORLD_TICK.register(clientWorld -> runnableRunner());
    }

    private static void runnableRunner() {
        clientTick++;

        // process entities for boar timer
        setAlphaBoarTimerText();

        if (clientTick > 20) clientTick = 1;
        else if (clientTick == 20) {
            if (IslesClientState.isOnIsles()) {
                if (alphaBoarTimer - 1 < 0) {
                    alphaBoarTimer = 0;
                    willSummonBoar = false;
                }
                else {
                    alphaBoarTimer -= 1;
                }
            } else {
                alphaBoarTimer = 0;
            }
        }
    }

    private static void registerEntity(Entity entity) {
        if (IslesClientState.isOnIsles()) {
            if (entity instanceof DisplayEntity.TextDisplayEntity textDisplay) {
                pendingTextDisplays.add(textDisplay);
            }
        }
    }

    private static void setAlphaBoarTimerText() {
        for (DisplayEntity.TextDisplayEntity textDisplay : pendingTextDisplays) {
            if (textDisplay.getX() == -335.0 && textDisplay.getY() == 106.0 && textDisplay.getZ() == 750.0) {
                List<String> pendingUnparsedBoarTimer = List.of(textDisplay.getText().getString().split("\n"));
                if (pendingUnparsedBoarTimer.size() == 3) {
                    alphaBoarTimerStr = pendingUnparsedBoarTimer.get(2);
                } else {
                    alphaBoarTimerStr = "";
                }
            }
        }
    }
}
