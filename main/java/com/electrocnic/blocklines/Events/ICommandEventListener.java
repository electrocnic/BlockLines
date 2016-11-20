package com.electrocnic.blocklines.Events;

import com.electrocnic.blocklines.EditTools.Tool;

/**
 * Created by Andreas on 19.11.2016.
 */
public interface ICommandEventListener {
    void onCommandEvent(Event event);
    Tool getTool(String key);
}
