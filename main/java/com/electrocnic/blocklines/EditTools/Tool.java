package com.electrocnic.blocklines.EditTools;

import com.electrocnic.blocklines.History.HWorld;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

/**
 * Created by Andreas on 05.11.2016.
 */
public abstract class Tool {
    protected static boolean airOnly = false;

    public static void setAirOnly(boolean airOnly) {
        Tool.airOnly = airOnly;
    }

    public static boolean getAirOnly() {
        return Tool.airOnly;
    }

    protected boolean overwrite(HWorld world, BlockPos pos) {
        return !airOnly || world.getBlockState(pos) == Blocks.AIR.getDefaultState();
    }
}
