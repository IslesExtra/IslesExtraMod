package net.skyblockisles.islesextra.config.invshortcut;

import dev.isxander.yacl3.api.Option;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.skyblockisles.islesextra.constants.IslesShortcutWidget;

class InvShortcutEditScreen extends Screen {
    private final Screen parent;
    private final Option<IslesShortcutWidget.ShortcutData> option;

    private TextFieldWidget nameField;
    private TextFieldWidget commandField;
    private TextFieldWidget iconField;

    public InvShortcutEditScreen(Screen parent, Option<IslesShortcutWidget.ShortcutData> option) {
        super(Text.literal("Edit Shortcut"));
        this.parent = parent;
        this.option = option;
    }

    @Override
    protected void init() {
        int centerX = (this.width / 2) + 40;
        int startY = this.height / 2 - 60;

        IslesShortcutWidget.ShortcutData currentData = option.pendingValue();

        this.nameField = new TextFieldWidget(this.textRenderer, centerX - 100, startY, 200, 20, Text.literal("Name"));
        this.nameField.setMaxLength(32);
        this.nameField.setText(currentData.name());
        this.addDrawableChild(nameField);

        this.commandField = new TextFieldWidget(this.textRenderer, centerX - 100, startY + 40, 200, 20, Text.literal("Command"));
        this.commandField.setMaxLength(256);
        this.commandField.setText(currentData.command());
        this.addDrawableChild(commandField);

        this.iconField = new TextFieldWidget(this.textRenderer, centerX - 100, startY + 80, 200, 20, Text.literal("Icon Item"));
        this.iconField.setMaxLength(64);
        this.iconField.setText(currentData.iconPath());
        this.addDrawableChild(iconField);

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Done"), (button) -> {
            String newName = nameField.getText();
            String newCommand = commandField.getText();
            String newIcon = iconField.getText();

            option.requestSet(new IslesShortcutWidget.ShortcutData(newName, newIcon, newCommand));
            this.client.setScreen(parent);
        }).dimensions(this.width / 2 - 100, startY + 120, 200, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, this.width, this.height, 0x90000000);

        super.render(context, mouseX, mouseY, delta);
        int fieldLeftEdge = (this.width / 2) + 40 - 100;
        int startY = this.height / 2 - 60;

        drawLabel(context, "Name", fieldLeftEdge, startY);
        drawLabel(context, "Command", fieldLeftEdge, startY + 40);
        drawLabel(context, "Icon Item", fieldLeftEdge, startY + 80);
    }

    private void drawLabel(DrawContext context, String text, int fieldX, int fieldY) {
        Text label = Text.literal(text);
        int textWidth = textRenderer.getWidth(label);
        int x = fieldX - 10 - textWidth;
        int y = fieldY + (20 - 8) / 2;
        context.drawTextWithShadow(textRenderer, label, x, y, 0xFFFFFFFF);
    }

    @Override
    public void close() {
        this.client.setScreen(parent);
    }
}