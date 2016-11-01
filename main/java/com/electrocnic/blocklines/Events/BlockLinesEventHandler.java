package com.electrocnic.blocklines.Events;

import com.electrocnic.blocklines.EditTools.*;
import com.electrocnic.blocklines.Proxy.ServerProxy;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Andreas on 31.10.2016.
 */
public class BlockLinesEventHandler {
    private short deStutter = 0;
    private static List<BlockPos> selection = null;
    private Map<Mode, Drawable> generator = null;

    private static Mode currentMode = Mode.Line;

    private static final Line line = new Line();
    private static final Ellipse ellipse = new Ellipse();
    private static final Circle circle = new Circle();

    public BlockLinesEventHandler() {
        deStutter = 0;
        selection = new ArrayList<BlockPos>();
        generator = new HashMap<Mode, Drawable>();
        generator.put(Mode.Line, line);
        generator.put(Mode.Ellipse, ellipse);
        generator.put(Mode.Circle, circle);
    }

    @SubscribeEvent
    public void onClick(PlayerInteractEvent event) {

        if(event.getHand() == EnumHand.OFF_HAND) {
            if (deStutter >= 1) deStutter = 0;
            else {
                BlockPos pos = event.getPos();
                ITextComponent message = new TextComponentString("Right-Clicked Block: x=" + pos.getX() + ", y=" + pos.getY() + ", z=" + pos.getZ() + ", Side:" + event.getSide().name());
                event.getEntityPlayer().addChatMessage(message);
                //World world = event.getWorld();
                if(ServerProxy.getWorld()==null && event.getSide().isServer()) {
                    ServerProxy.setWorld(event.getWorld());
                    event.getEntityPlayer().setGameType(GameType.CREATIVE);
                }
                if(ServerProxy.getWorld()!=null) {
                    //ServerProxy.getWorld().setBlockToAir(pos);
                    selection.add(pos);
                    message = new TextComponentString("Block has been added to your selection. Selected: " + selection.size());
                    event.getEntityPlayer().addChatMessage(message);
                    if(selection.size() == generator.get(currentMode).getSelectionCount()) {
                        generator.get(currentMode).draw(event.getEntityPlayer(), selection, ServerProxy.getWorld().getBlockState(selection.get(0)));
                        //Circle circle = new Circle(selection, event.getEntityPlayer());
                        selection = new ArrayList<BlockPos>();
                        //circle.draw(event.getEntityPlayer());
                    }
                }

                deStutter++;
            }
        }
    }

    public static Line getLine() {
        return line;
    }

    public static Ellipse getEllipse() {
        return ellipse;
    }

    public static Circle getCircle() {
        return circle;
    }

    public static void setMode(Mode mode) {
        BlockLinesEventHandler.currentMode = mode;
    }

    public static Mode getCurrentMode() {
        return currentMode;
    }

    public static void resetSelection() {
        selection = new ArrayList<BlockPos>();
    }
}
