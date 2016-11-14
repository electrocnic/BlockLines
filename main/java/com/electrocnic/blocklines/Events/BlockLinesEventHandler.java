package com.electrocnic.blocklines.Events;

import com.electrocnic.blocklines.EditTools.*;
import com.electrocnic.blocklines.Proxy.ServerProxy;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.GameType;
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
    private List<BlockPos> selection = null; //was static
    private List<BlockPos> selection2 = null; //

    private Map<Mode, Drawable> generator = null;

    private Mode currentMode = Mode.Line; //
    private boolean inARow = false; //
    private int inARowCount = 0; //
    private boolean secondRow = false; //

    private final Line line = new Line(); //
    private final Ellipse ellipse = new Ellipse(); //
    private final Circle circle = new Circle(); //
    private final Cube cube = new Cube(); //

    public BlockLinesEventHandler() {
        deStutter = 0;
        selection = new ArrayList<BlockPos>();
        selection2 = new ArrayList<BlockPos>();
        generator = new HashMap<Mode, Drawable>();
        generator.put(Mode.Line, line);
        generator.put(Mode.Ellipse, ellipse);
        generator.put(Mode.Circle, circle);
        generator.put(Mode.Cube, cube);
        inARowCount =0;
        secondRow = false;
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
                    if(!secondRow) {
                        selection.add(pos);
                    }else if(secondRow) {
                        selection2.add(pos);
                    }
                    message = new TextComponentString("Block has been added to your " + (inARow ?(secondRow ?"second ":"first "):"") + "selection. Selected: " + (secondRow ?selection2:selection).size());
                    event.getEntityPlayer().addChatMessage(message);
                    if((!(inARow &&currentMode==Mode.Line) && selection.size() == generator.get(currentMode).getSelectionCount())
                            || ((inARow &&currentMode==Mode.Line) && secondRow && selection2.size() >= selection.size())) {
                        if(!(inARow &&currentMode==Mode.Line)) generator.get(currentMode).draw(event.getEntityPlayer(), selection, ServerProxy.getWorld().getBlockState(selection.get(0)));
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
                        secondRow = false;
                        inARowCount = 0;
                        //circle.draw(event.getEntityPlayer());
                    }else if(!inARow &&selection.size()>generator.get(currentMode).getSelectionCount()) {
                        selection = new ArrayList<BlockPos>();
                    }
                }

                deStutter++;
            }
        }
    }


    public Line getLine() {
        return line;
    }

    public Ellipse getEllipse() {
        return ellipse;
    }

    public Circle getCircle() {
        return circle;
    }

    public Cube getCube() {
        return cube;
    }

    public void setInARow(boolean inARow) {
        this.inARow = inARow;
    }

    public boolean isInARow() {
        return inARow;
    }

    public void setSecondRow(boolean secondRaw) {
        this.secondRow = secondRaw;
    }

    public boolean getSecondRow() {
        return secondRow;
    }

    public void setMode(Mode mode) {
        this.currentMode = mode;
    }

    public Mode getCurrentMode() {
        return currentMode;
    }

    public void resetSelection() {
        selection = new ArrayList<BlockPos>();
        selection2 = new ArrayList<BlockPos>();
        inARowCount = 0;
        secondRow = false;
    }
}
