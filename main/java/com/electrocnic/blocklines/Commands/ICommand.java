package com.electrocnic.blocklines.Commands;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

/**
 * Created by Andreas on 31.10.2016.
 */
@FunctionalInterface
public interface ICommand {
    public void execute(MinecraftServer server, ICommandSender sender, String[] args);
}
