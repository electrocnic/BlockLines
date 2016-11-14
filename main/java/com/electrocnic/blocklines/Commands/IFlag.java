package com.electrocnic.blocklines.Commands;

import com.electrocnic.blocklines.EditTools.Mode;

/**
 * Created by Andreas on 14.11.2016.
 */
public interface IFlag {
    void setAirOnly(boolean airOnly);
    boolean isAirOnly();

    void setMid(boolean mid);
    boolean isMid();

    void setThick(boolean thick);
    boolean isThick();

    void setFill(boolean fill);
    boolean isFill();

    int setMode(int mode);
    int getMode();
    //TODO: add more
}
