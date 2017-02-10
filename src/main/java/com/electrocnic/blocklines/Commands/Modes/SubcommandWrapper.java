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
import com.electrocnic.blocklines.Events.ICommandEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * A custom wrapper class for SubCommands...
 * Can be exchanged with another wrapper, which implements ISubCommands.
 */
public class SubcommandWrapper implements ISubCommands {

    private Map<String, ICommand> subcommands = null;

    public SubcommandWrapper() {
        this.subcommands = new HashMap<String, ICommand>();
    }

    /**
     * Adds an ICommand object with the specific mapping-key (the command as a string) to this wrapper.
     * @param command The command (which is a String, typed in by the player normally)
     * @param commandObject The ICommand object which executes the command.
     */
    public void addCommand(String command, ICommand commandObject) {
        this.subcommands.put(command, commandObject);
    }

    /**
     * Factory for BlockLines SubCommands. Add SubCommands here if needed.
     * @param eventHandler The eventhandler.
     * @return A new wrapper.
     */
    public static SubcommandWrapper create(ICommandEventListener eventHandler) {
        SubcommandWrapper wrapper = new SubcommandWrapper();
        wrapper.addCommand(Line.IDENTIFIER, new SubCommandLine(eventHandler));
        wrapper.addCommand(Ellipse.IDENTIFIER, new SubCommandEllipse(eventHandler));
        wrapper.addCommand(Circle.IDENTIFIER, new SubCommandCircle(eventHandler));
        wrapper.addCommand(Cube.IDENTIFIER, new SubCommandCube(eventHandler));
        return wrapper;
    }

    /**
     * Returns the command-object or null if not found.
     * @param command The command (key)
     * @return The executable ICommand for the key.
     */
    @Override
    public ICommand get(String command) {
        return this.subcommands.get(command);
    }
}
