package com.github.smarttest.core;

import com.github.smarttest.core.persist.SmartTestPersister;
import com.github.smarttest.core.targetClasses.MyUnusedClass;
import com.github.smarttest.core.targetClasses.MyUsedClass;
import org.junit.Assert;
import org.junit.Test;
import sun.misc.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by jteodoro on 01/02/16.
 */
public class WarmupClassTransformerTest {

    @Test
    public void runningTests() throws Exception {
        String usedClassName = MyUsedClass.class.getName();
        System.setProperty(SmartTestContext.SMART_TEST_CLASSPATHS, usedClassName);
        Assert.assertTrue(ClassTransformationChecker.shouldTransformClass(usedClassName));

        MyFakeSmartestPersister myFakePersister = new MyFakeSmartestPersister();
        SmartTestContext.get().setSmartTestPersister(myFakePersister);

        MyFakeClassLoader myFakeClassLoader = new MyFakeClassLoader();
        WarmupClassTransformer warmupClassTransformer = new WarmupClassTransformer();

        String unusedClassName = MyUnusedClass.class.getName();
        registerClass(unusedClassName, myFakeClassLoader, warmupClassTransformer);

        SmartTestContext.get().register(usedClassName);
        registerClass(usedClassName, myFakeClassLoader, warmupClassTransformer);

        SmartTestContext.get().setRunningTest(this.getClass().getName());

        Class<?> unusedClass = myFakeClassLoader.loadClass(unusedClassName);
        Object unusedObject = unusedClass.newInstance();
        Method pow = unusedClass.getMethod("pow");
        pow.invoke(unusedObject);
        SmartTestContext.get().save();
        Assert.assertTrue(myFakePersister.getResults().isEmpty());

        Class<?> usedClass = myFakeClassLoader.loadClass(usedClassName);
        Object usedObject = usedClass.newInstance();
        Method increment = usedClass.getMethod("increment");
        increment.invoke(usedObject);
        SmartTestContext.get().save();
        Assert.assertEquals(1, myFakePersister.getResults().size());
        String usedtest = myFakePersister.getResults().get(usedClassName).iterator().next();
        Assert.assertEquals(this.getClass().getName(), usedtest );

    }

    private void registerClass(String className, MyFakeClassLoader myFakeClassLoader, WarmupClassTransformer warmupClassTransformer) throws IOException, IllegalClassFormatException {
        InputStream usedClassContentStream = this.getClass().getClassLoader().getResourceAsStream(className+".class");
        byte[] classByteContent = IOUtils.readFully(usedClassContentStream, 0, false);
        byte[] transformedClass = warmupClassTransformer.transform(this.getClass().getClassLoader(), className, null, null, classByteContent);
        myFakeClassLoader.defineClass(className, transformedClass);
    }

    public static class MyFakeClassLoader extends ClassLoader {

        private Map<String, byte[]> definedClass = new HashMap<String, byte[]>();

        @Override
        protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
            final byte[] bytes = definedClass.get(name);
            if (bytes != null) {
                return defineClass(name, bytes, 0, bytes.length);
            }
            return super.loadClass(name, resolve);
        }

        public void defineClass(String className, byte[] content) {
            this.definedClass.put(className, content);
        }

    }

    public static class MyFakeSmartestPersister implements SmartTestPersister {

        Map<String, Set<String>> results = new HashMap<String, Set<String>>();

        public void save(Map<String, Set<String>> classesAndTheirTests) {
            results.putAll(classesAndTheirTests);
        }

        public Map<String, Set<String>> getResults() {
            return results;
        }
    }

}