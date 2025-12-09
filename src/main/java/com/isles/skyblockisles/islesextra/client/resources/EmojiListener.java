package com.isles.skyblockisles.islesextra.client.resources;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class EmojiListener implements SynchronousResourceReloader {

    private static final Set<String> emojis = new HashSet<>();
    public static Set<String> getEmojis() { return emojis; }

    @Override
    public void reload(ResourceManager manager) {
        HashMap<String, String> emojiMap = new HashMap<>();
        for (Identifier id : manager.findResources("font", path -> path.getPath().endsWith(".json")).keySet()) {
            Optional<Resource> resource = manager.getResource(id);
            if (resource.isEmpty()) continue;
            try (InputStream stream = resource.get().getInputStream()) {
                if (stream == null) continue;
                JsonObject o = (JsonObject) JsonParser.parseReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
                JsonArray providers = o.getAsJsonArray("providers");
                for (JsonElement e : providers) {
                    // TODO; add some more checks here to prevent errors
                    JsonObject provider = (JsonObject) e;
                    if (!provider.get("type").getAsString().equals("bitmap")) continue;
                    if (!provider.has("file")) continue;
                    String path = provider.get("file").getAsString();
                    if (!path.startsWith("custom/emojis")) continue;
                    String emojiName = path.replace("custom/emojis/", "").replace(".png", "");
                    emojiMap.put(":" + emojiName + ":", provider.getAsJsonArray("chars").get(0).getAsString());
                }
            }
            catch (IOException ignored) {}
        }
        emojis.clear();
        for (String emoji : emojiMap.keySet()) {
            emojis.add(emojiMap.get(emoji) + " " + emoji);
        }
    }

}
