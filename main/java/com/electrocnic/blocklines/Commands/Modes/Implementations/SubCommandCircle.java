package com.electrocnic.blocklines.Commands.Modes.Implementations;

import com.electrocnic.blocklines.Commands.Modes.TemplateExecutive;
import com.electrocnic.blocklines.EditTools.Circle;
import com.electrocnic.blocklines.EditTools.Mode;
import com.electrocnic.blocklines.Events.BlockLinesEventHandler;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Andreas on 14.11.2016.
 */
public class SubCommandCircle extends TemplateExecutive<Circle> {

    private static final String NOT_SUPPORTED = "This mode is not supported for Circles.";

    public SubCommandCircle(BlockLinesEventHandler eventHandler) {
        super(eventHandler, eventHandler.getCircle());
    }

    @Override
    protected String useModeImpl() {
        super.setMode(Mode.Circle);
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