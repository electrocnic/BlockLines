package com.electrocnic.blocklines.Proxy;

import com.electrocnic.blocklines.Events.BlockLinesEventHandler;
import com.electrocnic.blocklines.Events.ICommandEventListener;
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
import net.minecraftforge.fml.common.eventhandler.IEventListener;

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
        if(mirror==null) mirror = new Mirror();
        if(world==null) {
            world = new HWorld(mirror);
        }else {
            world.setMirror(mirror);
        }
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e, ICommandEventListener eventHandler) {
        super.init(e, eventHandler);
        if(mirror==null) mirror = new Mirror();
        eventHandler.setMirror(mirror);
        MinecraftServer minecraftServer = getServer();
        if(!minecraftServer.getEntityWorld().isRemote) {
            if(world==null) {
                world = new HWorld(minecraftServer.getEntityWorld(), mirror);
            }else {
                world.setMirror(mirror);
                world.setWorld(minecraftServer.getEntityWorld());
            }
        }
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        super.postInit(e);
    }

    public static HWorld getWorld() {
        if(world==null) {
            //world = new HWorld(mirror);
            if(mirror==null) {
                mirror = new Mirror();
            }
            world = new HWorld(mirror);
            //should never really happen
            try{
                throw new NullPointerException("Serverproxy failure.");
            }catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        return world;
    }

    public static void setWorld(World world, ICommandEventListener eventHandler) {
        if(!world.isRemote) {
            //if(mirror==null) mirror = new Mirror();
            //if(ServerProxy.world==null) ServerProxy.world = new HWorld(world, mirror);
            if(mirror==null) {
                mirror = new Mirror();
            }
            eventHandler.setMirror(mirror);
            if(ServerProxy.world==null) {
                ServerProxy.world = new HWorld(world, mirror); //should never happen.
            }else {
                ServerProxy.world.setWorld(world);
                ServerProxy.world.setMirror(mirror);
            }
        }
    }

    private MinecraftServer getServer() {
        WorldServer worldServer = DimensionManager.getWorld(0);
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "FakePlayer");
        FakePlayer fakePlayer = new FakePlayer(worldServer, gameProfile);
        MinecraftServer minecraftServer = fakePlayer.mcServer;
        return minecraftServer;
    }

}
