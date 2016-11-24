package com.electrocnic.blocklines.Commands;

import com.electrocnic.blocklines.Proxy.ServerProxy;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

/**
 * Created by Andreas on 31.10.2016.
 */
public class Redo implements ICommand {

    public Redo() {

    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        ServerProxy.getWorld().redo();
    }

    @Override
    public String toString() {
        return "- /bl redo - Will redo the last drawing.";
    }
}
