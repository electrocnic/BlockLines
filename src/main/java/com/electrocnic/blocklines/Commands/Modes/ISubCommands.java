package com.electrocnic.blocklines.Commands.Modes;

import com.electrocnic.blocklines.Commands.ICommand;

/**
 * Created by Andreas on 18.11.2016.
 */
public interface ISubCommands {
    ICommand get(String command);
}
