package com.github.smarttest.junit;

import com.github.smarttest.core.SmartTestContext;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author rodrigo.miguele
 * @since 22/01/16
 */
public class SmartTestRunner extends Suite {

    public SmartTestRunner(Class<?> klass) throws InitializationError, ClassNotFoundException {
        super(klass, getTestsClasses(klass));
    }

    private static Class<?>[] getTestsClasses(Class<?> klass) throws InitializationError, ClassNotFoundException {
        TargetClass annotation = klass.getAnnotation(TargetClass.class);
        if (annotation == null) {
            throw new InitializationError(String.format("class '%s' must have a TargetClass annotation", klass.getName()));
        }
        Class<?>[] value = annotation.value();
        Set<Class<?>> testsClasses = new HashSet<Class<?>>();
        for (Class<?> targetClass : value) {
            List<String> classNameList = SmartTestContext.get().retrieve(targetClass);
            for (String className : classNameList) {
                testsClasses.add(Class.forName(className));
            }
        }
        return testsClasses.toArray(new Class[testsClasses.size()]);
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Inherited
    public @interface TargetClass {

        Class<?>[] value();

    }
}
