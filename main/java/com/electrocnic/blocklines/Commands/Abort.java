package com.electrocnic.blocklines.Commands;

import com.electrocnic.blocklines.Events.Event;
import com.electrocnic.blocklines.Events.ICommandEventListener;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

/**
 * Created by Andreas on 31.10.2016.
 */
public class Abort implements ICommand {

    private ICommandEventListener eventHandler = null;

    public Abort(ICommandEventListener eventHandler) {
        this.eventHandler = eventHandler;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        if(eventHandler!=null) {
            Event<String> event = new Event<>(Event.ABORT);
            eventHandler.onCommandEvent(event);
            sender.addChatMessage(new TextComponentString("Selection reset."));
        }else {
            sender.addChatMessage(new TextComponentString("EventHandler not yet initialized."));
        }
    }

    @Override
    public String toString() {
        return "- /bl abort - Will reset the current selection of blocks for the drawing.";
    }
}
