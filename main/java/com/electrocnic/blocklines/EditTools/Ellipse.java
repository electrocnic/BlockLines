package com.electrocnic.blocklines.EditTools;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

import java.util.List;

/**
 * Created by Andreas on 31.10.2016.
 */
public class Ellipse extends Tool implements Drawable, Qualifyable {

    public static final String IDENTIFIER = "ellipse";

    private int mode = 0;
    private int quality = Qualifyable.DEFAULT_QUALITY;
    private boolean autoQuality = true;

    public Ellipse() {
        quality = Qualifyable.DEFAULT_QUALITY;
        autoQuality = true;
    }

    @Override
    public void draw(EntityPlayer player, List<BlockPos> selection, IBlockState blockType) {

    }

    @Override
    public int getSelectionCount() {
        return 3; //TODO: change if only 2 are needed.
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
    public int setMode(int mode) {
        if( mode>=0 && mode<=3 ) {
            this.mode = mode;
        }
        return this.mode;
    }

    @Override
    public int getMode() {
        return mode;
    }
}
