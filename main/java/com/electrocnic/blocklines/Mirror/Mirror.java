package com.electrocnic.blocklines.Mirror;

import com.electrocnic.blocklines.Container.DetailedBlockPos;
import com.electrocnic.blocklines.Container.IDetailedBlockPos;
import com.sun.istack.internal.NotNull;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andreas on 24.11.2016.
 */
public class Mirror implements IMirror {

    //axis
    private BlockPos a = null;
    private BlockPos b = null;
    private boolean aChanged = false;

    /**
     * 0 xy
     * 1 yz
     * 2 zx
     */
    private Reflection activePlane = Reflection.Cube;

    //flags
    private boolean horizontalMirror = true;
    private boolean verticalMirror = false;
    private boolean active = false;

    public Mirror() {
    }

    @Override
    public List<IDetailedBlockPos> mirror(@NotNull List<IDetailedBlockPos> toBeMirrored) throws UnsupportedOperationException {

        if(active) {
            if(a!=null && b!=null) {
                switch (reflectedBy()) {
                    case Cube: return toBeMirrored;
                    case Point: return pointReflection(toBeMirrored);
                    case YLine: return lineReflection(toBeMirrored); //TODO: maybe more parameters for direction.
                    case ZLine: return lineReflection(toBeMirrored);
                    case XLine: return lineReflection(toBeMirrored);
                    case ZYPlane: return planeReflection(toBeMirrored);
                    case XYPlane: return planeReflection(toBeMirrored);
                    case XZPlane: return planeReflection(toBeMirrored);
                    default: throw new UnsupportedOperationException("Mirror operation not supported: " + reflectedBy());
                }
            }
        }

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

    @Override
    public boolean toggleMirror() {
        this.active = !this.active;
        return this.active;
    }

    @Override
    public boolean toggleVerticalMirror() {
        this.verticalMirror = !this.verticalMirror;
        return this.verticalMirror;
    }

    @Override
    public boolean toggleHorizontalMirror() {
        this.horizontalMirror = !this.horizontalMirror;
        return this.horizontalMirror;
    }

    @Override
    public boolean performSelection(BlockPos pos) {
        if(!aChanged) {
            this.a = pos;
            aChanged = true;
            return false;
        }else {
            this.b = pos;
            aChanged = false;
            return true;
        }
    }

    @Override
    public boolean isInvalid() {
        return reflectedBy() == Reflection.Cube;
    }

    @Override
    public String getAxisName() {
        String name = "";
        Reflection axis = reflectedBy();
        if(axis == Reflection.XYPlane || axis == Reflection.XZPlane || axis == Reflection.ZYPlane) name = "Plane (1 copy)";
        else if(axis == Reflection.YLine || axis == Reflection.XLine || axis == Reflection.ZLine) name = "Line (3 copies)";
        else if(axis == Reflection.Point) name = "Point (7 copies)";
        else name = "Cube (error)";
        return name;
    }


    /**
     * Y is the minecraft-Y, namely the normal Z. xz means xy and vice versa.
     */
    enum Reflection {
        XZPlane, //y(a) == y(b)
        XYPlane, //z(a) == z(b)
        ZYPlane, //x(a) == x(b)
        XLine, //z(a) == z(b) && y(a) == y(b)
        ZLine, //x(a) == x(b) && y(a) == y(b)
        YLine, //x(a) == x(b) && z(a) == z(b)
        Point, //x(a) == x(b) && y(a) == y(b) && z(a) == z(b)
        Cube; //x(a) != x(b) && y(a) != y(b) && z(a) != z(b)

        public String toString() {
            return this.name();
        }
    }

    /**
     * Calculates the axis of the reflection
     * @return An enum-value
     */
    private Reflection reflectedBy() {
        if(a!=null && b!=null) {
            if(a.getX() != b.getX() && a.getY() != b.getY() && a.getZ() != b.getZ()) return Reflection.Cube;
            if(a.getX() == b.getX() && a.getY() == b.getY() && a.getZ() == b.getZ()) return Reflection.Point;
            if(a.getZ() == b.getZ() && a.getY() == b.getY()) return Reflection.XLine;
            if(a.getX() == b.getX() && a.getY() == b.getY()) return Reflection.ZLine;
            if(a.getX() == b.getX() && a.getZ() == b.getZ()) return Reflection.YLine;
            if(a.getY() == b.getY()) return Reflection.XZPlane;
            if(a.getZ() == b.getZ()) return Reflection.XYPlane;
            if(a.getX() == b.getX()) return Reflection.ZYPlane;
        }
        return Reflection.Cube;
    }

    /**
     * Mirrors the given blocks by the reflection model [a,b]
     * @param toBeMirrored The blocks (with details about rotation) which are mirrored.
     * @return The mirrored blocks including the given initial blocks.
     */
    private List<IDetailedBlockPos> pointReflection(List<IDetailedBlockPos> toBeMirrored) {
        List<IDetailedBlockPos> mirrored = new ArrayList<IDetailedBlockPos>();
        //call planeReflection for each axis (x,y,z) once.

        //begin with xy Plane.
        activePlane = Reflection.XYPlane;
        // . -> .  .
        mirrored = planeReflection(toBeMirrored);

        //carry on with yz Plane.
        activePlane = Reflection.ZYPlane;
        // .   .  ->  .   .
        //            .   .
        mirrored = planeReflection(mirrored);

        //carry on with zx Plane.
        activePlane = Reflection.XZPlane;
        mirrored = planeReflection(mirrored);

        return mirrored;
    }

    /**
     * Mirrors the given blocks by the reflection model [a,b] three times. (Two crossed planes)
     * @param toBeMirrored The blocks (with details about rotation) which are mirrored.
     * @return The mirrored blocks including the given initial blocks.
     */
    private List<IDetailedBlockPos> lineReflection(List<IDetailedBlockPos> toBeMirrored) throws UnsupportedOperationException{
        List<IDetailedBlockPos> mirrored = new ArrayList<IDetailedBlockPos>();
        //Call plane mirror two times. this mirror is two planes crossed in the a-b direction.

        //both planes have the axis.
        switch (reflectedBy()) {
            case XLine:
                activePlane = Reflection.XYPlane; //xy
                mirrored = planeReflection(toBeMirrored);
                activePlane = Reflection.XZPlane; //zx
                mirrored = planeReflection(mirrored);
                break;
            case YLine:
                activePlane = Reflection.XYPlane; //xy
                mirrored = planeReflection(toBeMirrored);
                activePlane = Reflection.ZYPlane; //yz
                mirrored = planeReflection(mirrored);
                break;
            case ZLine:
                activePlane = Reflection.ZYPlane; //yz
                mirrored = planeReflection(toBeMirrored);
                activePlane = Reflection.XZPlane; //zx
                mirrored = planeReflection(mirrored);
                break;
            default:
                throw new UnsupportedOperationException("Mirror operation in lineReflection not supported: " + reflectedBy());
        }

        return mirrored;
    }

    /**
     * Mirrors the given blocks by the reflection model [a,b] once.
     * This is the only function which cares about the mirror flags.
     * @param toBeMirrored The blocks (with details about rotation) which are mirrored.
     * @return The mirrored blocks including the given initial blocks.
     */
    private List<IDetailedBlockPos> planeReflection(List<IDetailedBlockPos> toBeMirrored) {
        List<IDetailedBlockPos> mirrored = new ArrayList<IDetailedBlockPos>();

        Reflection plane = Reflection.XYPlane;

        //consider the current active plane!
        if( reflectedBy() == Reflection.Point || reflectedBy() == Reflection.XLine || reflectedBy() == Reflection.YLine || reflectedBy() == Reflection.ZLine ) {
            //do it by the active plane.
            plane = activePlane;
        }else {
            //get plane by a-b
            plane = reflectedBy();
        }

        PropertyDirection HORIZONTAL_FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
        PropertyDirection VERTICAL_FACING = PropertyDirection.create("facing", EnumFacing.Plane.VERTICAL);

        List<IDetailedBlockPos> mirroredOnly = new ArrayList<IDetailedBlockPos>();

        //perform the actual mirroring.
        for(IDetailedBlockPos block : toBeMirrored) {
            IBlockState newState = block.getState();
            //block.getState().withRotation();


            //TODO: seems to not work. try with rotation?
            //EnumFacing newFacing = block.getState().getValue(HORIZONTAL_FACING);
            //newFacing = mirrorFacing(newFacing);
            //IBlockState newState = block.getState().withProperty(HORIZONTAL_FACING, newFacing);

            //TODO: if mirrored vertically: flip vertically.

            BlockPos newPos = block.getPos();

            switch (plane) {
                case XYPlane:
                    newPos = new BlockPos(newPos.getX(), newPos.getY(), (2*(a.getZ()-newPos.getZ()))+newPos.getZ());
                    newState = block.getState().withMirror(net.minecraft.util.Mirror.FRONT_BACK);
                    break;
                case ZYPlane:
                    newPos = new BlockPos((2*(a.getX()-newPos.getX())+newPos.getX()), newPos.getY(), newPos.getZ());
                    newState = block.getState().withMirror(net.minecraft.util.Mirror.LEFT_RIGHT);
                    break;
                case XZPlane:
                    newPos = new BlockPos(newPos.getX(), (2*(a.getY()-newPos.getY())+newPos.getY()), newPos.getZ());
                    //newState = block.getState().withMirror(net.minecraft.util.Mirror.);   UP_DOWN???
                    break;
                default:
                    throw new UnsupportedOperationException("Mirror operation in planeReflection not supported: " + plane);
            }

            DetailedBlockPos pos = new DetailedBlockPos(newPos, newState);
            mirroredOnly.add(pos);
        }

        mirrored.addAll(mirroredOnly);
        mirrored.addAll(toBeMirrored);
        return mirrored;
    }

    private EnumFacing mirrorFacing(EnumFacing before) {
        EnumFacing facing = before;

        if (facing == EnumFacing.NORTH ) {
            facing = EnumFacing.SOUTH;
        } else if (facing == EnumFacing.SOUTH ) {
            facing = EnumFacing.NORTH;
        } else if (facing == EnumFacing.WEST ) {
            facing = EnumFacing.EAST;
        } else if (facing == EnumFacing.EAST ) {
            facing = EnumFacing.WEST;
        } else if (facing == EnumFacing.DOWN ) {
            facing = EnumFacing.UP;
        } else if (facing == EnumFacing.UP ) {
            facing = EnumFacing.DOWN;
        }

        return facing;
    }

}
