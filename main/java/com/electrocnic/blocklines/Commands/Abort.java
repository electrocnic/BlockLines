package com.electrocnic.blocklines.Commands;

import com.electrocnic.blocklines.Events.BlockLinesEventHandler;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

/**
 * Created by Andreas on 31.10.2016.
 */
public class Abort implements Command {


    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        BlockLinesEventHandler.resetSelection();
        sender.addChatMessage(new TextComponentString("Selection reset."));
    }
}
