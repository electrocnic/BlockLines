package com.electrocnic.blocklines.Commands.Modes.Implementations;

import com.electrocnic.blocklines.Commands.Modes.TemplateExecutive;
import com.electrocnic.blocklines.EditTools.Circle;
import com.electrocnic.blocklines.Events.ICommandEventListener;

/**
 * Created by Andreas on 14.11.2016.
 */
public class SubCommandCircle extends TemplateExecutive {

    private static final String NOT_SUPPORTED = "This mode is not supported for Circles.";

    public SubCommandCircle(ICommandEventListener eventHandler) {
        super(eventHandler, eventHandler.getTool(Circle.IDENTIFIER));
    }

    @Override
    protected String useModeImpl() {
        super.setMode(Circle.IDENTIFIER);
        return "Mode set to Circle.";
    }

    @Override
    protected String useOtherImpl(String[] args) {
        int submode = 0;
        try{
            submode = super.setSubmode(args);
        }catch (NumberFormatException e) {
            return "Mode for circle has to be 0, 1, 2 or 3; m, t, a or f";
        }
        return "Mode for circle set to " + submode;
    }

    @Override
    public String toString() {
        return "circle";
    }
}