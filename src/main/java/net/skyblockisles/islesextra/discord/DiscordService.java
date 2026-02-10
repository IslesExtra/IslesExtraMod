package net.skyblockisles.islesextra.discord;

import com.wynntils.antiope.core.DiscordGameSDKCore;
import com.wynntils.antiope.core.type.CreateParams;
import com.wynntils.antiope.core.type.GameSDKException;
import com.wynntils.antiope.core.type.Result;
import com.wynntils.antiope.manager.activity.type.Activity;

import java.time.Instant;

public class DiscordService {
    private static final long DISCORD_APPLICATION_ID = 1128526559016394874L;
    private static final int TICKS_PER_UPDATE = 5;

    private CreateParams params;
    private DiscordGameSDKCore core;
    private Activity activity;

    private int ticksUntilUpdate = 0;

    public boolean load() {
        try {
            DiscordGameSDKCore.loadLibrary();
        } catch (UnsatisfiedLinkError e) {
            System.out.println("IslesExtra - Error loading Discord Rich Presence Library!");
            return false;
        }
        if (!isReady()) {
            createCore();
            System.out.println("IslesExtra - Loaded Discord Rich Presence Library!");
        }
        return true;
    }

    public void unload() {
        if (!isReady()) return;
        try {
            activity.close();
            activity = null;
            core.close();
            core = null;
            params.close();
            params = null;
        } catch (GameSDKException e) {
            if (e.getResult() == Result.TRANSACTION_ABORTED) {
                // This occurs when player closes game and JVM exits before we can close the core
                return;
            }
            System.out.println("IslesExtra - Could not unload Discord Game SDK");
        }
    }

    public boolean isReady() {
        return core != null && core.isOpen() && activity != null;
    }

    private void createCore() {
        params = new CreateParams();
        try {
            params.setClientID(DISCORD_APPLICATION_ID);
            params.setFlags(CreateParams.getNoRequireDiscordFlags());
            core = new DiscordGameSDKCore(params);
            activity = new Activity();
            activity.timestamps().setStart(Instant.now());
        } catch (Throwable e) {
            if (e instanceof GameSDKException gameSDKException
                    && gameSDKException.getResult() == Result.INTERNAL_ERROR) {
                // This occurs when player closes game and JVM exits before we can close the core
                return;
            }
            System.out.println("IslesExtra - Could not initialize Discord Game SDK");
        }
    }

    public void setDetails(String details) {
        if (!isReady()) return;
        activity.setDetails(details);
        core.activityManager().updateActivity(activity);
    }

    public void setLargeImage(String imageId) {
        if (!isReady()) return;
        activity.assets().setLargeImage(imageId);
        core.activityManager().updateActivity(activity);
    }

    public void setLargeImageText(String text) {
        if (!isReady()) return;
        activity.assets().setLargeText(text);
        core.activityManager().updateActivity(activity);
    }

    public void setSmallImage(String imageId) {
        if (!isReady()) return;
        activity.assets().setSmallImage(imageId);
        core.activityManager().updateActivity(activity);
    }

    public void setSmallImageText(String text) {
        if (!isReady()) return;
        activity.assets().setSmallText(text);
        core.activityManager().updateActivity(activity);
    }

    public void setState(String state) {
        if (!isReady()) return;
        activity.setState(state);
        core.activityManager().updateActivity(activity);
    }

    public void onTick() {
        if (ticksUntilUpdate > 0) {
            ticksUntilUpdate--;
            return;
        }
        ticksUntilUpdate = TICKS_PER_UPDATE;
        if (!isReady()) return;

        core.runCallbacks();
    }
}
