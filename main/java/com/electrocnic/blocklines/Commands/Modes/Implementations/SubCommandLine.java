package com.electrocnic.blocklines.Commands.Modes.Implementations;

import com.electrocnic.blocklines.Commands.Modes.TemplateExecutive;
import com.electrocnic.blocklines.EditTools.Line;
import com.electrocnic.blocklines.EditTools.Mode;
import com.electrocnic.blocklines.Events.BlockLinesEventHandler;

/**
 * Created by Andreas on 14.11.2016.
 */
public class SubCommandLine extends TemplateExecutive<Line> {

    private static final String NOT_SUPPORTED = "This mode is not supported for Lines.";

    public SubCommandLine(BlockLinesEventHandler eventHandler) {
        super(eventHandler, eventHandler.getLine());
    }

    @Override
    protected String useModeImpl() {
        super.setMode(Mode.Line);
        return "Mode set to Line.";
    }

    @Override
    protected String useMidImpl() {
        return NOT_SUPPORTED;
    }

    @Override
    protected String useThicknessImpl() {
        return NOT_SUPPORTED;
    }

    @Override
    protected String useFillImpl() {
        return NOT_SUPPORTED;
    }

    @Override
    protected String useOtherImpl(String[] args) {
        String result = "";
        if(args[2].equalsIgnoreCase("row")) {
            result = super.toggleInARow();
            result += "\n" +
                    "Select the blocks which are the \"first\" blocks. Then:\n" +
                    "Use the command /bl line next - which stands for \"next selection sequence\", then\n" +
                    "when you have the same amount in both selection sequences, the lines will automatically\n" +
                    "be drawn. Remember, that the first selection of the first selection sequence is used\n" +
                    "with the first selection of the second selection sequence to build a line and so on...";
        }else if(args[2].equalsIgnoreCase("next")) {
            eventHandler.setSecondRow(true);
            result = "Second selection sequence for multiple line creation is now active.";
        }else {
            result = "Command not found. Operation not supported. Supported Operations are: "; //TODO: add operations for line in string leftside.
        }

        return result;
    }
}