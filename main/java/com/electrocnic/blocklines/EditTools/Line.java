package com.electrocnic.blocklines.EditTools;

import com.electrocnic.blocklines.Commands.IFlag;
import com.electrocnic.blocklines.Proxy.ServerProxy;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andreas on 31.10.2016.
 */
public class Line extends Tool implements Drawable {

    public static final String IDENTIFIER = "line";

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
    public String toString() {
        return "/bl mode line [row|next] - For drawing more than one lines at the same time.\n" +
                "  row: starts the \"In a row\" Mode: Several lines can be drawn with low effort.\n" +
                "  next: starts the second selection sequence, if \"In a row\" is active.";
    }

    @Override
    public int getSelectionCount() {
        return 2;
    }


    @Override
    public int setMode(int mode) {
        return 0;
    }

    @Override
    public int getMode() {
        return 0;
    }
}
