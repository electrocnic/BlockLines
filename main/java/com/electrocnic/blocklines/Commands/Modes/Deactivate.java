package com.electrocnic.blocklines.Commands.Modes;

import com.electrocnic.blocklines.Commands.ICommand;
import com.electrocnic.blocklines.Events.Event;
import com.electrocnic.blocklines.Events.ICommandEventListener;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

/**
 * Created by Andreas on 31.01.2017.
 */
public class Deactivate implements ICommand {

    private ICommandEventListener eventHandler = null;

    public Deactivate(ICommandEventListener eventHandler) {
        this.eventHandler = eventHandler;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        String result = eventHandler.onCommandEvent(new Event<String>(Event.DEACTIVATE));
        sender.sendMessage(new TextComponentString(result));
    }

    @Override
    public String toString() {
        return "- /bl deactivate - Will deactivate the BlockLines' tools (line, circle, cube, etc.), but will not deactivate the mirror!";
    }
}
