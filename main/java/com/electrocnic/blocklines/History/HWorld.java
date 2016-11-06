package com.electrocnic.blocklines.History;

import com.sun.istack.internal.NotNull;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * Created by Andreas on 02.11.2016.
 */
public class HWorld {

    /**
     * Wrapper for Block positions with block types. Used to restore the correct blocktype at the correct position.
     */
    class BlockPosType {
        private BlockPos pos=null;
        private IBlockState type=null;

        public BlockPosType(BlockPos pos, IBlockState type) {
            this.pos = pos;
            this.type = type;
        }
    }

    /**
     * Wrapper for a list of Blocks (BlockPosTypes). Used for undo/redo actions.
     */
    class Blocks {
        private List<BlockPosType> blocks = null;

        public Blocks(List<BlockPosType> blocks) {
            this.blocks = blocks;
        }

        public Blocks() {
            this.blocks = new ArrayList<BlockPosType>();
        }

        public void setBlocks(List<BlockPosType> blocks) {
            this.blocks = blocks;
        }

        public void addBlock(BlockPosType block) {
            if(this.blocks==null) this.blocks = new ArrayList<BlockPosType>();
            this.blocks.add(block);
        }

        public List<BlockPos> getPositions() {
            return blocks.stream().map(block -> block.pos).collect(Collectors.toList());
        }
    }

    private World world = null;
    private Stack<Blocks> undoHistory = null;
    private Stack<Blocks> redoHistory = null;

    public HWorld(World world) {
        this.world = world;
        //TODO: load stack from file.
        this.undoHistory = new Stack<>();
        this.redoHistory = new Stack<>();
    }

    public HWorld() {
        this.undoHistory = new Stack<>();
        this.redoHistory = new Stack<>();
        //TODO: load stack from file.
    }

    public void setWorld(World world) {
        this.world = world;
    }

    /**
     * Should not be invoked, as each single block would be stored as a single undo action.
     * @param pos
     * @param newState
     * @param flags
     * @return
     */
    private boolean setBlockState(@NotNull BlockPos pos,
                                 IBlockState newState,
                                 int flags) {
        boolean b = false;

        if(world!=null) {
            //TODO: remember block state of pos.
            //...
            b = world.setBlockState(pos, newState, flags);

        }

        return b;
    }

    /**
     * Should be used rather than setBlockState
     * @param positions
     * @param newState
     * @param flags
     * @return
     */
    public void setBlocks(@NotNull List<BlockPos> positions,
                             IBlockState newState,
                             int flags) {
        if(world!=null) {
            Blocks history = rememberStates(positions);

            undoHistory.push(history);
            redoHistory = new Stack<>();

            for(BlockPos pos : positions) {
                try {
                    world.setBlockState(pos, newState, flags);
                }catch(Exception e) {
                }
            }
        }
    }

    private void setBlocks(@NotNull Blocks blocks, int flags) {
        if(world!=null) {
            for(BlockPosType blockPosType : blocks.blocks) {
                world.setBlockState(blockPosType.pos, blockPosType.type, flags);
            }
        }
    }

    public IBlockState getBlockState(BlockPos blockPos) {
        return world.getBlockState(blockPos);
    }

    /**
     * World must not be null to call this...
     * This generates a new Blocks object by a given BlockPos list.
     * @param positions
     * @return
     */
    private Blocks rememberStates(List<BlockPos> positions) {
        List<BlockPosType> positionsTypes = new ArrayList<>();
        for(BlockPos pos : positions) {
            positionsTypes.add(new BlockPosType(pos, world.getBlockState(pos)));
        }
        return new Blocks(positionsTypes);
    }

    public void undo() {
        undoRedo(true);
    }

    public void redo() {
        undoRedo(false);
    }

    private void undoRedo(boolean undo) {
        if(undo && undoHistory!=null && !undoHistory.isEmpty() || !undo && redoHistory!=null && !redoHistory.isEmpty()) {
            Blocks undoRedoBlocks = (undo?undoHistory:redoHistory).pop();
            if(redoHistory==null) redoHistory = new Stack<>();
            if(undoHistory==null) undoHistory = new Stack<>();
            (undo?redoHistory:undoHistory).push(rememberStates(undoRedoBlocks.getPositions()));
            setBlocks(undoRedoBlocks, 3);
        }
    }
}
