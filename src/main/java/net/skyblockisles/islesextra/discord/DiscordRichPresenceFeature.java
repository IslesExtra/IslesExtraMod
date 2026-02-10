package net.skyblockisles.islesextra.discord;

public class DiscordRichPresenceFeature {

    public void enableRichPresence() {
        // This isReady() check is required for Linux to not crash on config change.
        if (!DiscordHandler.Discord.isReady()) {
            // Load the Discord SDK
            if (!DiscordHandler.Discord.load()) {
                // happens when wrong version of GLIBC is installed and Discord SDK fails to load
                return;
            }
        }

        tryUpdateDisplayedInfo();
    }

    public void disableRichPresence() {
        DiscordHandler.Discord.unload();
    }

    private void tryUpdateDisplayedInfo() {
        if (!DiscordHandler.Discord.isReady()) return;

        DiscordHandler.Discord.setState("");
    }

}
