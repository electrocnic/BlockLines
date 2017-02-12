package com.electrocnic.blocklines.Proxy;

import com.electrocnic.blocklines.Events.BlockLinesEventHandler;
import com.electrocnic.blocklines.Events.ICommandEventListener;
import com.electrocnic.blocklines.Mirror.IMirror;
import com.electrocnic.blocklines.Mirror.Mirror;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by Andreas on 29.10.2016.
 */
public class CommonProxy {


    public void preInit(FMLPreInitializationEvent e) {
    }

    public void init(FMLInitializationEvent e, ICommandEventListener eventHandler) {
        MinecraftForge.EVENT_BUS.register(eventHandler);
    }

    public void postInit(FMLPostInitializationEvent e) {

    }


}
