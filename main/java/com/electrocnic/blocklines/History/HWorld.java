package com.electrocnic.blocklines.History;

import com.electrocnic.blocklines.Mirror.IMirror;
import com.electrocnic.blocklines.Mirror.Mirror;
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

    private IMirror mirror = null;

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

    public HWorld(World world, IMirror mirror) {
        this.world = world;
        //TODO: load stack from file.
        this.undoHistory = new Stack<>();
        this.redoHistory = new Stack<>();
        this.mirror = mirror;
    }

    /**
     * HISTORY-CONSTRAINT: Need to set the mirror after this constructor!
     */
    public HWorld(IMirror mirror) {
        this.mirror = mirror;
        this.undoHistory = new Stack<>();
        this.redoHistory = new Stack<>();
        //TODO: load stack from file.
    }

    public void setMirror(IMirror mirror) {
        this.mirror = mirror;
    }

    public IMirror getMirror() {
        return this.mirror;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    /**
     * Invokes the mirror. The mirror will add blocks depending on its current settings. Then remembers the blocks'
     * current states in the world for undo functionality. The sets each block in the world.
     * @param positions
     * @param newState
     * @param flags
     * @return
     */
    public void setBlocks(@NotNull List<BlockPos> positions,
                             IBlockState newState,
                             int flags) {
        if(world!=null) {
            positions = mirror.mirror(positions);

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

    /**
     * Just for undo/redo purposes, as a Blocks object also contains the block-type per block. No mirroring will be
     * invoked here.
     * @param blocks
     * @param flags
     */
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
