package com.github.smarttest.junit;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

/**
 * @author rodrigo.miguele
 * @since 19/01/16
 */
@Target(TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TestClasspaths {
    String[] value();
}
