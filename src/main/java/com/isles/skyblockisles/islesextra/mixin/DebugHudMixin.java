package com.isles.skyblockisles.islesextra.mixin;

import com.isles.skyblockisles.islesextra.client.resources.CustomBlockListener;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.NoteBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(DebugHud.class)
public class DebugHudMixin {
    @Shadow @Final private MinecraftClient client;

    // TODO; cleanup / rework this method
    @Inject(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/hud/DebugHud;drawText(Lnet/minecraft/client/gui/DrawContext;Ljava/util/List;Z)V",
            ordinal = 1 // 0 is Left Text, 1 is Right Text
        )
    )
    private void getRightText(DrawContext drawContext, CallbackInfo ci, @Local(ordinal = 1) List<String> list2) {
        if (list2 == null) return;

        HitResult hit = this.client.crosshairTarget;

        if (hit == null || hit.getType() != HitResult.Type.BLOCK) return;
        if (this.client.world == null) return;
        BlockState blockState = this.client.world.getBlockState(((BlockHitResult) hit).getBlockPos());
        Block block = blockState.getBlock();
        if (!(block instanceof NoteBlock)) return;
        List<String> removal = new ArrayList<>();
        String instrument = "";
        int note = -1;
        int idPos = 0;
        for (int i = 0; i < list2.size(); i++) {
            String line = list2.get(i);
            if (line.startsWith("minecraft:note_block")) idPos = i;
            else if (line.startsWith("instrument:")) {
                removal.add(line);
                instrument = line.replace("instrument: ", "");
            } else if (line.startsWith("note:")) {
                removal.add(line);
                note = Integer.parseInt(line.replace("note: ", ""));
            } else if (line.startsWith("powered:")) removal.add(line);
            else if (line.startsWith("#minecraft:mineable/axe")) removal.add(line);
        }
        if (note == -1 || instrument.isEmpty()) list2.set(idPos, "isles:unknown");
        else {
            list2.set(idPos, "isles:" + CustomBlockListener.getId(instrument, note));
        }
        list2.removeAll(removal);
    }
}
