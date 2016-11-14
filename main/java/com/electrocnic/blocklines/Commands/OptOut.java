package com.electrocnic.blocklines.Commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates, that a method is already implemented in the super class, thus, if the functionality is not wanted, it
 * MUST be overridden by the subclass.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface OptOut { }
