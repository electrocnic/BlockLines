package com.electrocnic.blocklines.EditTools;

/**
 * Created by Andreas on 01.11.2016.
 */
public interface Qualifyable {
    public static final int DEFAULT_QUALITY = 1000;

    public void setQuality(int quality);
    public void setQualityAuto(boolean autoset);
}
