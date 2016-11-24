package com.electrocnic.blocklines.Commands.Modes.Implementations;

import com.electrocnic.blocklines.Commands.Modes.TemplateExecutive;
import com.electrocnic.blocklines.EditTools.Ellipse;
import com.electrocnic.blocklines.Events.ICommandEventListener;

/**
 * Created by Andreas on 14.11.2016.
 */
public class SubCommandEllipse extends TemplateExecutive {

    private static final String NOT_SUPPORTED = "This mode is not supported for Ellipses.";

    public SubCommandEllipse(ICommandEventListener eventHandler) {
        super(eventHandler, eventHandler.getTool(Ellipse.IDENTIFIER));
    }

    @Override
    protected String useModeImpl() {
        super.setMode(Ellipse.IDENTIFIER);
        return "Mode set to Ellipse.";
    }

    @Override
    protected String useOtherImpl(String[] args) {
        int submode = 0;
        try{
            submode = super.setSubmode(args);
        }catch (NumberFormatException e) {
            return "Mode for ellipse has to be 0, 1, 2, 3 or 4; m, t or a";
        }
        return "Mode for ellipse set to " + submode;
    }
}
