package com.kyllian.islesextra.islesextra.client.commands;

import com.kyllian.islesextra.islesextra.client.ClientCommand;
import com.kyllian.islesextra.islesextra.client.ClientUtils;
import com.kyllian.islesextra.islesextra.client.IslesExtraClient;
import com.kyllian.islesextra.islesextra.gui.TestTexturedGui;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public class MapCommand extends ClientCommand {

    @Override
    public String getCommandName() {
        return "map";
    }

    @Override
    public int run(CommandContext context) {
        ClientUtils.openScreen(IslesExtraClient.map);
        return 0;
    }

    public void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal(this.getCommandName()).executes(this::run));
    }

}
