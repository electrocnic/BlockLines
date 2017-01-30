package com.electrocnic.blocklines.Commands;

import com.electrocnic.blocklines.Commands.Modes.ChangeMode;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andreas on 30.01.2017.
 */
public class HelpCommand implements ICommand{

    private Map<String, Object> helpText = null;

    public HelpCommand() {
        helpText = new HashMap<String, Object>();
        helpText.put(BlockLinesCommands.COMMAND_ABORT, new Abort(null));
        helpText.put(BlockLinesCommands.COMMAND_QUALITY, new Quality(null));
        helpText.put(BlockLinesCommands.COMMAND_MODE, new ChangeMode(null));
        helpText.put(BlockLinesCommands.COMMAND_MIRROR, new MirrorCommand(null));
        helpText.put(BlockLinesCommands.COMMAND_UNDO, new Undo());
        helpText.put(BlockLinesCommands.COMMAND_REDO, new Redo());
    }


    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        if(args==null || args.length<=1) {
            sender.addChatMessage(new TextComponentString("Usage: /bl help command\n" +
                    "Available commands: "
                    + BlockLinesCommands.getAvailableCommands()));
        }else if(helpText.get(args[1]) == null) {
            sender.addChatMessage(new TextComponentString("Command unknown. Available Commands:\n"
                    + BlockLinesCommands.getAvailableCommands()));
        } else {
            sender.addChatMessage(new TextComponentString("Usage:\n" +
                    "\n" +
                    helpText.get(args[1])));
        }
    }
}
