package net.skyblockisles.islesextra.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.ColorControllerBuilder;
import dev.isxander.yacl3.api.controller.DoubleSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.text.Text;

import java.awt.*;

public class ModmenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parentScreen -> YetAnotherConfigLib.createBuilder()
                .title(Text.translatable("text.islesextra.config"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.translatable("text.islesextra.config.category.helpers"))
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
                                                .formatValue(value -> Text.literal(value*100 + "%")))
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
