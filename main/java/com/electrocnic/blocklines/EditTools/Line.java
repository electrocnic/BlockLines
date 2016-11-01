package com.electrocnic.blocklines.EditTools;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

import java.util.List;

/**
 * Created by Andreas on 31.10.2016.
 */
public class Line implements Drawable {

    public static final String IDENTIFIER = "line";

    @Override
    public void draw(EntityPlayer player, List<BlockPos> selection, IBlockState blockType) {

    }

    @Override
    public int getSelectionCount() {
        return 2;
    }
}
