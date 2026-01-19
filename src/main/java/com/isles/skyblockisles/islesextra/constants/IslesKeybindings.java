package com.isles.skyblockisles.islesextra.constants;

import static com.isles.skyblockisles.islesextra.client.IslesExtraClient.MOD_ID;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public enum IslesKeybindings {
  OPEN_PARTY_SCREEN(
      Identifier.of(MOD_ID, "keybinds.open_party"),
      InputUtil.Type.KEYSYM,
      GLFW.GLFW_KEY_P,
      IslesKeybindingsCategory.GENERAL
  );

  private final Identifier id;
  private final IslesKeybindingsCategory category;
  private final KeyBinding binding;

  IslesKeybindings(Identifier id, InputUtil.Type type, int keyId, IslesKeybindingsCategory category) {
    this.id = id;
    this.category = category;
    this.binding = new KeyBinding(id.toString(), type, keyId, category.getCategory());
  }

  public Identifier getIdentifier() {
    return id;
  }

  public IslesKeybindingsCategory getCategory() {
    return category;
  }

  public KeyBinding getBinding() {
    return binding;
  }

  public static void register() {
    for (IslesKeybindings keybind : IslesKeybindings.values()) {
      KeyBindingHelper.registerKeyBinding(keybind.getBinding());
    }
  }
}
