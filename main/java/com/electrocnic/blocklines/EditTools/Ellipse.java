package com.electrocnic.blocklines.EditTools;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

import java.util.List;

/**
 * Created by Andreas on 31.10.2016.
 */
public class Ellipse extends Tool implements Qualifyable {

    public static final String IDENTIFIER = "ellipse";

    private int mode = 0;
    private int quality = Qualifyable.DEFAULT_QUALITY;
    private boolean autoQuality = true;

    public Ellipse() {
        super(3); //TODO change selectionCount if more or less.
        quality = Qualifyable.DEFAULT_QUALITY;
        autoQuality = true;
    }

    public void draw(EntityPlayer player, List<BlockPos> selection, IBlockState blockType) {

    }

    @Override
    public void performSelection(BlockPos pos, EntityPlayer player) {
        super.performSelection(pos, player);
    }

    @Override
    public void resetSelection() {
        super.resetSelection();
    }


    @Override
    public void setQuality(int quality) {
        if(quality>=1) this.quality = quality;
    }

    @Override
    public void setQualityAuto(boolean autoset) {
        this.autoQuality = autoset;
    }

    @Override
    public int setSubMode(int mode) {
        if( mode>=0 && mode<=3 ) { //TODO: change to static fields.
            this.mode = mode;
        }
        return this.mode;
    }

    @Override
    public int getSubMode() {
        return mode;
    }

}
