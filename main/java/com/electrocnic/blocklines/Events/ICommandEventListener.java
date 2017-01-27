package com.electrocnic.blocklines.Events;

import com.electrocnic.blocklines.EditTools.Tool;
import com.electrocnic.blocklines.Mirror.IMirror;

/**
 * Created by Andreas on 19.11.2016.
 */
public interface ICommandEventListener {
    void onCommandEvent(Event event);
    Tool getTool(String key);
    void setMirror(IMirror mirror);
}
