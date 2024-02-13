package com.isles.skyblockisles.islesextra.general.party;

import com.isles.skyblockisles.islesextra.utils.ClientUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

public class PartyManagmentScreen extends Screen {
    public PartyManagmentScreen(Text title) {
        super(title);
    }

    private static final TextWidget textWidget = new TextWidget(
            Text.translatable("text.islesextra.TextWidget"),
            ClientUtils.getClient().textRenderer
    );

    @Override
    protected void init() {

        addDrawableChild(textWidget).setDimensionsAndPosition(50,20,290, 275);

    }

}
