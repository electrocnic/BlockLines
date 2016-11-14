package com.electrocnic.blocklines.Commands.Modes;

import com.electrocnic.blocklines.Commands.ICommand;
import com.electrocnic.blocklines.Commands.IFlag;
import com.electrocnic.blocklines.EditTools.Mode;
import com.electrocnic.blocklines.Events.BlockLinesEventHandler;

/**
 * This class is the base class for Sub-Commands. This is probably the subclass-sandbox pattern.
 * Here are the methods implemented, which are used by subclasses.
 * @param <T> The type of the mode-object (on 14.11.2016 this would be for example: Circle, Line, Ellipse, Cube)
 */
public abstract class SubCommandSandbox<T extends IFlag> implements ICommand {

    protected BlockLinesEventHandler eventHandler = null;
    protected T modeObject = null;

    /**
     * The constructor for the SubCommandSandbox:
     * @param eventHandler The event-handler, needed to set the Mode.
     * @param modeObject The "mode-object" (for example: Circle, Ellipse, Line, Cube, ...)
     */
    public SubCommandSandbox(BlockLinesEventHandler eventHandler, T modeObject) {
        this.eventHandler = eventHandler;
        this.modeObject = modeObject;
    }

    /**
     * Sets the mode in the event handler.
     * @param mode The new mode. (See com.electrocnic.blocklines.EditTools.Mode for details)
     */
    protected void setMode(Mode mode) {
        this.eventHandler.setMode(mode);
    }

    /**
     * Toggles the airOnly-Flag in the mode-object. If the flag was true, it will be false afterwards and vice versa.
     * Air Only is used to determine, whether the drawing-function will override solid blocks or not.
     */
    protected String toggleAirOnly() {
        this.modeObject.setAirOnly(!this.modeObject.isAirOnly());
        return "Turned " + (this.modeObject.isAirOnly() ? ("off") : ("on")) + " overwrite solid blocks.";
    }

    /**
     * Toggles the multi-take performance of drawLine. (If multi-take is on, users can select multiple start points
     * and then switch to the next sequence to select an equal amount of end points)
     * @return The player feedback message whether this mode has been turned off or on.
     */
    protected String toggleInARow() {
        this.eventHandler.setInARow(!this.eventHandler.isInARow());
        return "Turned " + (eventHandler.isInARow() ? ("on") : ("off")) + " performing several lines in one take.";
    }

    /**
     * Toggles the placement of a diamond mid-block on or off. The mid can be placed for circles, ellipses and cubes.
     * @return The player feedback message whether this mode has been turned off or on.
     */
    protected String toggleMid() {
        this.modeObject.setMid(!this.modeObject.isMid());
        return "Turned " + (this.modeObject.isMid() ? ("on") : ("off")) + " placement of mid.";
    }

    /**
     * Toggles thick lines on or off. Not available for the draw-Lines mode.
     * @return The player feedback message whether this mode has been turned off or on.
     */
    protected String toggleThickness() {
        this.modeObject.setThick(!this.modeObject.isThick());
        return "Turned " + (this.modeObject.isThick() ? ("on") : ("off")) + " thick lines.";
    }

    /**
     * Toggles filled forms on or off... Possible for Circles, maybe ellipses, and cubes.
     * @return The player feedback message whether this mode has been turned off or on.
     */
    protected String toggleFill() {
        this.modeObject.setFill(!this.modeObject.isFill());
        return "Turned " + (this.modeObject.isFill() ? ("on") : ("off")) + " filled forms.";
    }

    /**
     * Attempts to set the mode for modeObject. If the arg is not a number, a NumberFormatException will be thrown.
     * @param args args[2] should be a number. Otherwise a NumberFormatException will be thrown.
     * @return Returns the mode of modeObject.
     * @throws NumberFormatException
     */
    protected int setSubmode(String[] args) throws NumberFormatException{
        int submode = 0;
        submode = Integer.parseInt(args[2]); //throws exception.
        submode = this.modeObject.setMode(submode);
        return submode;
    }

    //todo: implement other methods.

}