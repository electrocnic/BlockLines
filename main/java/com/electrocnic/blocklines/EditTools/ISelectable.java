package com.electrocnic.blocklines.EditTools;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

/**
 * Created by Andreas on 19.11.2016.
 */
public interface ISelectable {
    /**
     * Performs the selection for the desired active tool and will invoke draw() if the selection has been finished.
     * @param pos The block position of the selected block.
     * @param player The player instance for messages.
     */
    void performSelection(BlockPos pos, EntityPlayer player);

    /**
     * Resets the selection lists.
     */
    void resetSelection();
}
