package net.skyblockisles.islesextra.config;

import com.google.gson.GsonBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import net.skyblockisles.islesextra.IslesExtra;

public class IslesConfig {
    public static ConfigClassHandler<IslesConfig> HANDLER = ConfigClassHandler.createBuilder(IslesConfig.class)
        .id(Identifier.of(IslesExtra.MOD_ID, "isles_config"))
        .serializer(config -> GsonConfigSerializerBuilder.create(config)
                .setPath(FabricLoader.getInstance().getConfigDir().resolve("isles_config.json5"))
                .appendGsonBuilder(GsonBuilder::setPrettyPrinting)
                .setJson5(true)
                .build())
        .build();

    @SerialEntry
    public boolean enableQTEHelper = true;

    @SerialEntry
    public boolean enableQTEHelperTitle = true;
}
