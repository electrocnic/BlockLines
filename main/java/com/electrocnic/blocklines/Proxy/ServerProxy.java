package com.electrocnic.blocklines.Proxy;

import com.electrocnic.blocklines.Events.BlockLinesEventHandler;
import com.electrocnic.blocklines.History.HWorld;
import com.electrocnic.blocklines.Mirror.IMirror;
import com.electrocnic.blocklines.Mirror.Mirror;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.UUID;

/**
 * Created by Andreas on 29.10.2016.
 */
public class ServerProxy extends CommonProxy   {

    private static HWorld world = null;
    private static IMirror mirror = null;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
        mirror = new Mirror();
        world = new HWorld(mirror);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e, BlockLinesEventHandler eventHandler) {
        super.init(e, eventHandler);
        WorldServer worldServer = DimensionManager.getWorld(0);
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "FakePlayer");
        FakePlayer fakePlayer = new FakePlayer(worldServer, gameProfile);
        MinecraftServer minecraftServer = fakePlayer.mcServer;
        if(ServerProxy.mirror==null) ServerProxy.mirror = new Mirror();
        if(!minecraftServer.getEntityWorld().isRemote) world = new HWorld(minecraftServer.getEntityWorld(), mirror);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        super.postInit(e);
    }

    public static HWorld getWorld() {
        if(world==null) {
            world = new HWorld(mirror);
        }
        return world;
    }

    public static void setWorld(World world) {
        if(!world.isRemote) {
            if(ServerProxy.mirror==null) ServerProxy.mirror = new Mirror();
            if(ServerProxy.world==null) ServerProxy.world = new HWorld(world, mirror);
            else ServerProxy.world.setWorld(world);
        }
    }

}
