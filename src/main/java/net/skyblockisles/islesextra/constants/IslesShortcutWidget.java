package net.skyblockisles.islesextra.constants;

import java.util.List;
import java.util.function.Consumer;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.skyblockisles.islesextra.config.IslesConfig;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.input.AbstractInput;
import net.minecraft.util.Identifier;
import net.skyblockisles.islesextra.IslesExtra;

public class IslesShortcutWidget extends PressableWidget {
    public static final int WIDTH = 18;
    public static final int HEIGHT = 18;
    public static final int PADDING = 5;
    private static final Identifier BACKGROUND = Identifier.of(IslesExtra.MOD_ID, "textures/gui/shortcut_bg.png");

    private final ItemStack itemStack;
    private final Consumer<AbstractInput> action;

    protected IslesShortcutWidget(
		ItemStack itemStack, int x, int y, net.minecraft.text.Text text, Consumer<AbstractInput> action
	) {
        super(x, y, WIDTH, HEIGHT, text);
        this.itemStack = itemStack;
        this.action = action;
    }


    @Override
    protected void drawIcon(DrawContext context, int mouseX, int mouseY, float delta) {
        context.drawTexture(RenderPipelines.GUI_TEXTURED, BACKGROUND, getX(), getY(), 0, 0, WIDTH, HEIGHT, WIDTH, HEIGHT);

        int itemX = getX() + (WIDTH - 16) / 2;
        int itemY = getY() + (HEIGHT - 16) / 2;
        context.drawItem(itemStack, itemX, itemY);
        if (isHovered()) {
            context.fill(getX(), getY(), getX() + WIDTH, getY() + HEIGHT, 0x40FFFFFF);
            context.drawTooltip(MinecraftClient.getInstance().textRenderer, this.getMessage(), mouseX, mouseY);
        }
    }

    @Override
    public void onPress(AbstractInput input) {
        action.accept(input);
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder narrationMessageBuilder) {
        this.appendDefaultNarrations(narrationMessageBuilder);
    }

    public static IslesShortcutWidget[] getWidgets(int x, int y) {
        List<ShortcutData> shortcuts = IslesConfig.HANDLER.instance().inventoryShortcuts;

        IslesShortcutWidget[] widgets = new IslesShortcutWidget[shortcuts.size()];

        for (int i = 0; i < shortcuts.size(); i++) {
            ShortcutData shortcut = shortcuts.get(i);
            widgets[i] = shortcut.getWidget(x - WIDTH + PADDING, y + HEIGHT + (i * (HEIGHT + PADDING)));
        }

        return widgets;
    }

    public record ShortcutData (
        String name,
        String iconPath,
        String command
    ) {
        IslesShortcutWidget getWidget(int x, int y) {
            ItemStack stack;
            try {
                Identifier id = Identifier.tryParse(iconPath());
                if (id == null) {
                    stack = new ItemStack(Items.BARRIER);
                } else {
                    Item item = Registries.ITEM.get(id);
                    stack = (item == Items.AIR) ? new ItemStack(Items.BARRIER) : new ItemStack(item);
                }
            } catch (Exception e) {
                stack = new ItemStack(Items.BARRIER);
            }

            return new IslesShortcutWidget(stack, x, y, net.minecraft.text.Text.literal(name()), input -> {
                MinecraftClient client = MinecraftClient.getInstance();
                if (client.getNetworkHandler() != null) {
                    client.getNetworkHandler().sendChatCommand(command());
                }
            });
        }
    }
}
