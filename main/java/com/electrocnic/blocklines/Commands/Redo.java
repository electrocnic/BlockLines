package com.electrocnic.blocklines.Commands;

import com.electrocnic.blocklines.Proxy.ServerProxy;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

/**
 * Created by Andreas on 31.10.2016.
 */
public class Redo implements Command {

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        ServerProxy.getWorld().redo();
    }
}
