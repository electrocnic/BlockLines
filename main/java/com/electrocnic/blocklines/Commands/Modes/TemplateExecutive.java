package com.electrocnic.blocklines.Commands.Modes;

import com.electrocnic.blocklines.Commands.IFlag;
import com.electrocnic.blocklines.Commands.OptOut;
import com.electrocnic.blocklines.Events.ICommandEventListener;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import java.util.HashMap;
import java.util.Map;

/**
 * Base class for the Template-Pattern. Derived from the Subclass-Sandbox-Pattern-Baseclass.
 * This class implements the execute method from ICommand. The actual methods within execute must
 * be implemented in each Subclass individually. Non-Implemented Subclass methods will be empty and
 * thus do nothing. There is an additional method, called "useOther" which can do anything, not yet considered
 * in the execute method, if needed.
 */
public abstract class TemplateExecutive extends SubCommandSandbox {

    private Map<String, IFeedbackMethod> subcommands = null;

    public TemplateExecutive(ICommandEventListener eventHandler, IFlag modeObject) {
        super(eventHandler, modeObject);
        subcommands = new HashMap<String, IFeedbackMethod>();
        subcommands.put("a", this::useAirOnlyImpl);
        subcommands.put("m", this::useMidImpl);
        subcommands.put("t", this::useThicknessImpl);
        subcommands.put("f", this::useFillImpl);
        //todo: add other methods if needed.
    }

    /**
     * The actual execution-implementation from ICommand. The individual different components are implemented
     * in the correlating Sub-Class (e.g. SubCommandLine, SubCommandCircle...)
     * @param server The server. Not really needed here (but maybe sometimes later)
     * @param sender The sender. Important for feedback-messages to the player.
     * @param args The arguments. This decides the usage of the different methods. (useModeImpl will always be
     *             invoked, regardless of the amount of elements in args)
     */
    @Override
    public final void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        sender.sendMessage(new TextComponentString( useModeImpl() ));
        if(args.length >=3) {
            IFeedbackMethod method = subcommands.get(args[2]);
            if(method!=null) {
                sender.sendMessage(new TextComponentString( method.execute() ));
            } else {
                sender.sendMessage(new TextComponentString( useOtherImpl(args) ));
            }
        }
    }

    /**
     * Should call super.setMethod with the wanted mode-type.
     * @return The feedback-message String for output on screen for the player.
     */
    protected abstract String useModeImpl();

    /**
     * OPT-OUT: Override if NOT needed!!!
     * Should be implemented, and will likely use super.toggleAirOnly, if needed.
     * @return The feedback-message String for output on screen for the player.
     */
    @OptOut
    protected String useAirOnlyImpl() {
        return super.toggleAirOnly();
    }

    /**
     * OPT-OUT: Override if NOT needed!!!
     * If the placement of a mid-block is possible (see circle, ellipse, eventually cube), this method should be
     * implemented. Otherwise (e.g. for Lines), this method should be left empty. The implementation should use
     * super.toggleMid(). The toggleMid method returns a feedback string which can simply be returned, unless more
     * details are required.
     * @return The feedback-message String for output on screen for the player.
     */
    @OptOut
    protected String useMidImpl() {
        return super.toggleMid();
    }

    /**
     * OPT-OUT: Override if NOT needed!!!
     * If thick lines are possible (e.g. circle, ellipse), this method should be implemented. Otherwise, can be left
     * empty.
     * @return The feedback-message String for output on screen for the player.
     */
    @OptOut
    protected String useThicknessImpl() {
        return super.toggleThickness();
    }

    /**
     * OPT-OUT: Override if NOT needed!!!
     * If filled forms are possible (e.g. Circle, Cube, eventually Ellipse), this method should be implemented.
     * Otherwise (e.g. lines), this method should be left empty.
     * @return The feedback-message String for output on screen for the player.
     */
    @OptOut
    protected String useFillImpl() {
        return super.toggleFill();
    }

    /**
     * Used for other functionality, which is not directly templated in the execute method. Thus, this method
     * also takes the arguments to be able to implement different functionality.
     *
     * This method is especially useful, when there is a subclass which does not share a specific functionality
     * with other subclasses. Then, using this method can avoid overhead, as all other subclasses would otherwise need
     * to "implement" empty methods, which is useless.
     * @param args The arguments to determine detailed functionality.
     * @return The feedback-message String for output on screen for the player.
     */
    protected abstract String useOtherImpl(String[] args);
}