package com.kyllian.islesextra.islesextra.client.commands;

import com.kyllian.islesextra.islesextra.client.ClientCommand;
import com.kyllian.islesextra.islesextra.client.ClientUtils;
import com.kyllian.islesextra.islesextra.client.IslesExtraClient;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;

public class TestCommand extends ClientCommand {

    @Override
    public String getCommandName() {
        return "test";
    }

    @Override
    public int run(CommandContext context) {
        ClientUtils.openScreen(IslesExtraClient.map);
        return 0;
    }


}
