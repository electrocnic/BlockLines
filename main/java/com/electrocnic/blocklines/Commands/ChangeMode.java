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
                    sender.addChatMessage(new TextComponentString("Turned " + (Circle.getMid()?("on"):("off")) + " placement of mid."));
                }else {

                    int submode = 0;
                    try {
                        submode = Integer.parseInt(args[2]);
                    } catch (NumberFormatException e) {
                        sender.addChatMessage(new TextComponentString("Mode for circle has to be 0, 1, 2, 3 or 4."));
                    }
                    Circle.setMode(submode);
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
                int submode = 0;
                try{
                    submode = Integer.parseInt(args[2]);
                }catch (NumberFormatException e) {
                    sender.addChatMessage(new TextComponentString("Mode for ellipse has to be 0, 1, 2 or 3."));
                }
                Ellipse.setMode(submode);
                sender.addChatMessage(new TextComponentString("Mode for ellipse set to " + submode));
            }
        }
    }
}
