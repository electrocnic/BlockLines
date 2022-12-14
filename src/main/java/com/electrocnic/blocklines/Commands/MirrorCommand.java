package com.electrocnic.blocklines.Commands;

import com.electrocnic.blocklines.Events.Event;
import com.electrocnic.blocklines.Events.ICommandEventListener;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

/**
 * Created by Andreas on 26.01.2017.
 */
public class MirrorCommand implements ICommand {

    private ICommandEventListener eventHandler = null;

    public MirrorCommand(ICommandEventListener eventHandler) {
        this.eventHandler = eventHandler;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        if(eventHandler!=null) {
            Event<String> event = null;
            if(args.length<=1) {
                event = new Event<String>(Event.MIRROR);
            } else if(args.length>=2) {
                event = new Event<String>(Event.MIRROR, args[1]);
            }

            if(event!=null) {
                String result = eventHandler.onCommandEvent(event);
                sender.sendMessage(new TextComponentString(result));
            }else {
                sender.sendMessage(new TextComponentString("Should never happen (execute in MirrorCommand.java)"));
            }
        }else {
            sender.sendMessage(new TextComponentString("EventHandler not yet initialized."));
        }
    }

    @Override
    public String toString() {
        return "- /bl mirror - Will toggle the mirror on or off. \n" +
                "- /bl mirror h - Will toggle horizontal individual mirroring on or off.\n" +
                "- /bl mirror v - Will toggle vertical individual mirroring on or off.\n" +
                "- /bl mirror s - Select new mirror axis, the mirror stays activated.\n" +
                "- /bl mirror autoreset - Toggle on or off, whether you have to set a new axis everytime you toggle the mirror\n" +
                "on or off, or you want to keep the old axis when you activate or deactivate the mirror.\n" +
                "- /bl mirror odd|even - Choose, whether the axis lies directly on the blocks (which means there is a line of blocks\n" +
                "which represent the axis) (=odd), or the axis should be between two blocks (=even).\n" +
                "- /bl mirror autoremove - Choose, whether the mirrored blocks should be automatically destroyed, when you destroy blocks by hand.\n" +
                "When the mirror is on, you will be asked to select a mirror plane, a line or a point, using two blocks:\n" +
                "First, select block a, then select block b via right-clicking blocks. If the blocks form a cube, mirroring is not possible, and thus, deactivated.";
    }
}
