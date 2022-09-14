package com.kyllian.skyblockisles.islesextra.client.commands;

import com.kyllian.skyblockisles.islesextra.client.ClientCommand;
import com.kyllian.skyblockisles.islesextra.client.ClientUtils;
import com.kyllian.skyblockisles.islesextra.client.IslesExtraClient;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public class MapCommand extends ClientCommand {

    //  The temporary command to open the bad map.

    @Override
    public String getCommandName() {
        return "map";
    }

    @Override
    public int run(CommandContext context) {
        //ClientUtils.openScreen(IslesExtraClient.map);
        return 0;
    }

    public void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal(this.getCommandName()).executes(this));
    }

}
