package com.kyllian.skyblockisles.islesextra.client.commands;

import com.kyllian.skyblockisles.islesextra.IslesExtra;
import com.kyllian.skyblockisles.islesextra.client.ClientCommand;
import com.kyllian.skyblockisles.islesextra.client.ClientUtils;
import com.kyllian.skyblockisles.islesextra.gui.TexturedGui;
import com.kyllian.skyblockisles.islesextra.utility.Dialogue;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public class TestCommand extends ClientCommand {

    // Simply a command to trigger certain code that requires conditions within the game to be met (for example ClientPlayerEntity != null)

    @Override
    public String getCommandName() {
        return "test";
    }

    @Override
    public int run(CommandContext context) {
        IslesExtra.discord.setRichPresence(false, "test", "this was test yepge");

        Dialogue.addDialogue(new Dialogue.DialogueComponent("Well, hello there", "JoshuaS"));
        Dialogue.addDialogue(new Dialogue.DialogueComponent("Go to the mines, or else...", "JoshuaS"));

        ClientUtils.openScreen(new TexturedGui("globaltradesystem"));
        return 0;
    }

    public void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal(this.getCommandName()).executes(this::run));
    }

}
