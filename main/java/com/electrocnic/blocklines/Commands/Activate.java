package com.electrocnic.blocklines.Commands;

import com.electrocnic.blocklines.Events.Event;
import com.electrocnic.blocklines.Events.ICommandEventListener;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

/**
 * Created by Andreas on 31.01.2017.
 */
public class Activate implements ICommand {

    private ICommandEventListener eventHandler = null;

    public Activate(ICommandEventListener eventHandler) {
        this.eventHandler = eventHandler;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        String result = eventHandler.onCommandEvent(new Event<String>(Event.ACTIVATE));
        sender.sendMessage(new TextComponentString(result));
    }

    @Override
    public String toString() {
        return "- /bl activate - Will enable the usage of the building-tools (line, circle, cube, etc.). Will not affect the mirror.\n" +
                "Mirror will also duplicate blocks which are generated with these tools however, if the mirror is activated.";
    }
}
