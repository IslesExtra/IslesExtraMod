package net.skyblockisles.islesextra.config.invshortcut;

import dev.isxander.yacl3.api.Controller;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.AbstractWidget;
import dev.isxander.yacl3.gui.YACLScreen;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.skyblockisles.islesextra.constants.IslesShortcutWidget;

public record InvShortcutController(Option<IslesShortcutWidget.ShortcutData> option) implements Controller<IslesShortcutWidget.ShortcutData> {
    @Override
    public Text formatValue() {
        IslesShortcutWidget.ShortcutData value = option.pendingValue();

        return Text.literal("")
                .append(Text.literal(value.name()).formatted(Formatting.GOLD, Formatting.BOLD))
                .append(Text.literal("  /").formatted(Formatting.DARK_GRAY))
                .append(Text.literal(value.command()).formatted(Formatting.AQUA));
    }

    @Override
    public AbstractWidget provideWidget(YACLScreen screen, Dimension<Integer> widgetDimension) {
        return new InvShortcutWidgetElement(this, screen, widgetDimension);
    }
}