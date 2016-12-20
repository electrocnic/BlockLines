package com.electrocnic.blocklines.Container;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper for Block positions with block types. Used to restore the correct blocktype at the correct position. Also
 * with directions/rotations of the block.
 */
public class DetailedBlockPos implements IDetailedBlockPos {

    IBlockState state = null;
    BlockPos pos = null;

    public DetailedBlockPos(BlockPos pos, IBlockState state) {
        this.state = state;
        this.pos = pos;
    }

    public DetailedBlockPos(IDetailedBlockPos dpos) {
        this.pos = dpos.getPos();
        this.state = dpos.getState();
    }

    /**
     * Adds the detailed information of a block to the blockPos.
     * After this operation, rotation of block is available, as well as mirror-options and other stuff.
     * @param world The world, needed to fetch the IBlockState
     * @param pos The position of the desired block
     * @return A new IBlockPosWithOrientation object which can be mirrored.
     */
    public static @NotNull
    IDetailedBlockPos convertBlockPos(@NotNull World world, @Nullable BlockPos pos) {
        return new DetailedBlockPos(pos, world.getBlockState(pos));
    }

    /**
     * Performs the convertBlockPos operation for every block in the given list and returns a new list
     * containing IBlockPosWithOrientation objects.
     * @param world The world needed to fetch the IBlockState.
     * @param poses The positions of the blocks.
     * @return A new IBlockPosWithOrientation list.
     */
    public static @NotNull List<IDetailedBlockPos> convertBlocks(@NotNull World world, @Nullable List<BlockPos> poses) {
        List<IDetailedBlockPos> detailedBlocks = new ArrayList<IDetailedBlockPos>();

        for(BlockPos pos : poses) {
            detailedBlocks.add(convertBlockPos(world, pos));
        }

        return detailedBlocks;
    }

    @Override
    public BlockPos getPos() {
        return this.pos;
    }

    @Override
    public IBlockState getState() {
        return this.state;
    }
}
