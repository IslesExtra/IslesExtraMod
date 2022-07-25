package com.kyllian.islesextra.islesextra.client;

import com.kyllian.islesextra.islesextra.IslesExtra;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class ItemEditor {

    private ItemStack result;

    public ItemEditor(ItemConvertible item) {
        result = new ItemStack(item);
    }
    public ItemEditor(ItemStack itemStack) { result = itemStack; }

    public ItemEditor setItem(ItemConvertible item) {
        if (result!=null)
        result = new ItemStack(item, result.getCount());
        else result = new ItemStack(item);
        return this;
    }

    public ItemEditor setName(String name) {
        result.setCustomName(new LiteralText(name));
        return this;
    }

    public ItemEditor setName(Text name) {
        result.setCustomName(name);
        return this;
    }

    public ItemEditor addLore(String ... lines) {
        int i = 0;
        assert result.getNbt() != null;
        for (String line : lines) {
            while (result.getNbt().contains(IslesExtra.MOD_ID + ".lore." + i)) i++;
            NbtCompound nbt = result.getNbt();
            nbt.putString(IslesExtra.MOD_ID + ".lore." + i, line);
        }
        return this;
    }

    public ItemEditor setGlowing() {
        result.addEnchantment(Enchantments.PROTECTION, 1);
        result.addHideFlag(ItemStack.TooltipSection.ENCHANTMENTS);
        return this;
    }

    public ItemStack getResult() {
        return result;
    }

}
