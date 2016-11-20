package com.electrocnic.blocklines.Commands;


import com.electrocnic.blocklines.Proxy.ServerProxy;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

/**
 * Created by Andreas on 31.10.2016.
 */
public class Undo implements ICommand {

    public Undo() {

    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        ServerProxy.getWorld().undo();
    }

    @Override
    public String toString() {
        return "- /bl undo - Will undo the last drawing.";
    }
}
