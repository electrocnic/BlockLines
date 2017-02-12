package com.electrocnic.blocklines.EditTools;

import com.electrocnic.blocklines.Proxy.ServerProxy;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andreas on 31.10.2016.
 */
public class Line extends Tool {

    public static final String IDENTIFIER = "line";

    private List<BlockPos> selection2 = null;

    public Line() {
        super(2);
        selection2 = new ArrayList<BlockPos>();
    }

    @Override
    public void draw(EntityPlayer player, List<BlockPos> selection, IBlockState blockType) {
        if(selection!=null && selection.size()>=2) {
            double x = selection.get(0).getX();
            double y = selection.get(0).getY();
            double z = selection.get(0).getZ();
            double dx = x - selection.get(1).getX();
            double dy = y - selection.get(1).getY();
            double dz = z - selection.get(1).getZ();

            int steps = (int)Math.floor(Math.abs(Math.abs(dx)>Math.abs(dy) && Math.abs(dx)>Math.abs(dz)?dx:((Math.abs(dy)>Math.abs(dx)&&Math.abs(dy)>Math.abs(dz))?dy:dz)));
            double xi = dx / (double) steps;
            double yi = dy / (double) steps;
            double zi = dz / (double) steps;

            List<BlockPos> blocks = new ArrayList<>();

            for(int i=0; i<steps; i++) {
                x-=xi;
                y-=yi;
                z-=zi;
                BlockPos pos = new BlockPos(x, y, z);
                if(overwrite(ServerProxy.getWorld(), pos)) blocks.add(pos);
            }

            ServerProxy.getWorld().setBlocks(blocks, ServerProxy.getWorld().getBlockState(selection.get(0)), 3);
        }
    }

    @Override
    public void performSelection(BlockPos pos, EntityPlayer player) {
        if(!secondRow) {
            selection.add(pos);
        }else if(secondRow) {
            selection2.add(pos);
        }
        player.sendMessage(new TextComponentString("Block has been added to your " + (inARow ?(secondRow ?"second ":"first "):"") + "selection. Selected: " + (secondRow ?selection2:selection).size()));
        if((!inARow && selection.size() == getSelectionCount()) || (inARow && secondRow && selection2.size() >= selection.size())) {
            if(!inARow) {
                draw(player, selection, ServerProxy.getWorld().getBlockState(selection.get(0)));
            } else {
                for(int i=0; i<selection.size(); i++) {
                    List<BlockPos> temp = new ArrayList<>();
                    temp.add(selection.get(i));
                    temp.add(selection2.get(i));
                    draw(player, temp, ServerProxy.getWorld().getBlockState(temp.get(0)));
                }
            }
            resetSelection();
        }else if(!inARow && selection.size()>getSelectionCount()) {
            selection = new ArrayList<BlockPos>();
        }
    }

    @Override
    public void resetSelection() {
        super.resetSelection();
        selection2 = new ArrayList<BlockPos>();
        secondRow = false;
    }

    @Override
    public String toString() {
        return "/bl mode line [row|next] - For drawing more than one lines at the same time.\n" +
                "  row: starts the \"In a row\" Mode: Several lines can be drawn with low effort.\n" +
                "  next: starts the second selection sequence, if \"In a row\" is active.";
    }

    /**
     * There is no submode for line. This method will return 0.
     * @return 0
     */
    @Override
    public int setSubMode(int mode) {
        return 0;
    }

    /**
     * There is no submode for line. This method will return 0.
     * @return 0
     */
    @Override
    public int getSubMode() {
        return 0;
    }

}
