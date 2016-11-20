package com.electrocnic.blocklines.EditTools;

import com.electrocnic.blocklines.Commands.IFlag;
import com.electrocnic.blocklines.History.HWorld;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

/**
 * Created by Andreas on 05.11.2016.
 */
public abstract class Tool implements IFlag, ISelectable {
    protected boolean airOnly = false;
    protected boolean thick = false;
    protected boolean mid = false;
    protected boolean fill = false;
    protected boolean inARow = false;
    protected boolean secondRow = false;

    private int selectionCount = 0;

    public Tool(int selectionCount) {
        this.selectionCount = selectionCount;
    }

    @Override
    public void setAirOnly(boolean airOnly) {
        this.airOnly = airOnly;
    }

    @Override
    public boolean isAirOnly() {
        return this.airOnly;
    }

    @Override
    public boolean isThick() {
        return thick;
    }

    @Override
    public void setThick(boolean thick) {
        this.thick = thick;
    }

    @Override
    public boolean isMid() {
        return mid;
    }

    @Override
    public void setMid(boolean mid) {
        this.mid = mid;
    }

    @Override
    public boolean isFill() {
        return fill;
    }

    @Override
    public void setFill(boolean fill) {
        this.fill = fill;
    }

    @Override
    public void setInARow(boolean inARow) {
        this.inARow = inARow;
    }

    @Override
    public boolean isInARow() {
        return this.inARow;
    }

    @Override
    public void setSecondRow(boolean secondRow) {
        this.secondRow = secondRow;
    }

    @Override
    public boolean isSecondRow() {
        return this.secondRow;
    }

    protected int getSelectionCount() {
        return selectionCount;
    }

    protected boolean overwrite(HWorld world, BlockPos pos) {
        return !airOnly || world.getBlockState(pos) == Blocks.AIR.getDefaultState();
    }
}
