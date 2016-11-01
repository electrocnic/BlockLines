package com.electrocnic.blocklines.EditTools;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

import java.util.List;

/**
 * Created by Andreas on 31.10.2016.
 */
public interface Drawable {
    public void draw(EntityPlayer player, List<BlockPos> selection, IBlockState blockType);

    public int getSelectionCount();
}
