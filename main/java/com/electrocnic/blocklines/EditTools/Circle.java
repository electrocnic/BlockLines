package com.electrocnic.blocklines.EditTools;

import com.electrocnic.blocklines.History.HWorld;
import com.electrocnic.blocklines.Proxy.ServerProxy;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import java.util.*;

/**
 * Created by Andreas on 31.10.2016.
 */
public class Circle extends Tool implements Qualifyable {

    public static final String IDENTIFIER = "circle";

    private int mode = 0;
    private int quality = Qualifyable.DEFAULT_QUALITY;
    private Map<Integer, Filter> filter = null;
    private boolean autoQuality = true;

    /** A FULL circle, NOT a filled circle */
    public static final int MODE_FULL = 0;
    //public static final int MODE_FULL_FILL = MODE_FULL+1;
    public static final int MODE_SEGMENT_IN = MODE_FULL+1;
    public static final int MODE_SEGMENT_OUT = MODE_SEGMENT_IN+1;
    public static final int MODE_ONE_SEGMENT = MODE_SEGMENT_OUT+1;
    //public static final int MODE_THICK = MODE_ONE_SEGMENT+1;
    public static final int MODES = MODE_ONE_SEGMENT+1;

    public Circle() {
        super(3);
        quality = Qualifyable.DEFAULT_QUALITY;
        filter = new HashMap<Integer, Filter>();
        filter.put(MODE_FULL, new FilterMode0());
        //filter.put(MODE_FULL_FILL, new FilterMode1());
        filter.put(MODE_SEGMENT_IN, new FilterMode2());
        filter.put(MODE_SEGMENT_OUT, new FilterMode3());
        filter.put(MODE_ONE_SEGMENT, new FilterMode4());
       // filter.put(MODE_THICK, new FilterMode0());
        autoQuality = true;
    }


