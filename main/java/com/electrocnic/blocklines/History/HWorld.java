package com.electrocnic.blocklines.History;

import com.sun.istack.internal.NotNull;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by Andreas on 02.11.2016.
 */
public class HWorld {
    private World world = null;

    public HWorld(World world) {
        this.world = world;
    }

    public HWorld() {

    }

    public void setWorld(World world) {
        this.world = world;
    }

    public boolean setBlockState(@NotNull BlockPos pos,
                                 IBlockState newState,
                                 int flags) {
        boolean b = false;

        if(world!=null) {
            //TODO: remember block state of pos.
            //...

            b = world.setBlockState(pos, newState, flags);

        }

        return b;
    }

    public boolean setBlocks(@NotNull List<BlockPos> positions,
                             IBlockState newState,
                             int flags) {
        boolean b=false;

        if(world!=null) {
            //TODO: remember block state of positions.
            //...
            //add new List with a hashmap with states or so...

            for(BlockPos pos : positions) {
                world.setBlockState(pos, newState, flags);
            }
        }

        return b;
    }

    public IBlockState getBlockState(BlockPos blockPos) {
        return world.getBlockState(blockPos);
    }
}
