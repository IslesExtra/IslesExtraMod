package net.skyblockisles.islesextra.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.text.Text;

public class ModmenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parentScreen -> YetAnotherConfigLib.createBuilder()
                .title(Text.translatable("text.islesextra.config.name"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.translatable("text.islesextra.config.category.helpers.name"))
                        .tooltip(Text.translatable("text.islesextra.config.category.helpers.tooltip"))
                        .group(OptionGroup.createBuilder()
                                .name(Text.translatable("text.islesextra.config.category.helpers.group.qte.name"))
                                .description(OptionDescription.of(Text.translatable("text.islesextra.config.category.helpers.group.qte.tooltip")))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("text.islesextra.config.category.helpers.group.qte.options.enable.name"))
                                        .description(OptionDescription.of(Text.translatable("text.islesextra.config.category.helpers.group.qte.options.enable.desc")))
                                        .binding(true, () -> IslesConfig.HANDLER.instance().enableQTEHelper, newVal -> IslesConfig.HANDLER.instance().enableQTEHelper = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("text.islesextra.config.category.helpers.group.qte.options.enableTitle.name"))
                                        .description(OptionDescription.of(Text.translatable("text.islesextra.config.category.helpers.group.qte.options.enableTitle.desc")))
                                        .binding(true, () -> IslesConfig.HANDLER.instance().enableQTEHelperTitle, newVal -> IslesConfig.HANDLER.instance().enableQTEHelperTitle = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .build())
                        .build())
                .build()
                .generateScreen(parentScreen);
    }
}
