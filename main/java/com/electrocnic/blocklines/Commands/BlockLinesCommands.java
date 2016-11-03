package com.electrocnic.blocklines.Commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andreas on 31.10.2016.
 */
public class BlockLinesCommands extends CommandBase {

    Map<String, Command> commands = null;

    public static final String COMMAND_MODE = "mode";
    public static final String COMMAND_ABORT = "abort";
    public static final String COMMAND_UNDO = "undo";
    public static final String COMMAND_REDO = "redo";
    public static final String COMMAND_QUALITY = "quality";

    public BlockLinesCommands() {
        super();
        commands = new HashMap<String, Command>();
        commands.put(COMMAND_MODE, new ChangeMode());
        commands.put(COMMAND_ABORT, new Abort());
        commands.put(COMMAND_UNDO, new Undo());
        commands.put(COMMAND_REDO, new Redo());
        commands.put(COMMAND_QUALITY, new Quality());
    }

    @Override
    public String getCommandName() {
        return "bl";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "Commands for BlockLines:\n- /bl mode <ModeType> [0,1,2,3,4]- Available ModeTypes: circle, ellipse, line\n" +
                "- /bl abort - Will reset the current selection of blocks for the drawing.\n" +
                "- /bl undo - Will undo the last drawing\n" +
                "- /bl redo - Will redo the last drawing\n" +
                "- /bl quality <circle|ellipse> <value|auto> - Will set the quality of the ellipse or circle.\n" +
                this.addSpaces("") + "Low quality is fast, but can result in leaks. Auto will automatically set the quality.\n" +
                "- /bl mode <circle|ellipse> [0|1|2|3|4|5|m] -\n" +
                this.addSpaces("") + "0: A circle/ellipse will be drawn.\n" +
                this.addSpaces("") + "1: A filled circle/ellipse will be drawn.\n" +
                this.addSpaces("") + "2: Only the part between the selected blocks will be drawn.\n" +
                this.addSpaces("") + "3: Only the part outside the selected blocks will be drawn.\n" +
                this.addSpaces("") + "4: Only the first segment between selection 1 and selection 2 will be drawn.\n" +
                this.addSpaces("") + "5: The circle will be drawn with thick lines.\n" +
                this.addSpaces("") + "m: Turns the placement of the middle of the circle on or off.\n" +
                "";
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

    private String addSpaces(String input) {
        return this.addSpaces(input, 2);
    }

    private String addSpaces(String input, int totalLength) {
        String spaces = "";
        for(int i=0; i<totalLength-input.length(); i++) {
            spaces += " ";
        }
        return input + spaces;
    }
}
