package com.kyllian.islesextra.islesextra.client;

import com.kyllian.islesextra.islesextra.client.commands.TestCommand;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;

public class ClientCommand implements Command {

    @Override
    public int run(CommandContext context) {
        return 0;
    }

    public String getCommandName() {
        return null;
    }

    public void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal(this.getCommandName()).executes(this::run));
    }

}
