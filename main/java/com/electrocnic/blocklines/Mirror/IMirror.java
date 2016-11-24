package com.electrocnic.blocklines.Mirror;

import com.sun.istack.internal.NotNull;
import net.minecraft.util.math.BlockPos;

import java.util.List;

/**
 * Created by Andreas on 24.11.2016.
 */
public interface IMirror {
    /**
     * Takes a list of block positions as input and adds the mirrored blocks to this list depending on the current
     * mirror settings. Then returns the extended list. Might return the same list if mirror is not active.
     * @param toBeMirrored A list of blocks which are to be mirrored.
     * @return The new list, at least with the elements of the input list. Never null.
     */
    @NotNull List<BlockPos> mirror(@NotNull List<BlockPos> toBeMirrored);

    /**
     * Sets the mirror-axis.
     * This defines the amount of mirrored objects:
     * If the axis builds a wall, the objects will be mirrored "by plane", which means, just one copy will be created.
     * If the axis builds a line, the objects will be mirrored around this line, which means, 3 copies will be created.
     * If the axis is just one block, the objects will be mirrored around that block, which means, 7 copies will be created.
     * @param a First point of wall, line, or point.
     * @param b Second point of wall or line, would be equal to first point for point.
     */
    void setAxis(BlockPos a, BlockPos b);

    /**
     * Sets the horizontal mirror to true or false, which means: Will objects be mirrored itself, or just copied with the
     * same orientation as the original? True: objects will be mirrored itself as well. False: objects will just be
     * duplicated.
     * @param horizontalMirror False: Objects will not be mirrored horizontally.
     */
    void setHorizontalMirror(boolean horizontalMirror);

    /**
     * Useful for avoiding upside-down mirrors.
     * @param verticalMirror False: objects will not be mirrored vertically.
     */
    void setVerticalMirror(boolean verticalMirror);

    /**
     * Activates or deactivates the mirror. If deactivated, the mirror() will always return the same list.
     * SetAxis should activate the mirror.
     * @param active True: mirror will return extended list depending on axis, false: Mirror will return the input list.
     */
    void activateMirror(boolean active);
}
