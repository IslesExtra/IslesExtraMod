package net.skyblockisles.islesextra.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.ColorControllerBuilder;
import dev.isxander.yacl3.api.controller.DoubleSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.text.Text;
import net.skyblockisles.islesextra.config.invshortcut.InvShortcutController;
import net.skyblockisles.islesextra.constants.IslesShortcutWidget;

import java.awt.*;
import java.util.List;

public class ModmenuIntegration implements ModMenuApi {
    static final List<IslesShortcutWidget.ShortcutData> shortcuts = List.of(
        new IslesShortcutWidget.ShortcutData("Open Trash", "barrier", "trash"),
        new IslesShortcutWidget.ShortcutData("Open Backpack", "brown_bundle", "backpack")
    );

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        IslesConfig.HANDLER.load();
        return parentScreen -> YetAnotherConfigLib.createBuilder()
                .save(IslesConfig.HANDLER::save)
                .title(Text.translatable("text.islesextra.config"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.translatable("text.islesextra.config.category.helpers"))
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.translatable("text.islesextra.config.category.helpers.options.enableLowInv"))
                                .binding(true, () -> IslesConfig.HANDLER.instance().lowInventoryEnable, newVal -> IslesConfig.HANDLER.instance().lowInventoryEnable = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(ListOption.<IslesShortcutWidget.ShortcutData>createBuilder()
                                .name(Text.translatable("text.islesextra.config.category.helpers.options.inventoryShortcuts"))
                                .binding(shortcuts, () -> IslesConfig.HANDLER.instance().inventoryShortcuts, newVal -> IslesConfig.HANDLER.instance().inventoryShortcuts = newVal)
                                .customController(InvShortcutController::new)
                                .initial(new IslesShortcutWidget.ShortcutData("Name", "Icon Path", "Command (no slash)"))
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(Text.translatable("text.islesextra.config.category.helpers.group.qte"))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("text.islesextra.config.category.helpers.group.qte.options.enable"))
                                        .binding(true, () -> IslesConfig.HANDLER.instance().qteEnable, newVal -> IslesConfig.HANDLER.instance().qteEnable = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("text.islesextra.config.category.helpers.group.qte.options.enableTitle"))
                                        .binding(true, () -> IslesConfig.HANDLER.instance().qteEnableTitle, newVal -> IslesConfig.HANDLER.instance().qteEnableTitle = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(Text.translatable("text.islesextra.config.category.bosstimer"))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("text.islesextra.config.category.bosstimer.enable"))
                                        .binding(true, () -> IslesConfig.HANDLER.instance().enableWorldBossTimer, newVal -> IslesConfig.HANDLER.instance().enableWorldBossTimer = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(Text.translatable("text.islesextra.config.category.quests"))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("text.islesextra.config.category.quests.hide"))
                                        .binding(true, () -> IslesConfig.HANDLER.instance().hideQuestInfo, newVal -> IslesConfig.HANDLER.instance().hideQuestInfo = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .build())
                        .build())
                .category(ConfigCategory.createBuilder()
                        .name(Text.translatable("text.islesextra.config.category.party"))
                        .group(OptionGroup.createBuilder()
                                .name(Text.translatable("text.islesextra.config.category.party.group.lowhealth"))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("text.islesextra.config.category.party.group.lowhealth.enable"))
                                        .binding(true, () -> IslesConfig.HANDLER.instance().partyLowHealthEnable, newVal -> IslesConfig.HANDLER.instance().partyLowHealthEnable = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Double>createBuilder()
                                        .name(Text.translatable("text.islesextra.config.category.party.group.lowhealth.threshold"))
                                        .binding(0.2, () -> IslesConfig.HANDLER.instance().partyLowHealthThreshold, newVal -> IslesConfig.HANDLER.instance().partyLowHealthThreshold = newVal)
                                        .controller(opt -> DoubleSliderControllerBuilder.create(opt)
                                                .range(0.0, 1.0)
                                                .step(0.05)
                                                .formatValue(value -> Text.literal((int) (value*100) + "%")))
                                        .build())
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(Text.translatable("text.islesextra.config.category.party.group.glow"))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("text.islesextra.config.category.party.group.glow.enable"))
                                        .binding(true, () -> IslesConfig.HANDLER.instance().partyGlowEnable, newVal -> IslesConfig.HANDLER.instance().partyGlowEnable = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Color>createBuilder()
                                        .name(Text.translatable("text.islesextra.config.category.party.group.glow.color"))
                                        .binding(Color.BLUE, () -> IslesConfig.HANDLER.instance().partyGlowColor, newVal -> IslesConfig.HANDLER.instance().partyGlowColor = newVal)
                                        .controller(ColorControllerBuilder::create)
                                        .build())
                                .build())
                        .build())
                .build()
                .generateScreen(parentScreen);
    }
}
