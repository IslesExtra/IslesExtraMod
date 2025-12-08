package com.isles.skyblockisles.islesextra.mixin;

import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
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

    @Shadow public abstract List<Text> getTooltip(Item.TooltipContext tooltipContext, @Nullable PlayerEntity playerEntity, TooltipType tooltipType);

    @Shadow public abstract int getMaxDamage();

    @Shadow public abstract int getDamage();

    @Shadow public abstract ComponentMap getComponents();

    @Inject(method = "getTooltip", at = @At("HEAD"), cancellable = true)
    void getIslesTooltip(Item.TooltipContext context, @Nullable PlayerEntity player, TooltipType tooltipType, CallbackInfoReturnable<List<Text>> cir) {
        var  nbtComponent = getComponents().get(DataComponentTypes.CUSTOM_DATA);
        if (nbtComponent == null) return;
        var nbt = nbtComponent.copyNbt();
        if (!nbt.contains("MYTHIC_TYPE")) return;
        List<Text> list = getTooltip(context, player, tooltipType);
        if (isDamaged()) {
            list.add(Text.translatable("item.durability", getMaxDamage() - getDamage(), getMaxDamage()));
        }
        list.add(Text.literal("isles:" + nbt.getString("MYTHIC_TYPE")).formatted(Formatting.DARK_GRAY));
        if (!nbt.isEmpty()) {
            list.add(Text.translatable("item.nbt_tags", nbt.getKeys().size()).formatted(Formatting.DARK_GRAY));
        }
        cir.setReturnValue(list);
    }

}
