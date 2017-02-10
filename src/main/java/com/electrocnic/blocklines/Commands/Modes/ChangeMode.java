package com.electrocnic.blocklines.Commands.Modes;

import com.electrocnic.blocklines.Commands.BlockLinesCommands;
import com.electrocnic.blocklines.Commands.ICommand;
import com.electrocnic.blocklines.EditTools.*;
import com.electrocnic.blocklines.Events.BlockLinesEventHandler;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import com.electrocnic.blocklines.Commands.Modes.Implementations.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Andreas on 31.10.2016.
 */
public class ChangeMode implements ICommand {

    private ISubCommands subcommands = null;

    public ChangeMode(ISubCommands subcommands) {
        this.subcommands = subcommands;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        if(args.length <= 1) {
            sender.sendMessage(new TextComponentString("Mode needs one of these arguments: line, ellipse, circle, cube."));
            return;
        }
        ICommand command = subcommands.get(args[1]);
        if(command != null) {
            command.execute(server,sender,args);
        }else {
            sender.sendMessage(new TextComponentString("Command with given argument does not exist."));
        }
    }

    @Override
    public String toString() {
        return "/bl mode <ModeType> [0|1|2|3|t|m|a|f]\n- Available ModeTypes: circle, ellipse, line, cube\n" +
                //"/bl mode " + subcommands.entrySet().stream().map(Map.Entry::toString).collect(Collectors.joining(System.lineSeparator()+"/bl mode ")) +
                //"\n- /bl mode <circle|ellipse> [0|1|2|3|t|m|a|f] -\n" +
                BlockLinesCommands.addSpaces("") + "0: A circle/ellipse will be drawn.\n" +
                BlockLinesCommands.addSpaces("") + "1: Only the part between the selected blocks will be drawn.\n" +
                BlockLinesCommands.addSpaces("") + "2: Only the part outside the selected blocks will be drawn.\n" +
                BlockLinesCommands.addSpaces("") + "3: Only the first segment between selection 1 and selection 2 will be drawn.\n" +
                BlockLinesCommands.addSpaces("") + "a: Turns the placement of blocks to overwrite non-air blocks on or off.\n" +
                BlockLinesCommands.addSpaces("") + "m: Turns the placement of the middle of the circle on or off.\n" +
                BlockLinesCommands.addSpaces("") + "t: The circle will be drawn with thick lines.\n" +
                BlockLinesCommands.addSpaces("") + "f: A filled circle/ellipse will be drawn.\n" +
                "- /bl mode line [row|next] - For drawing more than one lines at the same time.\n" +
                BlockLinesCommands.addSpaces("") + "row: starts the \"In a row\" Mode: Several lines can be drawn with low effort.\n" +
                BlockLinesCommands.addSpaces("") + "next: starts the second selection sequence, if \"In a row\" is active.\n" +
                "- /bl mode cube [f] - For drawing cubes or walls. Fill toggles solid cubes on or off (inside empty or not).\n";
    }
}

