package com.electrocnic.blocklines.Events;

/**
 * Created by Andreas on 20.11.2016.
 */
@FunctionalInterface
public interface IEventMethod {
    /**
     * Adopts settings for different objects, on user-input.
     * @param argument Most of the times a string argument for the settings.
     * @return A message to the player.
     */
    String adoptSettings(Object argument);
}
