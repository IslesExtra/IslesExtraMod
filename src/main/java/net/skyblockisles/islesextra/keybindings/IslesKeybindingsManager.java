package net.skyblockisles.islesextra.keybindings;

import static net.skyblockisles.islesextra.IslesExtra.MOD_ID;

import java.util.EnumMap;
import java.util.Map;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.KeyBinding.Category;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
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

    public static KeyBinding getBinding(IslesKeybinding definition) {
        return BINDINGS.get(definition);
    }
}
