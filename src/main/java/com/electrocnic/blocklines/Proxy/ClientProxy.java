package com.electrocnic.blocklines.Proxy;

import com.electrocnic.blocklines.Events.BlockLinesEventHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by Andreas on 29.10.2016.
 */
public class ClientProxy extends CommonProxy{
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e, BlockLinesEventHandler eventHandler) {
        super.init(e, eventHandler);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        super.postInit(e);
    }
}
