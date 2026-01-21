package com.isles.skyblockisles.islesextra.constants;

import static com.isles.skyblockisles.islesextra.IslesExtraClient.MOD_ID;

import java.util.EnumMap;
import java.util.Map;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.KeyBinding.Category;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;

public class IslesKeybindingsManager {
    private static final Map<IslesKeybinding, KeyBinding> BINDINGS = new EnumMap<>(IslesKeybinding.class);

    public static void register() {
        for (IslesKeybinding keybind : IslesKeybinding.values()) {
            Identifier keyId = Identifier.of(MOD_ID, keybind.getName());
            Identifier categoryId = Identifier.of(MOD_ID, keybind.getCategory().getName());

            Category category = Category.create(categoryId);
            KeyBinding binding = new KeyBinding(keyId.toString(), InputUtil.Type.KEYSYM, keybind.getDefaultKey(), category);

            BINDINGS.put(keybind, binding);
            KeyBindingHelper.registerKeyBinding(binding);
        }
    }

    public static KeyBinding getBinding(IslesKeybinding definition) {
        return BINDINGS.get(definition);
    }
}
