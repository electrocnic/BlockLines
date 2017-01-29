package com.electrocnic.blocklines.Events;

import com.electrocnic.blocklines.EditTools.*;
import com.electrocnic.blocklines.Mirror.IMirror;
import com.electrocnic.blocklines.Proxy.ServerProxy;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Andreas on 31.10.2016.
 */
/*
public class BlockLinesEventHandler implements ICommandEventListener {
    private short deStutter = 0;
    private List<BlockPos> selection = null; //was static
    private List<BlockPos> selection2 = null; //

    private Map<Mode, IDrawable> generator = null;

    private Mode currentMode = Mode.Line; //
    private boolean inARow = false; //
    private int inARowCount = 0; //
    private boolean secondRow = false; //

    private final Line line = new Line(); //
    private final Ellipse ellipse = new Ellipse(); //
    private final Circle circle = new Circle(); //
    private final Cube cube = new Cube(); //

    public BlockLinesEventHandler() {
        deStutter = 0;
        selection = new ArrayList<BlockPos>();
        selection2 = new ArrayList<BlockPos>();
        generator = new HashMap<Mode, IDrawable>();
        generator.put(Mode.Line, line);
        generator.put(Mode.Ellipse, ellipse);
        generator.put(Mode.Circle, circle);
        generator.put(Mode.Cube, cube);
        inARowCount =0;
        secondRow = false;
    }

    @SubscribeEvent
    public void onClick(PlayerInteractEvent event) {
        //TODO: use "command-pattern" here too...  make methods for each command-setting, for example, the multiple-line selection does a different thing than the others... also the selection of a mirror plane...
        //TODO: use same patter as in commands: template and subclass sandbox: some methods will do the pretty much same thing. the methods will return the final selection (or nothing in case of mirror-plane)
        //TODO: the final selection will be put into the draw method...

        //TODO: try to couple selection mode with draw-class... thus, not in seperated methods, but in the same method... re-arrange the generator-hashmap.
        //TODO: BAD: as the selection can have different forms, it is now mandatory to be careful of how to call the draw method. this should not be as dangerous. make a beautiful pattern.
        if(event.getHand() == EnumHand.OFF_HAND) {
            if (deStutter >= 1) deStutter = 0;
            else {
                BlockPos pos = event.getPos();
                ITextComponent message = new TextComponentString("Right-Clicked Block: x=" + pos.getX() + ", y=" + pos.getY() + ", z=" + pos.getZ() + ", Side:" + event.getSide().name());
                event.getEntityPlayer().addChatMessage(message);
                //World world = event.getWorld();
                if(ServerProxy.getWorld()==null && event.getSide().isServer()) {
                    ServerProxy.setWorld(event.getWorld());
                    event.getEntityPlayer().setGameType(GameType.CREATIVE);
                }
                if(ServerProxy.getWorld()!=null) {
                    //ServerProxy.getWorld().setBlockToAir(pos);
                    if(!secondRow) {
                        selection.add(pos);
                    }else if(secondRow) {
                        selection2.add(pos);
                    }
                    message = new TextComponentString("Block has been added to your " + (inARow ?(secondRow ?"second ":"first "):"") + "selection. Selected: " + (secondRow ?selection2:selection).size());
                    event.getEntityPlayer().addChatMessage(message);
                    if((!(inARow &&currentMode==Mode.Line) && selection.size() == generator.get(currentMode).getSelectionCount())
                            || ((inARow &&currentMode==Mode.Line) && secondRow && selection2.size() >= selection.size())) {
                        if(!(inARow &&currentMode==Mode.Line)) generator.get(currentMode).draw(event.getEntityPlayer(), selection, ServerProxy.getWorld().getBlockState(selection.get(0)));
                        else {
                            for(int i=0; i<selection.size(); i++) {
                                List<BlockPos> temp = new ArrayList<>();
                                temp.add(selection.get(i));
                                temp.add(selection2.get(i));
                                generator.get(currentMode).draw(event.getEntityPlayer(), temp, ServerProxy.getWorld().getBlockState(temp.get(0)));
                            }
                        }
                        //Circle circle = new Circle(selection, event.getEntityPlayer());
                        selection = new ArrayList<BlockPos>();
                        selection2 = new ArrayList<BlockPos>();
                        secondRow = false;
                        inARowCount = 0;
                        //circle.draw(event.getEntityPlayer());
                    }else if(!inARow &&selection.size()>generator.get(currentMode).getSelectionCount()) {
                        selection = new ArrayList<BlockPos>();
                    }
                }

                deStutter++;
            }
        }
    }


    public Line getLine() {
        return line;
    }

    public Ellipse getEllipse() {
        return ellipse;
    }

    public Circle getCircle() {
        return circle;
    }

    public Cube getCube() {
        return cube;
    }

    public void setInARow(boolean inARow) {
        this.inARow = inARow;
    }

    public boolean isInARow() {
        return inARow;
    }

    public void setSecondRow(boolean secondRaw) {
        this.secondRow = secondRaw;
    }

    public boolean getSecondRow() {
        return secondRow;
    }

    public void setSubMode(Mode mode) {
        this.currentMode = mode;
    }

    public Mode getCurrentMode() {
        return currentMode;
    }

    public void resetSelection() {
        selection = new ArrayList<BlockPos>();
        selection2 = new ArrayList<BlockPos>();
        inARowCount = 0;
        secondRow = false;
    }
}*/



