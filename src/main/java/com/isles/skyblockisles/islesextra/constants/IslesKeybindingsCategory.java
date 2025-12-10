package com.isles.skyblockisles.islesextra.constants;

import static com.isles.skyblockisles.islesextra.client.IslesExtraClient.MOD_ID;

import net.minecraft.client.option.KeyBinding.Category;
import net.minecraft.util.Identifier;

enum IslesKeybindingsCategory {
  GENERAL(Category.create(Identifier.of(MOD_ID, "general")));

  private final Category category;

  IslesKeybindingsCategory(Category category) {
    this.category = category;
  }

  public Category getCategory() {
    return this.category;
  }
}
