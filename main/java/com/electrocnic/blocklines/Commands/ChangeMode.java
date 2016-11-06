package com.electrocnic.blocklines.Commands;

import com.electrocnic.blocklines.EditTools.Circle;
import com.electrocnic.blocklines.EditTools.Ellipse;
import com.electrocnic.blocklines.EditTools.Line;
import com.electrocnic.blocklines.EditTools.Mode;
import com.electrocnic.blocklines.Events.BlockLinesEventHandler;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedPlayerList;
import net.minecraft.util.text.TextComponentString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Andreas on 31.10.2016.
 */
public class ChangeMode implements Command {

    private Map<String, Command> subcommands = null;

    public ChangeMode() {
        subcommands = new HashMap<String, Command>();
        subcommands.put(Line.IDENTIFIER, new SubCommandLine());
        subcommands.put(Ellipse.IDENTIFIER, new SubCommandEllipse());
        subcommands.put(Circle.IDENTIFIER, new SubCommandCircle());
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        if(args.length <= 1) {
            sender.addChatMessage(new TextComponentString("Mode needs one of these arguments: line, ellipse, circle."));
            return;
        }
        subcommands.get(args[1]).execute(server, sender, args);
    }
}

class SubCommandLine implements Command {
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        BlockLinesEventHandler.setMode(Mode.Line);
        if(args.length >=3 && args[2].equalsIgnoreCase("a")) {
            Line.setAirOnly(!Line.getAirOnly());
            sender.addChatMessage(new TextComponentString("Turned " + (Line.getAirOnly() ? ("off") : ("on")) + " overwrite non-air blocks."));
        }else if(args.length >=3 && args[2].equalsIgnoreCase("row")) {
            BlockLinesEventHandler.setInARaw(!BlockLinesEventHandler.getInARaw());
            sender.addChatMessage(new TextComponentString("Turned " + (BlockLinesEventHandler.getInARaw() ? ("on") : ("off")) + " performing several lines in one take.\n" +
                    "Select the blocks which are the \"first\" blocks. Then:\n" +
                    "Use the command /bl line next - which stands for \"next selection sequence\", then\n" +
                    "when you have the same amount in both selection sequences, the lines will automatically\n" +
                    "be drawn. Remember, that the first selection of the first selection sequence is used\n" +
                    "with the first selection of the second selection sequence to build a line and so on..."));
        }else if(args.length >=3 && args[2].equalsIgnoreCase("next")) {
            BlockLinesEventHandler.setSecondRaw(true);
            sender.addChatMessage(new TextComponentString("Second selection sequence for multiple line creation is now active."));
        }
        sender.addChatMessage(new TextComponentString("Mode set to Line."));
    }
}

class SubCommandCircle implements Command {
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        if(args.length>=2) {
            BlockLinesEventHandler.setMode(Mode.Circle);
            sender.addChatMessage(new TextComponentString("Mode set to Circle."));
            if(args.length>=3) {
                if(args[2].equalsIgnoreCase("m")) {
                    Circle.toggleMid();
                    sender.addChatMessage(new TextComponentString("Turned " + (Circle.getMid() ? ("on") : ("off")) + " placement of mid."));
                }else if(args[2].equalsIgnoreCase("t")) {
                    Circle.setThick(!Circle.getThick());
                    sender.addChatMessage(new TextComponentString("Turned " + (Circle.getThick() ? ("on") : ("off")) + " thick lines."));
                }else if(args[2].equalsIgnoreCase("a")) {
                    Circle.setAirOnly(!Circle.getAirOnly());
                    sender.addChatMessage(new TextComponentString("Turned " + (Circle.getAirOnly() ? ("off") : ("on")) + " overwrite non-air blocks."));
                }else{
                    int submode = 0;
                    try {
                        submode = Integer.parseInt(args[2]);
                    } catch (NumberFormatException e) {
                        sender.addChatMessage(new TextComponentString("Mode for circle has to be 0, 1, 2, 3 or 4; m, t or a"));
                    }
                    submode = Circle.setMode(submode);
                    sender.addChatMessage(new TextComponentString("Mode for circle set to " + submode));
                }
            }
        }
    }
}

class SubCommandEllipse implements Command {
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        if(args.length>=2) {
            BlockLinesEventHandler.setMode(Mode.Ellipse);
            sender.addChatMessage(new TextComponentString("Mode set to Ellipse."));
            if(args.length>=3) {
                if(args[2].equalsIgnoreCase("m")) {
                    //Ellipse.toggleMid();
                    //sender.addChatMessage(new TextComponentString("Turned " + (Ellipse.getMid() ? ("on") : ("off")) + " placement of mid."));
                }else if(args[2].equalsIgnoreCase("t")) {
                }else if(args[2].equalsIgnoreCase("a")) {
                    Ellipse.setAirOnly(!Ellipse.getAirOnly());
                    sender.addChatMessage(new TextComponentString("Turned " + (Ellipse.getAirOnly() ? ("off") : ("on")) + " overwrite non-air blocks."));
                }else{
                    int submode = 0;
                    try {
                        submode = Integer.parseInt(args[2]);
                    } catch (NumberFormatException e) {
                        sender.addChatMessage(new TextComponentString("Mode for ellipse has to be 0, 1, 2, 3 or 4; m or t"));
                    }
                    Ellipse.setMode(submode);
                    sender.addChatMessage(new TextComponentString("Mode for ellipse set to " + submode));
                }
            }
        }
    }
}
