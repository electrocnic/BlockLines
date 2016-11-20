package com.electrocnic.blocklines.Commands;

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

    void setInARow(boolean inARow);
    boolean isInARow();

    void setSecondRow(boolean secondRow);
    boolean isSecondRow();
    //TODO: add more
}
