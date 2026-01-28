package net.skyblockisles.islesextra.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.text.Text;

public class ModmenuIntegration implements ModMenuApi {
    private boolean enableQTEHelper;
    private boolean enableQTEHelperTitle;


    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parentScreen -> YetAnotherConfigLib.createBuilder()
                .title(Text.literal("IslesExtra Config"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Helpers"))
                        .tooltip(Text.literal("Enable, disable and configure the various helpers of IslesExtra"))
                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Resource Gathering QTE"))
                                .description(OptionDescription.of(Text.literal("Notifies you and shows you where the Bonus popped up")))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Enable QTE Helper"))
                                        .description(OptionDescription.of(Text.literal("Enable the Helper")))
                                        .binding(true, () -> IslesConfig.HANDLER.instance().enableQTEHelper, newVal -> IslesConfig.HANDLER.instance().enableQTEHelper = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Enable QTE Helper Title"))
                                        .description(OptionDescription.of(Text.literal("Enable the Title that displays when a bonus pops up")))
                                        .binding(true, () -> IslesConfig.HANDLER.instance().enableQTEHelperTitle, newVal -> IslesConfig.HANDLER.instance().enableQTEHelperTitle = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .build())
                        .build())
                .build()
                .generateScreen(parentScreen);
    }
}
