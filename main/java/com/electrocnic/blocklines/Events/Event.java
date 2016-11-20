package com.electrocnic.blocklines.Events;

/**
 * Created by Andreas on 20.11.2016.
 */
public class Event<T> {

    public static final String MODE = "mode";
    public static final String ABORT = "abort";

    private String key = null;
    private T description = null;

    public Event(String key) {
        this.key = key;
    }

    public Event(String key, T description) {
        this.key = key;
        this.description = description;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public T getDescription() {
        return description;
    }

    public void setDescription(T description) {
        this.description = description;
    }
}
