package com.github.smarttest.junit;

import com.github.smarttest.core.SmartTestContext;
import com.github.smarttest.core.TransformedClassRetriever;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rodrigo.miguele
 * @since 21/01/16
 */
class TransformedClassJunitRetriever {

    public static Class<?> getTransformedClass(Class<?> originalClass) throws ClassNotFoundException {
        if (System.getProperty(SmartTestContext.SMART_TEST_CLASSPATHS) == null && originalClass.isAnnotationPresent(TestClasspaths.class)) {
            TestClasspaths annotation = originalClass.getAnnotation(TestClasspaths.class);
            System.setProperty(SmartTestContext.SMART_TEST_CLASSPATHS, StringUtils.join(annotation.value(), ","));
        }
        return TransformedClassRetriever.getTransformedClass(originalClass);
    }

    public static Class<?>[] getTransformedClasses(Class<?>[] classes) throws ClassNotFoundException {
        List<Class<?>> classList = new ArrayList<Class<?>>();
        for (Class<?> clazz : classes) {
            classList.add(getTransformedClass(clazz));
        }
        return classList.toArray(new Class<?>[classList.size()]);
    }
}
