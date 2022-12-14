package com.electrocnic.blocklines.Events;

import com.electrocnic.blocklines.EditTools.*;
import com.electrocnic.blocklines.Mirror.IMirror;
import com.electrocnic.blocklines.Proxy.ServerProxy;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
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


public class BlockLinesEventHandler implements ICommandEventListener {
    private Map<String, Tool> tools = null;
    private Map<String, IEventMethod> eventMethods = null;
    private IMirror mirror = null;

    private short deStutter = 0;
    private String currentMode = Line.IDENTIFIER;

    private boolean buildingToolActive = false;

    public BlockLinesEventHandler() {
        tools = new HashMap<String, Tool>();
        eventMethods = new HashMap<String, IEventMethod>();
        eventMethods.put(Event.MODE, this::setMode);
        eventMethods.put(Event.ABORT, this::resetSelection);
        eventMethods.put(Event.MIRROR, this::mirrorSettings);
        eventMethods.put(Event.ACTIVATE, this::activate);
        eventMethods.put(Event.DEACTIVATE, this::deactivate);
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

    /**
     * Performs selection for mirror or building tools.
     * @param event
     */
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
                    if(isMirrorAxisSelecting()) {
                        if(mirror != null) {
                            if(mirror.performSelection(event.getPos())) {
                                mirror.setJustToggledOn(false);
                                mirror.setManuallySettingAxis(false);
                                event.getEntityPlayer().sendMessage(new TextComponentString("Mirror Axis selected: Mirror by " + mirror.getAxisName()));
                                if(mirror.isInvalid()) {
                                    event.getEntityPlayer().sendMessage(new TextComponentString("Error: Axis cannot form a cube. You must either select a point (a=b), a line or a plane.\n" +
                                            "The mirror has been deactivated. Activate again to select a new axis."));
                                    mirror.activateMirror(false);
                                }
                            }else{
                                event.getEntityPlayer().sendMessage(new TextComponentString("First mirror Axis point selected..."));
                            }
                        }else {
                            event.getEntityPlayer().sendMessage(new TextComponentString("Fatal error during selection for the mirror in BlockLinesEventHandler occurred."));
                        }
                    }else if(buildingToolActive){
                        ISelectable tool = tools.get(currentMode);
                        if (tool != null) {
                            tool.performSelection(event.getPos(), event.getEntityPlayer());
                        } else {
                            event.getEntityPlayer().sendMessage(new TextComponentString("Fatal error during selection in BlockLinesEventHandler occurred."));
                        }
                    }
                }
                deStutter++;
            }
        }
    }

    /**
     * Mirrors block when player placed block by hand.
     * @param event
     */
    @SubscribeEvent
    public void onBlockPlaceEvent(BlockEvent.PlaceEvent event) {
        if(mirror.isActive() && !mirror.isInvalid()) {
            BlockPos pos = event.getPos();
            IBlockState state = event.getPlacedBlock();
            IBlockState oldState = event.getBlockSnapshot().getReplacedBlock();
            List<BlockPos> singleBlockList = new ArrayList<BlockPos>();
            singleBlockList.add(pos);
            ServerProxy.getWorld().setBlocks(singleBlockList, state, oldState, 3);
        }
    }

    /**
     * Removes blocks when mirror is active and player destroys a block by hand.
     * @param event
     */
    @SubscribeEvent
    public void onBlockBreakEvent(BlockEvent.BreakEvent event) {
        if(mirror.isActive() && !mirror.isInvalid() && mirror.isAutoRemove()) {
            BlockPos pos = event.getPos();
            IBlockState state = Blocks.AIR.getDefaultState();
            IBlockState oldState = event.getState();

            List<BlockPos> singleBlockList = new ArrayList<BlockPos>();
            singleBlockList.add(pos);
            ServerProxy.getWorld().setBlocks(singleBlockList, state, oldState, 3);
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
        return "Unknown command.";
    }

    /**
     * Sets the current mode of the Mod and resets the selection for that mode.
     * @param mode Must be one of the IDENTIFIERS of either Circle, Line, Ellipse, Cube or other future adds...
     */
    private String setMode(Object mode) {
        String castedMode = (String) mode;
        if(castedMode != null && tools.containsKey(castedMode)) {
            this.currentMode = castedMode;
            resetSelection(null);
            return "Selection reset. Mode set to " + castedMode;
        }
        return "Unknown command.";
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

    private boolean isMirrorAxisSelecting() {
        return (mirror.hasJustToggledOn()&&(mirror.isAutoReset()||mirror.isInvalid()))||mirror.isManuallySettingAxis();
    }

    /**
     * Toggles mirror on of, if the arguments are empty.
     * Toggles horizontal or vertical mirror on or off if the argument is h or v.
     * @param arguments
     */
    private String mirrorSettings(Object arguments) {
        String args = (String) arguments;
        if(args==null || args.isEmpty() || args.equalsIgnoreCase(" ")) {
            mirror.toggleMirror();
            return "Toggled " + (mirror.isActive()?"on":"off") + " mirror." + (isMirrorAxisSelecting()?" Please select the mirror axis now.":"");
        }else if(args.equalsIgnoreCase("h")) {
            boolean on = mirror.toggleHorizontalMirror();
            return "Toggled horizontal mirror " + (on?"on.":"off.");
        }else if(args.equalsIgnoreCase("v")) {
            boolean on = mirror.toggleVerticalMirror();
            return "Toggled vertical mirror " + (on ? "on." : "off.");
        }else if(args.equalsIgnoreCase("s")) {
            mirror.setManuallySettingAxis(true);
            return "Please select the mirror axis now.";
        }else if(args.equalsIgnoreCase("autoreset")) {
            boolean on = mirror.toggleAutoReset();
            return "Toggled auto reset axis " + (on ? "on." : "off.");
        }else if(args.equalsIgnoreCase("odd")) {
            boolean odd = mirror.setOdd(true);
            return "Axis set to " + (odd?"odd":"even");
        }else if(args.equalsIgnoreCase("even")) {
            boolean odd = mirror.setOdd(false);
            return "Axis set to " + (odd?"odd":"even");
        }else if(args.equalsIgnoreCase("autoremove")) {
            boolean on = mirror.toggleAutoRemove();
            return "Auto-remove blocks when destroyed by hand toggled " + (on ? "on." : "off.");
        }
        return "Unknown command.";
    }

    private String activate(Object arguments) {
        this.buildingToolActive = true;
        return "BlockLines enabled. Right clicking will now select and draw forms.";
    }

    private String deactivate(Object arguments) {
        this.buildingToolActive = false;
        return "BlockLines disabled. Mirror has to be disabled separately if still enabled unless needed!";
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
