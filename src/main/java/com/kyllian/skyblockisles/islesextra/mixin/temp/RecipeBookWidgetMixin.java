package com.kyllian.skyblockisles.islesextra.mixin.temp;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.client.gui.screen.recipebook.RecipeResultCollection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.List;

@Mixin(RecipeBookWidget.class)
public class RecipeBookWidgetMixin {

    @Shadow protected MinecraftClient client;

    @ModifyVariable(method = "refreshResults", at = @At("STORE"), ordinal = 0)
    List<RecipeResultCollection> orderRecipes(List<RecipeResultCollection> value) {
        List<RecipeResultCollection> list = new ArrayList<>(value);
        list.sort((o1, o2) -> {
            if (o1.getAllRecipes() == null || o1.getAllRecipes().isEmpty()) return 0;
            if (o2.getAllRecipes() == null || o2.getAllRecipes().isEmpty()) return 1;
            return o1.getAllRecipes().get(0).getOutput(this.client.world.getRegistryManager()).getName().getString()
                    .compareTo(o2.getAllRecipes().get(0).getOutput(this.client.world.getRegistryManager()).getName().getString());
        });
        return list;
    }



}