    public void draw(EntityPlayer player, List<BlockPos> selection, IBlockState blockType) {
        boolean err = false;

        if(selection==null || selection.size()!=3) err = true;

        if(!err) {
            ITextComponent message = new TextComponentString("Selection for Circle: " + selection.size());
            player.sendMessage(message);

            // K(xyz) = M(xyz) + cos(winkel)*u(xyz) + sin(winkel)*v(xyz);
            double a1 = 0, a2 = 0, a3 = 0;
            double b1 = 0, b2 = 0, b3 = 0;
            double c1 = 0, c2 = 0, c3 = 0;
            double n1 = 0, n2 = 0, n3 = 0;

            //double n1=0, n2=0, n3=0;
            //double n[] = null;

            double k[] = null;

            double[] M = {0, 0, 0};

            a1 = selection.get(0).getX();
            a2 = selection.get(0).getZ();
            a3 = selection.get(0).getY();

            b1 = selection.get(1).getX();
            b2 = selection.get(1).getZ();
            b3 = selection.get(1).getY();

            c1 = selection.get(2).getX();
            c2 = selection.get(2).getZ();
            c3 = selection.get(2).getY();

            message = new TextComponentString("a1 = " + a1 + "; a2 = " + a2 + "; a3 = " + a3);
            player.sendMessage(message);
            //player.sendMessage( "a1 = " + a1 + "; a2 = " + a2 + "; a3 = " + a3 );
            //player.sendMessage( "b1 = " + b1 + "; b2 = " + b2 + "; b3 = " + b3 );
            message = new TextComponentString("b1 = " + b1 + "; b2 = " + b2 + "; b3 = " + b3);
            player.sendMessage(message);
            //player.sendMessage( "c1 = " + c1 + "; c2 = " + c2 + "; c3 = " + c3 );
            message = new TextComponentString("c1 = " + c1 + "; c2 = " + c2 + "; c3 = " + c3);
            player.sendMessage(message);

            k = loadCoefficients(a1, a2, a3, b1, b2, b3, c1, c2, c3);
            n1 = k[75];
            n2 = k[76];
            n3 = k[77];

            if (k[39] != 0) {
                M[0] = k[40] / k[39];
            } else if (k[41] != 0) {
                M[0] = k[42] / k[41];
            } else if (k[43] != 0) {
                M[0] = k[44] / k[43];
            } else if (k[45] != 0) {
                M[0] = k[46] / k[45];
            } else if (k[47] != 0) {
                M[0] = k[48] / k[47];
            } else if (k[49] != 0) {
                M[0] = k[50] / k[49];
            } else {
                player.sendMessage(new TextComponentString("The three points form a straight line. - x"));
                err=true;
            }

            if (k[63] != 0) {
                M[1] = k[64] / k[63];
            } else if (k[65] != 0) {
                M[1] = k[66] / k[65];
            } else if (k[67] != 0) {
                M[1] = k[68] / k[67];
            } else if (k[69] != 0) {
                M[1] = k[70] / k[69];
            } else if (k[71] != 0) {
                M[1] = k[72] / k[71];
            } else if (k[73] != 0) {
                M[1] = k[74] / k[73];
            } else {
                player.sendMessage(new TextComponentString("The three points form a straight line. - y"));
                err=true;
            }

            if (k[51] != 0) {
                M[2] = k[52] / k[51];
            } else if (k[53] != 0) {
                M[2] = k[54] / k[53];
            } else if (k[55] != 0) {
                M[2] = k[56] / k[55];
            } else if (k[57] != 0) {
                M[2] = k[58] / k[57];
            } else if (k[59] != 0) {
                M[2] = k[60] / k[59];
            } else if (k[61] != 0) {
                M[2] = k[62] / k[61];
            } else {
                player.sendMessage(new TextComponentString("The three points form a straight line. - z"));
                err=true;
            }

            if(!err) {

                player.sendMessage(new TextComponentString("M(" + Math.round(M[0]) + "/" + Math.round(M[2]) + "/" + Math.round(M[1]) + ")"));

                //w.getBlockAt((int) Math.round(M[0]), (int) Math.round(M[1]), (int) Math.round(M[2])).setType(Material.EMERALD_BLOCK);
                if(ServerProxy.getWorld()!=null) {
                    final HWorld world = ServerProxy.getWorld();
                    BlockPos midPos = new BlockPos(M[0], M[1], M[2]);


                    double[] f = null;
                    double v1 = 0, v2 = 0, v3 = 0;
                    double u1 = 0, u2 = 0, u3 = 0;

                    f = loadV(a1, a2, a3, n1, n2, n3, M[0], M[2], M[1]);

                    u1 = f[6];
                    u2 = f[5];
                    u3 = f[4];

                    v1 = f[7] - f[8];
                    v2 = f[9] - f[10];
                    v3 = f[11] - f[12];

                    // K(xyz) = M(xyz) + cos(winkel)*u(xyz) + sin(winkel)*v(xyz);
                    double K[] = new double[3];
                    double phi = 0;

                    double radius = Math.sqrt(Math.pow((a1-M[0]),2)+ Math.pow((a2-M[2]),2)+Math.pow((a3-M[1]),2));
                    player.sendMessage(new TextComponentString("Radius: " + radius));
                    double umfang = 2 * Math.PI * radius;
                    player.sendMessage(new TextComponentString("Umfang: " + umfang));

                    if(autoQuality) quality = (int)(Math.pow(umfang,2));
                    player.sendMessage(new TextComponentString("Quality: " + quality));

                    double dphi = umfang/quality;
                    player.sendMessage(new TextComponentString("dphi: " + dphi));

                    List<BlockPos> seg1 = new ArrayList<BlockPos>();
                    List<BlockPos> seg2 = new ArrayList<BlockPos>();
                    List<BlockPos> seg3 = new ArrayList<BlockPos>();
                    List<BlockPos> temp = new ArrayList<BlockPos>();
                    List<BlockPos> mid = new ArrayList<BlockPos>();
                    Map<Integer, List<BlockPos>> currentSegment = new HashMap<Integer, List<BlockPos>>();
                    currentSegment.put(0, temp);
                    currentSegment.put(1, seg1);
                    currentSegment.put(2, seg2);
                    currentSegment.put(3, seg3);
                    currentSegment.put(4, mid);
                    int segKey = 0; //starting as temp.

                    Stack<Integer> stack = new Stack<Integer>();

                    short segmentation=0;
                    double fillfactor = 1;
                    double filldecrement = 1/(2.3*radius+1);
                    if(fill)  player.sendMessage(new TextComponentString("filldecrement: " + filldecrement));
                    boolean secondRound=false;

                    do {
                        for (int i = 0; i < quality; i++) {
                            K[0] = M[0] + (Math.cos(phi) * u1 + Math.sin(phi) * v1) * fillfactor;
                            K[2] = M[2] + (Math.cos(phi) * u2 + Math.sin(phi) * v2) * fillfactor;
                            K[1] = M[1] + (Math.cos(phi) * u3 + Math.sin(phi) * v3) * fillfactor;
                            BlockPos normalBlock = new BlockPos(Math.round(K[0]), Math.round(K[1]), Math.round(K[2]));
                            BlockPos floorfloorfloor = new BlockPos((int)Math.floor(K[0]), (int)Math.floor(K[1]), (int)Math.floor(K[2]));
                            BlockPos floorfloorceil = new BlockPos((int)Math.floor(K[0]), (int)Math.floor(K[1]), (int)Math.ceil(K[2]));
                            BlockPos floorceilfloor = new BlockPos((int)Math.floor(K[0]), (int)Math.ceil(K[1]), (int)Math.floor(K[2]));
                            BlockPos floorceilceil = new BlockPos((int)Math.floor(K[0]), (int)Math.ceil(K[1]), (int)Math.ceil(K[2]));
                            BlockPos ceilfloorfloor = new BlockPos((int)Math.ceil(K[0]), (int)Math.floor(K[1]), (int)Math.floor(K[2]));
                            BlockPos ceilfloorceil = new BlockPos((int)Math.ceil(K[0]), (int)Math.floor(K[1]), (int)Math.ceil(K[2]));
                            BlockPos ceilceilfloor = new BlockPos((int)Math.ceil(K[0]), (int)Math.ceil(K[1]), (int)Math.floor(K[2]));
                            BlockPos ceilceilceil = new BlockPos((int)Math.ceil(K[0]), (int)Math.ceil(K[1]), (int)Math.ceil(K[2]));

                            //adds the block, when it is a block from the selection, to the stack, to be able to figure out,
                            //to which segment the temp blocks belong.
                            if(!secondRound) {
                                for (int z = 0; z < 3; z++) {
                                    //if(isAroundSelection(blockPos, selection.get(z))) {
                                    if (normalBlock.equals(selection.get(z))) {
                                        if (stack.size() == 0 || (stack.size() == 1 && stack.get(0) != z)) { //just add first two blocks
                                            //remember which point is faced first
                                            stack.push(z);

                                        }
                                        segKey = z + 1;
                                        segmentation++;
                                    }
                                }
                            }

                        /*
                        if(blockPos.equals(selection.get(0))) {
                            //start seg1
                            if(stack.size()==0 || (stack.size()==1 && stack.get(0)!=0)) { //just add first two blocks
                                //remember which point is faced first
                                stack.push(0);
                            }
                            segKey = 1;
                        }else if(blockPos.equals(selection.get(1))) {
                            //start seg2
                            segKey = 2;
                        }else if(blockPos.equals(selection.get(2))) {
                            //start seg3
                            segKey = 3;
                        }*/

                        /*if(segmentation>0) {
                            if(segKey>1) {
                                currentSegment.get(segKey-1).add(blockPos);
                            }else if(segKey==1) {
                                currentSegment.get(3).add(blockPos);
                            }
                            if(segmentation>2) segmentation=0;
                        }else {*/

                        if(secondRound) {
                            if(!currentSegment.get(1).contains(normalBlock) && overwrite(world, normalBlock)) currentSegment.get(1).add(normalBlock);
                            if(/*mode==MODE_THICK*/thick) {
                                if (!currentSegment.get(1).contains(floorfloorfloor)&& overwrite(world, normalBlock))
                                    currentSegment.get(1).add(floorfloorfloor);
                                if (!floorfloorceil.equals(floorfloorfloor) && !currentSegment.get(1).contains(floorfloorceil)&& overwrite(world, normalBlock))
                                    currentSegment.get(1).add(floorfloorceil);
                                if (!floorceilfloor.equals(floorfloorfloor) && !currentSegment.get(1).contains(floorceilfloor)&& overwrite(world, normalBlock))
                                    currentSegment.get(1).add(floorceilfloor);
                                if (!floorceilceil.equals(floorfloorfloor) && !currentSegment.get(1).contains(floorceilceil)&& overwrite(world, normalBlock))
                                    currentSegment.get(1).add(floorceilceil);
                                if (!ceilfloorfloor.equals(floorfloorfloor) && !currentSegment.get(1).contains(ceilfloorfloor)&& overwrite(world, normalBlock))
                                    currentSegment.get(1).add(ceilfloorfloor);
                                if (!ceilfloorceil.equals(floorfloorfloor) && !currentSegment.get(1).contains(ceilfloorceil)&& overwrite(world, normalBlock))
                                    currentSegment.get(1).add(ceilfloorceil);
                                if (!ceilceilfloor.equals(floorfloorfloor) && !currentSegment.get(1).contains(ceilceilfloor)&& overwrite(world, normalBlock))
                                    currentSegment.get(1).add(ceilceilfloor);
                                if (!ceilceilceil.equals(floorfloorfloor) && !currentSegment.get(1).contains(ceilceilceil)&& overwrite(world, normalBlock))
                                    currentSegment.get(1).add(ceilceilceil);
                            }
                        }else {
                            if (!currentSegment.get(segKey).contains(normalBlock)&& overwrite(world, normalBlock))
                                currentSegment.get(segKey).add(normalBlock);
                            if (/*mode==MODE_THICK*/thick) {
                                if (!currentSegment.get(segKey).contains(floorfloorfloor)&& overwrite(world, normalBlock))
                                    currentSegment.get(segKey).add(floorfloorfloor);
                                if (!floorfloorceil.equals(floorfloorfloor) && !currentSegment.get(segKey).contains(floorfloorceil)&& overwrite(world, normalBlock))
                                    currentSegment.get(segKey).add(floorfloorceil);
                                if (!floorceilfloor.equals(floorfloorfloor) && !currentSegment.get(segKey).contains(floorceilfloor)&& overwrite(world, normalBlock))
                                    currentSegment.get(segKey).add(floorceilfloor);
                                if (!floorceilceil.equals(floorfloorfloor) && !currentSegment.get(segKey).contains(floorceilceil)&& overwrite(world, normalBlock))
                                    currentSegment.get(segKey).add(floorceilceil);
                                if (!ceilfloorfloor.equals(floorfloorfloor) && !currentSegment.get(segKey).contains(ceilfloorfloor)&& overwrite(world, normalBlock))
                                    currentSegment.get(segKey).add(ceilfloorfloor);
                                if (!ceilfloorceil.equals(floorfloorfloor) && !currentSegment.get(segKey).contains(ceilfloorceil)&& overwrite(world, normalBlock))
                                    currentSegment.get(segKey).add(ceilfloorceil);
                                if (!ceilceilfloor.equals(floorfloorfloor) && !currentSegment.get(segKey).contains(ceilceilfloor)&& overwrite(world, normalBlock))
                                    currentSegment.get(segKey).add(ceilceilfloor);
                                if (!ceilceilceil.equals(floorfloorfloor) && !currentSegment.get(segKey).contains(ceilceilceil)&& overwrite(world, normalBlock))
                                    currentSegment.get(segKey).add(ceilceilceil);
                            }
                        }
                            //}
                        /*if(filter.get(mode).blockAllowed(blockPos, selection)){
                            world.setBlockState(blockPos,  Blocks.EMERALD_BLOCK.getDefaultState(), 3);
                        }*/
                            phi += dphi;
                        }
                        fillfactor -= filldecrement;
                        phi=0;
                        secondRound=true;
                    }while (fill && fillfactor>0.001);

                    int tempKey = ((stack.peek()+1)%3)+1; //maps values from 1,0,2 to 3,2,1
                    currentSegment.get(tempKey).addAll(temp);

                    if(super.mid) {
                        //world.setBlockState(midPos,  Blocks.DIAMOND_BLOCK.getDefaultState(), 3);
                        currentSegment.get(4).add(midPos);
                    }
                    filter.get(mode).paintSegments(player, world, currentSegment, blockType);
                }
            }
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


    private boolean isAroundSelection(BlockPos block, BlockPos selection) {
        return (block.equals(selection) ||
                (Math.abs(block.getX()-selection.getX()) <= 1 &&
                Math.abs(block.getY()-selection.getY()) <= 1 &&
                Math.abs(block.getZ()-selection.getZ()) <= 1 ));
    }

    private double[] loadV( 	double ox, double oy, double oz,
                               double n1, double n2, double n3,
                               double mx, double my, double mz		)
    {
        double f[] = new double[13];

        f[0] = Math.sqrt( n1*n1 + n2*n2 + n3*n3 ); //Betrag von n fuer normalvektor berechnung

        f[1] = n1 / f[0];
        f[2] = n2 / f[0];
        f[3] = n3 / f[0];

        f[4] = oz - mz;
        f[5] = oy - my;
        f[6] = ox - mx;

        f[7] = f[2] * f[4];
        f[8] = f[3] * f[5];
        f[9] = f[3] * f[6];
        f[10] = f[1] * f[4];
        f[11] = f[1] * f[5];
        f[12] = f[2] * f[6];

        return f;
    }


    private double[] loadCoefficients( 	double a1, double a2, double a3,
                                          double b1, double b2, double b3,
                                          double c1, double c2, double c3		)
    {
        double k[] = new double[78];
        double n1=0, n2=0, n3=0;
        //page 1
        k[0] = 0.5 * ( b1*b1 - a1*a1 + b2*b2 - a2*a2 + b3*b3 - a3*a3 );
        k[1] = b1 - a1;
        k[2] = b2 - a2;
        k[3] = b3 - a3;

        k[4] = 0.5 * ( c1*c1 - b1*b1 + c2*c2 - b2*b2 + c3*c3 - b3*b3 );
        k[5] = c1 - b1;
        k[6] = c2 - b2;
        k[7] = c3 - b3;

        k[8] = c3 - a3;
        k[9] = c2 - a2;
        k[10] = c1 - a1;

        n1 = k[2]*k[8] - k[3]*k[9];
        n2 = -( k[1]*k[8] - k[3]*k[10] );
        n3 = k[1]*k[9] - k[2]*k[10];

        k[75] = n1;
        k[76] = n2;
        k[77] = n3;

//		n = new double[3];
//		n[0] = n1;
//		n[1] = n2;
//		n[2] = n3;

        k[11] = n1*a1 + n2*a2 + n3*a3;

        k[12] = k[1]*k[6] - k[5]*k[2];
        k[13] = k[1]*k[7] - k[5]*k[3];
        k[14] = k[1]*k[4] - k[5]*k[0];

        k[15] = k[1]*n2 - n1*k[2];
        k[16] = k[1]*n3 - n1*k[3];
        k[17] = k[1]*k[11] - n1*k[0];

        k[18] = k[5]*n2 - n1*k[6];
        k[19] = k[5]*n3 - n1*k[7];
        k[20] = k[5]*k[11] - n1*k[4];

        k[21] = -k[12];
        k[22] = k[2]*k[7] - k[6]*k[3];
        k[23] = k[2]*k[4] - k[6]*k[0];

        k[24] = -k[15];
        k[25] = k[2]*n3 - n2*k[3];
        k[26] = k[2]*k[11] - n2*k[0];

        k[27] = -k[19];
        k[28] = k[6]*n3 - n2*k[7];
        k[29] = k[6]*k[11] - n2*k[4];

        //page 2
        k[30] = -k[13];
        k[31] = -k[22];
        k[32] = k[3]*k[4] - k[7]*k[0];

        k[33] = -k[16];
        k[34] = -k[25];
        k[35] = k[3]*k[11] - n3*k[0];

        k[36] = -k[19];
        k[37] = -k[28];
        k[38] = k[7]*k[11] - n3*k[4];

        k[39] = k[31]*k[33] - k[34]*k[30];
        k[40] = k[31]*k[35] - k[34]*k[32];

        k[41] = k[31]*k[36] - k[37]*k[30];
        k[42] = k[31]*k[38] - k[37]*k[32];

        k[43] = k[34]*k[36] - k[37]*k[33];
        k[44] = k[34]*k[38] - k[37]*k[35];

        k[45] = k[22]*k[24] - k[25]*k[21];
        k[46] = k[22]*k[26] - k[25]*k[23];

        k[47] = k[22]*k[27] - k[28]*k[21];
        k[48] = k[22]*k[29] - k[28]*k[23];

        k[49] = k[25]*k[27] - k[28]*k[24];
        k[50] = k[25]*k[29] - k[28]*k[26];

        k[51] = k[30]*k[34] - k[33]*k[31];
        k[52] = k[30]*k[35] - k[33]*k[32];

        k[53] = k[30]*k[37] - k[36]*k[31];
        k[54] = k[30]*k[38] - k[36]*k[32];

        k[55] = k[33]*k[37] - k[36]*k[34];
        k[56] = k[33]*k[38] - k[36]*k[35];

        k[57] = k[13]*k[15] - k[16]*k[12];
        k[58] = k[13]*k[17] - k[16]*k[14];

        k[59] = k[13]*k[18] - k[19]*k[12];
        k[60] = k[13]*k[20] - k[19]*k[14];

        k[61] = k[18]*k[16] - k[19]*k[15];
        k[62] = k[16]*k[20] - k[19]*k[17];

        k[63] = k[12]*k[16] - k[15]*k[13];
        k[64] = k[12]*k[17] - k[15]*k[14];

        k[65] = k[12]*k[19] - k[18]*k[13];
        k[66] = k[12]*k[20] - k[18]*k[14];

        k[67] = k[15]*k[19] - k[18]*k[16];
        k[68] = k[15]*k[20] - k[18]*k[17];

        k[69] = k[21]*k[25] - k[24]*k[22];
        k[70] = k[21]*k[26] - k[24]*k[23];

        k[71] = k[21]*k[28] - k[27]*k[22];
        k[72] = k[21]*k[29] - k[27]*k[23];

        k[73] = k[21]*k[28] - k[27]*k[25];
        k[74] = k[21]*k[29] - k[27]*k[26];

        return k;
    }

    @Override
    public void setQuality(int quality) {
        if(quality>=1) this.quality = quality;
        autoQuality=false;
    }

    @Override
    public void setQualityAuto(boolean autoset) {
        this.autoQuality = autoset;
    }

    @Override
    public int setSubMode(int mode) {
        if( mode>=MODE_FULL && mode<MODES ) {
            this.mode = mode;
            return mode;
        }else return this.mode;
    }

    @Override
    public int getSubMode() {
        return mode;
    }

}

interface Filter {
    public void paintSegments(EntityPlayer player, HWorld world, Map<Integer, List<BlockPos>> segments, IBlockState blockType);
}

/**
 * Full circle (not filled)
 */
class FilterMode0 implements Filter {
    @Override
    public void paintSegments(EntityPlayer player, HWorld world, Map<Integer, List<BlockPos>> segments, IBlockState blockType) {
        List<BlockPos> segmentsSum = new ArrayList<>();
        for(Map.Entry<Integer, List<BlockPos>> segment : segments.entrySet()) {
            segmentsSum.addAll(segment.getValue());
        }
        world.setBlocks(segmentsSum, blockType, 3);
    }
}

/**
 * Segments from first selection to third selection (in this order)
 */
class FilterMode2 implements Filter {
    @Override
    public void paintSegments(EntityPlayer player, HWorld world, Map<Integer, List<BlockPos>> segments, IBlockState blockType) {
        List<BlockPos> segmentsSum = new ArrayList<>();
        for(int i=1; i<=2; i++) {
            segmentsSum.addAll(segments.get(i));
        }
        if(!segments.get(4).isEmpty()) {
            segmentsSum.add(segments.get(4).get(0));
        }
        world.setBlocks(segmentsSum, blockType, 3);
    }
}

/**
 * Segments from third selection to first selection (in this order)
 */
class FilterMode3 implements Filter {
    @Override
    public void paintSegments(EntityPlayer player, HWorld world, Map<Integer, List<BlockPos>> segments, IBlockState blockType) {
        List<BlockPos> segmentsSum = new ArrayList<>();
        segmentsSum.addAll(segments.get(3));
        if(!segments.get(4).isEmpty()) {
            segmentsSum.add(segments.get(4).get(0));
        }
        world.setBlocks(segmentsSum, blockType, 3);
    }
}

/**
 * Segment from first to second selection.
 */
class FilterMode4 implements Filter {
    @Override
    public void paintSegments(EntityPlayer player, HWorld world, Map<Integer, List<BlockPos>> segments, IBlockState blockType) {
        List<BlockPos> segmentsSum = new ArrayList<>();
        segmentsSum.addAll(segments.get(1));
        if(!segments.get(4).isEmpty()) {
            segmentsSum.add(segments.get(4).get(0));
        }
        world.setBlocks(segmentsSum, blockType, 3);
    }
}

