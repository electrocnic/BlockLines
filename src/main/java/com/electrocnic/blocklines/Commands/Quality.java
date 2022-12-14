package com.electrocnic.blocklines.Commands;

import com.electrocnic.blocklines.EditTools.Circle;
import com.electrocnic.blocklines.EditTools.Ellipse;
import com.electrocnic.blocklines.EditTools.Qualifyable;
import com.electrocnic.blocklines.Events.ICommandEventListener;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andreas on 31.10.2016.
 */
public class Quality implements ICommand {

    private Map<String, Qualifyable> qualifyableMap = null;

    public Quality(ICommandEventListener eventHandler) {
        if(eventHandler!=null) {
            qualifyableMap = new HashMap<String, Qualifyable>();
            qualifyableMap.put(Ellipse.IDENTIFIER, (Ellipse) eventHandler.getTool(Ellipse.IDENTIFIER));
            qualifyableMap.put(Circle.IDENTIFIER, (Circle) eventHandler.getTool(Circle.IDENTIFIER));
        }
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        //arg[0] = "quality". arg[1] = "ellipse|circle". arg[2] = value.
        if(args.length==3) {
            if(args[2].equalsIgnoreCase("auto")) {
                qualifyableMap.get(args[1]).setQualityAuto(true);
                sender.sendMessage(new TextComponentString("Quality set to auto."));
            }
            else {
                int quality = 0;
                try {
                    quality = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(new TextComponentString("Value is not an integer."));
                }
                if (quality > 0) {
                    if (qualifyableMap.containsKey(args[1])) {
                        qualifyableMap.get(args[1]).setQuality(quality);
                        sender.sendMessage(new TextComponentString("Set quality to " + quality + " for " + args[1] + "."));
                    } else
                        sender.sendMessage(new TextComponentString("Only ellipse and circle have a quality which can be set."));
                } else sender.sendMessage(new TextComponentString("Value must be bigger than 0."));
            }
        }else {
            sender.sendMessage(new TextComponentString("Quality needs a value and the target (ellipse or circle)."));
        }
    }

    @Override
    public String toString() {
        return "- /bl quality <circle|ellipse> <value|auto> - Will set the quality of the ellipse or circle." +
                BlockLinesCommands.addSpaces("") + "Low quality is fast, but can result in leaks. Auto will automatically set the quality.";
    }
}
