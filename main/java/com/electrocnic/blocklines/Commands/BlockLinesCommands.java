package com.electrocnic.blocklines.Commands;

import com.electrocnic.blocklines.Commands.Modes.ChangeMode;
import com.electrocnic.blocklines.Commands.Modes.SubcommandWrapper;
import com.electrocnic.blocklines.Events.BlockLinesEventHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.*;
import java.util.stream.Collectors;

/**
 * Created by Andreas on 31.10.2016.
 */
public class BlockLinesCommands extends CommandBase {

    Map<String, ICommand> commands = null;

    public static final String COMMAND_MODE = "mode";
    public static final String COMMAND_ABORT = "abort";
    public static final String COMMAND_UNDO = "undo";
    public static final String COMMAND_REDO = "redo";
    public static final String COMMAND_QUALITY = "quality";

    private BlockLinesEventHandler eventHandler = null;

    public BlockLinesCommands() {
        super();
        commands = new HashMap<String, ICommand>();
    }

    public BlockLinesCommands(BlockLinesEventHandler eventHandler) {
        this();
        this.eventHandler = eventHandler;
    }

    public void addCommand(String name, ICommand command) {
        if(commands==null) commands = new HashMap<String, ICommand>();
        commands.put(name, command);
    }

    @Override
    public String getCommandName() {
        return "bl";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        if(commands==null) commands = new HashMap<String, ICommand>();
        return commands.entrySet().stream().map(Entry::toString).collect(Collectors.joining("\n"));
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length <= 0)
        {
            sender.addChatMessage(new TextComponentString(getCommandUsage(sender)));
            return;
        }
        try {
            commands.get(args[0]).execute(server, sender, args);
        }catch (Exception e) {
            sender.addChatMessage(new TextComponentString("This command does not exist. Maybe you forgot something? Mis-spelled?"));
        }
    }

    public static BlockLinesCommands init(BlockLinesEventHandler eventHandler) {
        BlockLinesCommands commandFactory = new BlockLinesCommands(eventHandler);

        commandFactory.addCommand(COMMAND_MODE, new ChangeMode(SubcommandWrapper.create(eventHandler)));
        commandFactory.addCommand(COMMAND_ABORT, new Abort(eventHandler));
        commandFactory.addCommand(COMMAND_UNDO, new Undo(eventHandler));
        commandFactory.addCommand(COMMAND_REDO, new Redo(eventHandler));
        commandFactory.addCommand(COMMAND_QUALITY, new Quality(eventHandler));

        return commandFactory;
    }



    public static String addSpaces(String input) {
        return addSpaces(input, 2);
    }

    public static String addSpaces(String input, int totalLength) {
        String spaces = "";
        for(int i=0; i<totalLength-input.length(); i++) {
            spaces += " ";
        }
        return input + spaces;
    }

    public void setEventHandler(BlockLinesEventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    public BlockLinesEventHandler getEventHandler() {
        return this.eventHandler;
    }
}
