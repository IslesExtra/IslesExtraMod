package net.skyblockisles.islesextra.keybindings;

import java.util.EnumMap;
import java.util.Map;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.KeyBinding.Category;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import static net.skyblockisles.islesextra.IslesExtra.MOD_ID;

import net.skyblockisles.islesextra.annotations.EveryTick;
import net.skyblockisles.islesextra.annotations.Init;

public class IslesKeybindingsManager {
    private static final Map<IslesKeybinding, KeyBinding> BINDINGS = new EnumMap<>(IslesKeybinding.class);

    @Init
    public static void init() {
        for (IslesKeybinding keybind : IslesKeybinding.values()) {
            Identifier keyId = Identifier.of(MOD_ID, keybind.getName());
            Identifier categoryId = Identifier.of(MOD_ID, keybind.getCategory().getName());

            Category category = Category.create(categoryId);
            KeyBinding binding = new KeyBinding(keyId.toString(), InputUtil.Type.KEYSYM, keybind.getDefaultKey(), category);

            BINDINGS.put(keybind, binding);
            KeyBindingHelper.registerKeyBinding(binding);
        }
    }

    @EveryTick
    public static void onTick(MinecraftClient client) {
        if (client.player == null || client.world == null) return;

        if (IslesKeybindingsManager.getBinding(IslesKeybinding.OPEN_PARTY_SCREEN).isPressed()) {

        }
    }

    public static KeyBinding getBinding(IslesKeybinding definition) {
        return BINDINGS.get(definition);
    }
}
