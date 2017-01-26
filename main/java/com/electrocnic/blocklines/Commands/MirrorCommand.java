package com.electrocnic.blocklines.Commands;

import com.electrocnic.blocklines.Events.ICommandEventListener;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

/**
 * Created by Andreas on 26.01.2017.
 */
public class MirrorCommand implements ICommand {

    public MirrorCommand(ICommandEventListener eventHandler) {


    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        //TODO: somehow combine the functional mirror with the commands and the event listener to be able to select a and b.
        //TODO: test mirror and add h and v functionality.
    }

    @Override
    public String toString() {
        return "mirror: Will toggle the mirror on or off. \nmirror h: Will toggle horizontal individual mirroring on or off.\n" +
                "mirror v: Will toggle vertical individual mirroring on or off.\n" +
                "When the mirror is on, you will be asked to select a mirror plane, a line or a point, using two blocks:\n" +
                "First, select block a, then select block b via right-clicking blocks. If the blocks form a cube, mirroring is not possible, and thus, deactivated.";
    }
}
