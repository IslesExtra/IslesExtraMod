package net.skyblockisles.islesextra.config.invshortcut;

import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.YACLScreen;
import dev.isxander.yacl3.gui.controllers.ControllerWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class InvShortcutWidgetElement extends ControllerWidget<InvShortcutController> {

    public InvShortcutWidgetElement(InvShortcutController control, YACLScreen screen, Dimension<Integer> dim) {
        super(control, screen, dim);
    }

    @Override
    protected void drawValueText(DrawContext graphics, int mouseX, int mouseY, float delta) {
        TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
        Text text = control.formatValue();

        String iconString = control.option().pendingValue().iconPath();
        ItemStack itemStack = getItemFromString(iconString);

        int totalWidth = renderer.getWidth(text) + 20;
        int startX = getDimension().centerX() - (totalWidth / 2);
        int centerY = getDimension().centerY();

        graphics.drawItem(itemStack, startX, centerY - 8);
        graphics.drawTextWithShadow(renderer, text, startX + 20, centerY - (renderer.fontHeight / 2), 0xFFFFFFFF);
    }

    private ItemStack getItemFromString(String id) {
        try {
            Identifier identifier = id.contains(":") ? Identifier.tryParse(id) : Identifier.of("minecraft", id);
            if (identifier == null) return new ItemStack(Items.BARRIER);

            Item item = Registries.ITEM.get(identifier);
            if (item == Items.AIR) return new ItemStack(Items.BARRIER);

            return new ItemStack(item);
        } catch (Exception e) {
            return new ItemStack(Items.BARRIER);
        }
    }

    @Override
    public boolean onMouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseOver(mouseX, mouseY)) {
            playDownSound();
            MinecraftClient.getInstance().setScreen(new InvShortcutEditScreen(screen, control.option()));
            return true;
        }
        return false;
    }

    @Override
    protected int getHoveredControlWidth() {
        return MinecraftClient.getInstance().textRenderer.getWidth(control.formatValue()) + 30;
    }

    @Override
    protected int getUnhoveredControlWidth() {
        return getHoveredControlWidth();
    }
}