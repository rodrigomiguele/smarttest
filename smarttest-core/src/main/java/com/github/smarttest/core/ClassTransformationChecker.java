package com.github.smarttest.core;

/**
 * @author rodrigo.miguele
 * @since 20/01/16
 */
public class ClassTransformationChecker {

    public static boolean shouldTransformClass(String name) {
        String property = System.getProperty(SmartTestContext.SMART_TEST_CLASSPATHS);
        boolean shouldTransform = false;
        if (property != null) {
            String[] classpaths = property.split(",");
            for (String classpath : classpaths) {
                if (classpath.startsWith("java.")) {
                    throw new IllegalArgumentException("Cannot transform \"java.\" classes!");
                }
                if (name.startsWith(classpath)) {
                    shouldTransform = true;
                    break;
                }
            }
        }
        return shouldTransform;
    }
}
