package com.electrocnic.blocklines.Commands.Modes;

import com.electrocnic.blocklines.Commands.ICommand;
import com.electrocnic.blocklines.Commands.Modes.Implementations.SubCommandCircle;
import com.electrocnic.blocklines.Commands.Modes.Implementations.SubCommandCube;
import com.electrocnic.blocklines.Commands.Modes.Implementations.SubCommandEllipse;
import com.electrocnic.blocklines.Commands.Modes.Implementations.SubCommandLine;
import com.electrocnic.blocklines.EditTools.Circle;
import com.electrocnic.blocklines.EditTools.Cube;
import com.electrocnic.blocklines.EditTools.Ellipse;
import com.electrocnic.blocklines.EditTools.Line;
import com.electrocnic.blocklines.Events.BlockLinesEventHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andreas on 18.11.2016.
 */
public class SubcommandWrapper implements ISubCommands {

    private Map<String, ICommand> subcommands = null;

    public SubcommandWrapper() {
        this.subcommands = new HashMap<String, ICommand>();
    }

    public void addCommand(String command, ICommand commandObject) {
        this.subcommands.put(command, commandObject);
    }

    public static SubcommandWrapper create(BlockLinesEventHandler eventHandler) {
        SubcommandWrapper wrapper = new SubcommandWrapper();
        wrapper.addCommand(Line.IDENTIFIER, new SubCommandLine(eventHandler));
        wrapper.addCommand(Ellipse.IDENTIFIER, new SubCommandEllipse(eventHandler));
        wrapper.addCommand(Circle.IDENTIFIER, new SubCommandCircle(eventHandler));
        wrapper.addCommand(Cube.IDENTIFIER, new SubCommandCube(eventHandler));
        return wrapper;
    }

    @Override
    public ICommand get(String command) {
        return this.subcommands.get(command);
    }
}
