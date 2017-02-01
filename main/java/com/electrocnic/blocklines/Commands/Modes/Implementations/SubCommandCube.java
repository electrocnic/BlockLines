package com.electrocnic.blocklines.Commands.Modes.Implementations;

import com.electrocnic.blocklines.Commands.Modes.TemplateExecutive;
import com.electrocnic.blocklines.EditTools.Cube;
import com.electrocnic.blocklines.Events.ICommandEventListener;

/**
 * Created by Andreas on 14.11.2016.
 */
public class SubCommandCube extends TemplateExecutive {

    private static final String NOT_SUPPORTED = "This mode is not supported for Cubes.";

    public SubCommandCube(ICommandEventListener eventHandler) {
        super(eventHandler, eventHandler.getTool(Cube.IDENTIFIER));
    }

    @Override
    protected String useModeImpl() {
        return super.setMode(Cube.IDENTIFIER);
    }

    @Override
    protected String useOtherImpl(String[] args) {
        return NOT_SUPPORTED;
    }
}