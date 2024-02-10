package com.isles.skyblockisles.islesextra.mixin;

import com.isles.skyblockisles.islesextra.IslesExtra;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.NoteBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(DebugHud.class)
public class DebugHudMixin {
    @Shadow private HitResult blockHit;

    @Shadow @Final private MinecraftClient client;

    // TODO; cleanup / rework this method
    @Inject(method = "getRightText", at = @At("TAIL"), cancellable = true)
    private void getRightText(CallbackInfoReturnable<List<String>> ci) {
        List<String> list = ci.getReturnValue();
        if (list == null) return;
        if (this.blockHit.getType() != HitResult.Type.BLOCK) return;
        if (this.client.world == null) return;
        BlockState blockState = this.client.world.getBlockState(((BlockHitResult) this.blockHit).getBlockPos());
        Block block = blockState.getBlock();
        if (!(block instanceof NoteBlock)) return;
        List<String> removal = new ArrayList<>();
        String instrument = "";
        int note = -1;
        int idPos = 0;
        for (int i = 0; i < list.size(); i++) {
            String line = list.get(i);
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
        if (note == -1 || instrument.isEmpty()) list.set(idPos, "isles:unknown");
        else {
            list.set(idPos, "isles:" + IslesExtra.getId(instrument, note));
        }
        list.removeAll(removal);
        ci.setReturnValue(list);
    }
}