public class BlockLinesEventHandler implements ICommandEventListener {
    private Map<String, Tool> tools = null;
    private Map<String, IEventMethod> eventMethods = null;
    private IMirror mirror = null;
    private boolean mirrorJustToggledOn = false;

    private short deStutter = 0;
    private String currentMode = Line.IDENTIFIER;


    public BlockLinesEventHandler() {
        tools = new HashMap<String, Tool>();
        eventMethods = new HashMap<String, IEventMethod>();
        eventMethods.put(Event.MODE, this::setMode);
        eventMethods.put(Event.ABORT, this::resetSelection);
        eventMethods.put(Event.MIRROR, this::mirrorSettings);
        //TODO: add methods if needed
    }

    public void addDrawable(String key, Tool drawable) {
        this.tools.put(key, drawable);
    }

    /**
     * Returns the mapped selectable object to the given key.
     * @param key The key for the selectable. Should be an IDENTIFIER (of Circle, Line, Ellipse, etc.)
     * @return The selectable object or null if not mapped.
     */
    @Override
    public Tool getTool(String key) {
        return tools.get(key);
    }

    @Override
    public void setMirror(IMirror mirror) {
        this.mirror = mirror;
    }

    @SubscribeEvent
    public void onClick(PlayerInteractEvent event) {
        //1. destutter.
        //2. call specific selection mode...
        //maybe it is better to call selection per drawable, so different selection-behaviour does not corrupt the whole thing?
        //thus make super class which implements basic selection behaviour and override for special snowflakes.
        if(event.getHand() == EnumHand.OFF_HAND) {
            if (deStutter >= 1) deStutter = 0;
            else {
                if (ServerProxy.getWorld() != null) {
                    if(mirrorJustToggledOn) {
                        if(mirror != null) {
                            if(mirror.performSelection(event.getPos())) {
                                mirrorJustToggledOn = false;
                                event.getEntityPlayer().addChatMessage(new TextComponentString("Mirror Axis selected: Mirror by " + mirror.getAxisName()));
                                if(mirror.isInvalid()) {
                                    event.getEntityPlayer().addChatMessage(new TextComponentString("Error: Axis cannot form a cube. You must either select a point (a=b), a line or a plane.\n" +
                                            "The mirror has been deactivated. Activate again to select a new axis."));
                                    mirror.activateMirror(false);
                                }
                            }else{
                                event.getEntityPlayer().addChatMessage(new TextComponentString("First mirror Axis point selected..."));
                            }
                        }else {
                            event.getEntityPlayer().addChatMessage(new TextComponentString("Fatal error during selection for the mirror in BlockLinesEventHandler occurred."));
                        }
                    }else {
                        ISelectable tool = tools.get(currentMode);
                        if (tool != null) {
                            tool.performSelection(event.getPos(), event.getEntityPlayer());
                        } else {
                            event.getEntityPlayer().addChatMessage(new TextComponentString("Fatal error during selection in BlockLinesEventHandler occurred."));
                        }
                    }
                }
                deStutter++;
            }
        }
    }

