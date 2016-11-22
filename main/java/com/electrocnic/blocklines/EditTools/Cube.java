package com.electrocnic.blocklines.EditTools;

import com.electrocnic.blocklines.Proxy.ServerProxy;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andreas on 07.11.2016.
 */
public class Cube extends Tool  {

    public static final String IDENTIFIER = "cube";

    public Cube() {
        super(2);
    }

    public void draw(EntityPlayer player, List<BlockPos> selection, IBlockState blockType) {
        if(selection!=null &&selection.size()>=2) {
            int stepsX = Math.abs(selection.get(0).getX()-selection.get(1).getX());
            int stepsY = Math.abs(selection.get(0).getY()-selection.get(1).getY());
            int stepsZ = Math.abs(selection.get(0).getZ()-selection.get(1).getZ());

            List<BlockPos> blocks = new ArrayList<>();

            for(int x=0; x<=stepsX; x++) {
                for(int y=0; y<=stepsY; y++) {
                    for(int z=0; z<=stepsZ; z++) {
                        int globalX = Math.max(selection.get(0).getX(),selection.get(1).getX())-x;
                        int globalY = Math.max(selection.get(0).getY(),selection.get(1).getY())-y;
                        int globalZ = Math.max(selection.get(0).getZ(),selection.get(1).getZ())-z;
                        BlockPos block = new BlockPos(globalX, globalY, globalZ);
                        if(fillCubeAt(globalX, globalY, globalZ, selection) && overwrite(ServerProxy.getWorld(), block)) {
                            blocks.add(block);
                        }
                    }
                }
            }

            ServerProxy.getWorld().setBlocks(blocks, ServerProxy.getWorld().getBlockState(selection.get(0)), 3);
        }
    }

    @Override
    public void performSelection(BlockPos pos, EntityPlayer player) {
        super.performSelection(pos, player);
    }

    @Override
    public void resetSelection() {
        super.resetSelection();
    }

    private boolean fillCubeAt(int x, int y, int z, List<BlockPos> selection) {
        return (x==selection.get(0).getX() || x==selection.get(1).getX()
        || y==selection.get(0).getY() || y==selection.get(1).getY()
        || z == selection.get(0).getZ() || z == selection.get(1).getZ()
        || fill);
    }

    @Override
    public int setSubMode(int mode) {
        return 0;
    }

    @Override
    public int getSubMode() {
        return 0;
    }

}
