package com.electrocnic.blocklines.Commands.Modes.Implementations;

import com.electrocnic.blocklines.Commands.Modes.TemplateExecutive;
import com.electrocnic.blocklines.EditTools.Cube;
import com.electrocnic.blocklines.EditTools.Mode;
import com.electrocnic.blocklines.Events.BlockLinesEventHandler;

/**
 * Created by Andreas on 14.11.2016.
 */
public class SubCommandCube extends TemplateExecutive<Cube> {

    private static final String NOT_SUPPORTED = "This mode is not supported for Cubes.";

    public SubCommandCube(BlockLinesEventHandler eventHandler) {
        super(eventHandler, eventHandler.getCube());
    }

    @Override
    protected String useModeImpl() {
        super.setMode(Mode.Cube);
        return "Mode set to Cube.";
    }

    @Override
    protected String useOtherImpl(String[] args) {
        return NOT_SUPPORTED;
    }
}