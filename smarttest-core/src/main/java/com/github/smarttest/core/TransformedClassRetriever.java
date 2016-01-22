package com.github.smarttest.core;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rodrigo.miguele
 * @since 20/01/16
 */
public class TransformedClassRetriever {

    public static Class<?> getTransformedClass(Class<?> originalClass) throws ClassNotFoundException {
        SmartTestContext.get().setRunningTest(originalClass.getName());
        return Class.forName(originalClass.getName(), true, WarmupClassLoader.get());
    }

    public static Class<?>[] getTransformedClasses(Class<?>[] classes) throws ClassNotFoundException {
        List<Class<?>> classList = new ArrayList<Class<?>>();
        for (Class<?> clazz : classes) {
            classList.add(getTransformedClass(clazz));
        }
        return classList.toArray(new Class<?>[classList.size()]);
    }

}
