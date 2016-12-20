package com.electrocnic.blocklines.Container;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

/**
 * Created by Andreas on 20.12.2016.
 */
public interface IDetailedBlockPos {

    BlockPos getPos();

    IBlockState getState();
}
