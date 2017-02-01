package com.electrocnic.blocklines.EditTools;

import com.electrocnic.blocklines.Commands.IFlag;
import com.electrocnic.blocklines.History.HWorld;
import com.electrocnic.blocklines.Proxy.ServerProxy;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import java.util.ArrayList;
import java.util.List;

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

    protected List<BlockPos> selection = null;

    public Tool(int selectionCount) {
        selection = new ArrayList<BlockPos>();
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

    public void resetSelection() {
        selection = new ArrayList<BlockPos>();
    }

    @Override
    public void performSelection(BlockPos pos, EntityPlayer player) {
        selection.add(pos);
        player.sendMessage(new TextComponentString("Block has been added to your selection. Selected: " + selection.size()));
        if(selection.size() == getSelectionCount()) {
            draw(player, selection, ServerProxy.getWorld().getBlockState(selection.get(0)));
            resetSelection();
        }else if(selection.size() > getSelectionCount()) { //should never happen, however, possibly due to stuttering of right click...
            resetSelection();
        }
    }

    /**
     * Draws the form in Minecraft. Additional calculations for the form are done here. This method adds blocks to
     * the HWorld, thus to the undo/redo history.
     * @param player The player for message purposes.
     * @param selection The selection of at least two blocks to perform an operation based on this selection.
     * @param blockType The type of the block which will build the form.
     */
    public abstract void draw(EntityPlayer player, List<BlockPos> selection, IBlockState blockType);
}
