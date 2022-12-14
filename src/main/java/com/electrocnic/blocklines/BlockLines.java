package com.electrocnic.blocklines;

import com.electrocnic.blocklines.Commands.BlockLinesCommands;
import com.electrocnic.blocklines.Events.BlockLinesEventHandler;
import com.electrocnic.blocklines.Proxy.CommonProxy;
import com.electrocnic.blocklines.Proxy.ServerProxy;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

/**
 * Created by Andreas on 29.10.2016.
 *
 * TODO:
 * -Option to deactivate Blocklines itself (and leave mirror on)
 * -Mirror mode: set a mirror plane, using the mid-block and axis. Choose how many axis are active (via commands). Everything placed by BlockLines will be live-mirrored at those axises.
 *    (If possible: everything placed by hand would also be mirrored, and also be added to undo/redo history)
 *    Combined with copy-paste: copy paste would also be mirrored.
 * -Save history to file for persistence
 * -Ellipse
 * -Copy-Paste functions...
 * -ICommand input allows drawings (input coordinates and mode)
 * -Read script files for fast drawing big objects
 * -Save Generated stuff as templates
 * -Load Templates
 * -Allow copy-pasta... (Or look if world edit can do stuff like that... and look if it can duplicate stuff in a direction)
 * -mirror existing buildings in the world?
 *
 *
 *
 */
@Mod(modid = BlockLines.MODID, name = BlockLines.MODNAME, version = BlockLines.VERSION)
public class BlockLines {
    public static final String MODID = "blocklines";
    public static final String MODNAME = "BlockLines";
    public static final String VERSION = "1.0.00";

    @SidedProxy(clientSide="com.electrocnic.blocklines.Proxy.ClientProxy", serverSide="com.electrocnic.blocklines.Proxy.ServerProxy")
    public static CommonProxy proxy;

    private BlockLinesEventHandler eventHandler = null;

    @Mod.Instance
    public static BlockLines instance = new BlockLines();


    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        proxy.preInit(e);
        eventHandler = BlockLinesEventHandler.create();
        System.out.println("Called method: [preInit]");
    }

    @EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.init(e, eventHandler);
        System.out.println("Called method: [init]");
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
        System.out.println("Called method: [postInit]");
    }

    @EventHandler
    public void serverLoad(FMLServerStartingEvent event)
    {
        // register server commands
        ServerProxy.setWorld(event.getServer().getEntityWorld(), eventHandler);
        event.registerServerCommand(BlockLinesCommands.init(eventHandler));
    }
}
