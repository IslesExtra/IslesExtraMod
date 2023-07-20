package com.kyllian.skyblockisles.islesextra.mixin;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow public abstract boolean isDamaged();

    @Shadow public abstract List<Text> getTooltip(@Nullable PlayerEntity player, TooltipContext context);

    @Shadow public abstract boolean hasNbt();

    @Shadow private @Nullable NbtCompound nbt;

    @Shadow public abstract int getMaxDamage();

    @Shadow public abstract int getDamage();

    @Inject(method = "getTooltip", at = @At("HEAD"), cancellable = true)
    void getIslesTooltip(@Nullable PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> cir) {
        if (!context.isAdvanced()) return;
        if (nbt != null) System.out.println(nbt);
        if (nbt == null || !nbt.contains("MYTHIC_TYPE")) return;
        List<Text> list = getTooltip(player, TooltipContext.BASIC);
        if (isDamaged()) {
            list.add(Text.translatable("item.durability", getMaxDamage() - getDamage(), getMaxDamage()));
        }
        list.add(Text.literal("isles:" + nbt.getString("MYTHIC_TYPE")).formatted(Formatting.DARK_GRAY));
        if (hasNbt()) {
            list.add(Text.translatable("item.nbt_tags", nbt.getKeys().size()).formatted(Formatting.DARK_GRAY));
        }
        cir.setReturnValue(list);
    }

}
