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
    private static List<BlockPos> selection2 = null;

    private Map<Mode, Drawable> generator = null;

    private static Mode currentMode = Mode.Line;
    private static boolean inARaw = false;
    private static int inARawCount = 0;
    private static boolean secondRaw = false;

    private static final Line line = new Line();
    private static final Ellipse ellipse = new Ellipse();
    private static final Circle circle = new Circle();

    public BlockLinesEventHandler() {
        deStutter = 0;
        selection = new ArrayList<BlockPos>();
        selection2 = new ArrayList<BlockPos>();
        generator = new HashMap<Mode, Drawable>();
        generator.put(Mode.Line, line);
        generator.put(Mode.Ellipse, ellipse);
        generator.put(Mode.Circle, circle);
        inARawCount=0;
        secondRaw = false;
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
                    if(!secondRaw) {
                        selection.add(pos);
                    }else if(secondRaw) {
                        selection2.add(pos);
                    }
                    message = new TextComponentString("Block has been added to your " + (inARaw?(secondRaw?"second ":"first "):"") + "selection. Selected: " + (secondRaw?selection2:selection).size());
                    event.getEntityPlayer().addChatMessage(message);
                    if((!(inARaw&&currentMode==Mode.Line) && selection.size() == generator.get(currentMode).getSelectionCount())
                            || ((inARaw&&currentMode==Mode.Line) && secondRaw && selection2.size() >= selection.size())) {
                        if(!(inARaw&&currentMode==Mode.Line)) generator.get(currentMode).draw(event.getEntityPlayer(), selection, ServerProxy.getWorld().getBlockState(selection.get(0)));
                        else {
                            for(int i=0; i<selection.size(); i++) {
                                List<BlockPos> temp = new ArrayList<>();
                                temp.add(selection.get(i));
                                temp.add(selection2.get(i));
                                generator.get(currentMode).draw(event.getEntityPlayer(), temp, ServerProxy.getWorld().getBlockState(temp.get(0)));
                            }
                        }
                        //Circle circle = new Circle(selection, event.getEntityPlayer());
                        selection = new ArrayList<BlockPos>();
                        selection2 = new ArrayList<BlockPos>();
                        secondRaw = false;
                        inARawCount = 0;
                        //circle.draw(event.getEntityPlayer());
                    }else if(!inARaw&&selection.size()>generator.get(currentMode).getSelectionCount()) {
                        selection = new ArrayList<BlockPos>();
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

    public static void setInARaw(boolean inARaw) {
        BlockLinesEventHandler.inARaw = inARaw;
    }

    public static boolean getInARaw() {
        return inARaw;
    }

    public static void setSecondRaw(boolean secondRaw) {
        BlockLinesEventHandler.secondRaw = secondRaw;
    }

    public static boolean getSecondRaw() {
        return secondRaw;
    }

    public static void setMode(Mode mode) {
        BlockLinesEventHandler.currentMode = mode;
    }

    public static Mode getCurrentMode() {
        return currentMode;
    }

    public static void resetSelection() {
        selection = new ArrayList<BlockPos>();
        selection2 = new ArrayList<BlockPos>();
        inARawCount = 0;
        secondRaw = false;
    }
}