    //TODO: remove mirrored blocks if player removes a block by hand. (only remove blocks which are placed by mirror or let player choose via option).

    @SubscribeEvent
    public void onBlockPlaceEvent(BlockEvent.PlaceEvent event) {
        //event.getPlayer()
        //event.getPlayer().addChatMessage(new TextComponentString("Placed block."));
        if(mirror.isActive() && !mirror.isInvalid()) {
            BlockPos pos = event.getPos();
            IBlockState state = event.getPlacedBlock();
            /*if (event.isCancelable()) {
                event.setCanceled(true);
            }*/
            //IBlockState oldState = ServerProxy.getCurrentState(pos);

            //event.getPlayer().addChatMessage(new TextComponentString("Placed block at " + pos.toString()));
            List<BlockPos> singleBlockList = new ArrayList<BlockPos>();
            singleBlockList.add(pos);
            ServerProxy.getWorld().setBlocks(singleBlockList, state, 3);
        }
    }

    /**
     * Whenever an command has been made by the player which affects properties of the eventHandler directly (for
     * example and for now the current mode), this method will be invoked. This method looks, if there is a method
     * mapped for the given key within the even-argument. If so, it tries to invoke adoptSettings() with the
     * given description of the event as argument.
     * @param event An event object with a key from the static fields of the Event class and with custom description
     *              value. The description value can for example be: Circle.IDENTIFIER, in combination with
     *              Event.MODE, this will set the mode to circle for example.
     */
    @Override
    public String onCommandEvent(Event event) {
        IEventMethod method = this.eventMethods.get(event.getKey());
        if(method!=null) {
            return method.adoptSettings(event.getDescription());
        }
        return "Error in onCommandEvent.";
    }

    /**
     * Sets the current mode of the Mod and resets the selection for that mode.
     * @param mode Must be one of the IDENTIFIERS of either Circle, Line, Ellipse, Cube or other future adds...
     */
    private String setMode(Object mode) {
        String castedMode = (String) mode;
        if(castedMode != null) {
            this.currentMode = castedMode;
            resetSelection(null);
            return "Selection reset. Mode set to " + castedMode;
        }
        return "Error in setMode.";
    }

    /**
     * Reset the selection to abort any unintentional behaviour while drawing structures in Minecraft.
     * Resets the selection only for the current mode.
     * @param argument Just an empty null object, as placeholder, otherwise the lambda would not work.
     */
    private String resetSelection(Object argument) {
        tools.get(currentMode).resetSelection();
        return "Selection reset.";
    }

    /**
     * Toggles mirror on of, if the arguments are empty.
     * Toggles horizontal or vertical mirror on or off if the argument is h or v.
     * @param arguments
     */
    private String mirrorSettings(Object arguments) {
        String args = (String) arguments;
        if(args==null || args.isEmpty() || args.equalsIgnoreCase(" ")) {
            mirrorJustToggledOn = mirror.toggleMirror();
            return "Toggled " + (mirrorJustToggledOn?"on":"off") + " mirror." + (mirrorJustToggledOn?" Please select the mirror axis now.":"");
        }else if(args.equalsIgnoreCase("h")) {
            boolean on = mirror.toggleHorizontalMirror();
            return "Toggled horizontal mirror " + (on?"on.":"off.");
        }else if(args.equalsIgnoreCase("v")) {
            boolean on = mirror.toggleVerticalMirror();
            return "Toggled vertical mirror " + (on ? "on." : "off.");
        }else if(args.equalsIgnoreCase("s")) {
            mirrorJustToggledOn = true;
            return "Please select the mirror axis now.";
        }
        return "Error in mirrorSettings";
    }

    public static BlockLinesEventHandler create() {
        BlockLinesEventHandler eventHandler = new BlockLinesEventHandler();
        eventHandler.addDrawable(Line.IDENTIFIER, new Line());
        eventHandler.addDrawable(Ellipse.IDENTIFIER, new Ellipse());
        eventHandler.addDrawable(Circle.IDENTIFIER, new Circle());
        eventHandler.addDrawable(Cube.IDENTIFIER, new Cube());
        return eventHandler;
    }

}
