package net.skyblockisles.islesextra.constants;

import java.util.function.Consumer;

import org.joml.Matrix3x2fStack;

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

    private final Identifier icon;
    private final Consumer<AbstractInput> action;

    protected IslesShortcutWidget(
		Identifier icon, int x, int y, net.minecraft.text.Text text, Consumer<AbstractInput> action
	) {
        super(x, y, WIDTH, HEIGHT, text);
        this.icon = icon;
        this.action = action;
    }


    @Override
    protected void drawIcon(DrawContext context, int mouseX, int mouseY, float delta) {
        context.drawTexture(RenderPipelines.GUI_TEXTURED, BACKGROUND, getX(), getY(), 0, 0, WIDTH, HEIGHT, WIDTH, HEIGHT);
        
        float scale = 0.75f;
        float offset = (WIDTH * (1.0f - scale)) / 2.0f;

        Matrix3x2fStack matrices = context.getMatrices().pushMatrix();
        matrices.translate(getX() + offset, getY() + offset, matrices);
        matrices.scale(scale, scale, matrices);

        context.drawTexture(RenderPipelines.GUI_TEXTURED, icon, 0, 0, 0, 0, WIDTH, HEIGHT, WIDTH, HEIGHT);

        matrices.popMatrix();

        if (isHovered()) {
            context.fill(getX(), getY(), getX() + WIDTH, getY() + HEIGHT, 0x40FFFFFF);
        }
    }

    @Override
    public void onPress(AbstractInput input) {
        action.accept(input);
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder narrationMessageBuilder) {

    }

    public static IslesShortcutWidget[] getWidgets(int x, int y) {
        ShortcutData[] shortcuts = {
            new ShortcutData("Open Trash", "textures/item/barrier.png", "trash"),
            new ShortcutData("Open Backpack", "textures/item/brown_bundle.png", "backpack")
        };

        IslesShortcutWidget[] widgets = new IslesShortcutWidget[shortcuts.length];

        for (int i = 0; i < shortcuts.length; i++) {
            ShortcutData shortcut = shortcuts[i]; 
            widgets[i] = shortcut.getWidget(x - WIDTH + PADDING, y + HEIGHT + (i * (HEIGHT + PADDING)));
        }

        return widgets;
    }

    private record ShortcutData (
        String name,
        String iconPath,
        String command
    ) {
        IslesShortcutWidget getWidget(int x, int y) {
            return new IslesShortcutWidget(Identifier.ofVanilla(iconPath()), x, y, net.minecraft.text.Text.literal(name()), input -> {
                MinecraftClient client = MinecraftClient.getInstance();
                if (client.getNetworkHandler() != null) {
                    client.getNetworkHandler().sendChatCommand(command());
                }
            });
        }
    }
}
