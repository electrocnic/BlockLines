package com.electrocnic.blocklines.Mirror;

import com.sun.istack.internal.NotNull;
import net.minecraft.util.math.BlockPos;

import java.util.List;

/**
 * Created by Andreas on 24.11.2016.
 */
public class Mirror implements IMirror {

    //axis
    private BlockPos a = null;
    private BlockPos b = null;

    //flags
    private boolean horizontalMirror = true;
    private boolean verticalMirror = false;
    private boolean active = false;

    public Mirror() {
    }

    @Override
    public List<BlockPos> mirror(@NotNull List<BlockPos> toBeMirrored) {
        //TODO


        return toBeMirrored;
    }

    @Override
    public void setAxis(BlockPos a, BlockPos b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public void setHorizontalMirror(boolean horizontalMirror) {
        this.horizontalMirror = horizontalMirror;
    }

    @Override
    public void setVerticalMirror(boolean verticalMirror) {
        this.verticalMirror = verticalMirror;
    }

    @Override
    public void activateMirror(boolean active) {
        this.active = active;
    }
}
