package com.electrocnic.blocklines.EditTools;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

/**
 * Created by Andreas on 19.11.2016.
 */
public interface ISelectable {
    void performSelection(BlockPos pos, EntityPlayer player);
    void resetSelection();
}
