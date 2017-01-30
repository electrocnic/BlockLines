package com.electrocnic.blocklines.History;

import com.electrocnic.blocklines.Container.DetailedBlockPos;
import com.electrocnic.blocklines.Container.IDetailedBlockPos;
import com.electrocnic.blocklines.Mirror.IMirror;
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
     * Wrapper for a list of Blocks (BlockPosTypes). Used for undo/redo actions.
     */
    class Blocks {
        private List<IDetailedBlockPos> blocks = null;

        public Blocks(List<IDetailedBlockPos> blocks) {
            this.blocks = blocks;
        }

        public Blocks() {
            this.blocks = new ArrayList<IDetailedBlockPos>();
        }

        public void setBlocks(List<IDetailedBlockPos> blocks) {
            this.blocks = blocks;
        }

        public void addBlock(IDetailedBlockPos block) {
            if(this.blocks==null) this.blocks = new ArrayList<IDetailedBlockPos>();
            this.blocks.add(block);
        }

        public List<BlockPos> getPositions() {
            return blocks.stream().map(IDetailedBlockPos::getPos).collect(Collectors.toList());
        }
    }

    /**
     * Removes the oldest element in the history when a certain amount of elements is pushed onto the stack.
     * @param <T>
     */
    class FixedStack<T> extends Stack<T> {
        private int FIXED_SIZE = 100;

        @Override
        public T push(T item) {
            if (this.size() >= FIXED_SIZE) {
                this.removeElementAt(0);
            }
            return super.push(item);
        }
    }

    private World world = null;
    private Stack<Blocks> undoHistory = null;
    private Stack<Blocks> redoHistory = null;

    public HWorld(World world, IMirror mirror) {
        this.world = world;
        //TODO: load stack from file.
        this.undoHistory = new FixedStack<>();
        this.redoHistory = new FixedStack<>();
        this.mirror = mirror;

    }

    public HWorld(World world) {
        this.world = world;
        //TODO: load stack from file.
        this.undoHistory = new FixedStack<>();
        this.redoHistory = new FixedStack<>();
    }

    public HWorld() {
        //TODO: load stack from file.
        this.undoHistory = new FixedStack<>();
        this.redoHistory = new FixedStack<>();
    }

    /**
     * HISTORY-CONSTRAINT: Need to set the mirror after this constructor!
     */
    public HWorld(IMirror mirror) {
        this.mirror = mirror;
        this.undoHistory = new FixedStack<>();
        this.redoHistory = new FixedStack<>();
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

    public void setBlocks(@NotNull List<BlockPos> positions,
                          IBlockState newState,
                          IBlockState oldState,
                          int flags) {
        if(world!=null) {
            List<IDetailedBlockPos> detailedBlocks = DetailedBlockPos.convertBlocks(positions, newState); //get detailed blocks with the given state.
            setBlocks(detailedBlocks, flags, oldState);
        }
    }


    /**
     * Converts the given positions to detailedBlockPos objects with the given state. Then calls setBlocks with these detailedBlockPos objects.
     * @param positions The positions for the placed blocks in the world.
     * @param newState The new state for all of the blocks.
     * @param flags The update-behaviour flag in the world. (Should be 3 most of the times).
     * @return
     */
    public void setBlocks(@NotNull List<BlockPos> positions,
                             IBlockState newState,
                             int flags) {
        if(world!=null) {
            List<IDetailedBlockPos> detailedBlocks = DetailedBlockPos.convertBlocks(positions, newState); //get detailed blocks with the given state.
            setBlocks(detailedBlocks, flags, null);
        }
    }

    /**
     * Mirrors the given detailedBlocks with the current active mirror and remembers the current states of the world's blocks
     * for the given positions. These are pushed to the undo stack. The mirrored blocks are then placed with their state.
     * @param detailedBlocks Blocks with individual states (the states determine the blocks, the rest is the position).
     * @param flags Should be 3 most of the time.
     */
    public void setBlocks(@NotNull List<IDetailedBlockPos> detailedBlocks, int flags, IBlockState oldState) {
        if(world!=null) {
            //1. convert block positions of whatever to detailedBlockPositions (outside this method and take detailedBlocks as argument, so the list can vary in states.)
            //2. mirror them and save in new list. this list contains the to-be-set blocks, not the current blocks in the world.
            //3. remember the current state to each individual detailed block position
            //4. push to undo stack
            //5. set new blocks in the world.
            //This enables the usage for live mirroring user-placed blocks.

            if(mirror!=null && !mirror.isInvalid()) detailedBlocks = mirror.mirror(detailedBlocks);
            else {
                try{
                    throw new NullPointerException("mirror is null");
                }catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
            if(!detailedBlocks.isEmpty()) {

                List<IDetailedBlockPos> detailedUndoBlocks = DetailedBlockPos.convertDetailedBlocks(world, detailedBlocks);//DetailedBlockPos.convertBlocks(world, positions); //get detailed block positions of the current states, NOT the states after placement!!
                //if(mirror!=null) detailedUndoBlocks = mirror.mirror(detailedUndoBlocks);
                //detailedUndoBlocks = DetailedBlockPos.convertDetailedBlocks(world, detailedUndoBlocks); //get detailed block states of current blocks from the world. this time with the mirrored ones.

                Blocks history = rememberStates(detailedUndoBlocks);
                if (oldState != null) history.blocks.get(history.blocks.size() - 1).setState(oldState);
                undoHistory.push(history);
                redoHistory = new FixedStack<>();


                for (IDetailedBlockPos block : detailedBlocks) {
                    try {
                        world.setBlockState(block.getPos(), block.getState(), flags);
                    } catch (Exception e) {
                    }
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
            for(IDetailedBlockPos blockPosType : blocks.blocks) {
                world.setBlockState(blockPosType.getPos(), blockPosType.getState(), flags);
            }
        }
    }

    public IBlockState getBlockState(BlockPos blockPos) {
        return world.getBlockState(blockPos);
    }

    /**
     * World must not be null to call this...
     * This generates a new Blocks object by a given BlockPos list.
     * This will duplicate the data instead of referencing the given argument.
     * @param positions
     * @return
     */
    private Blocks rememberStates(List<IDetailedBlockPos> positions) {
        List<IDetailedBlockPos> positionsTypes = new ArrayList<>();
        for(IDetailedBlockPos pos : positions) {
            positionsTypes.add(new DetailedBlockPos(pos.getPos(), world.getBlockState(pos.getPos())));
        }
        return new Blocks(positionsTypes);
    }

    public String undo() {
        return undoRedo(true);
    }

    public String redo() {
        return undoRedo(false);
    }

    private String undoRedo(boolean undo) {
        if(undo && undoHistory!=null && !undoHistory.isEmpty() || !undo && redoHistory!=null && !redoHistory.isEmpty()) {
            Blocks undoRedoBlocks = (undo?undoHistory:redoHistory).pop();
            if(redoHistory==null) redoHistory = new FixedStack<>();
            if(undoHistory==null) undoHistory = new FixedStack<>();
            (undo?redoHistory:undoHistory).push(rememberStates(undoRedoBlocks.blocks));
            setBlocks(undoRedoBlocks, 3);
        }else{
            return "Nothing to un-/redo.";
        }
        return "";
    }
}
