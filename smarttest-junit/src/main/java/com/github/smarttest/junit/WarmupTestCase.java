package com.github.smarttest.junit;

import com.github.smarttest.core.SmartTestContext;
import junit.framework.TestCase;
import junit.framework.TestResult;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author rodrigo.miguele
 * @since 20/01/16
 */
public class WarmupTestCase extends TestCase {

    private String fName;

    public WarmupTestCase() {
        super();
    }

    public WarmupTestCase(String name) {
        super(name);
        this.fName = name;
    }

    @Override
    public void run(TestResult result) {
        super.run(result);
    }

    @Override
    protected void runTest() throws Throwable {
        TestCase.assertNotNull("TestCase.fName cannot be null", fName); // Some VMs crash when calling getMethod(null,null);
        Method runMethod = null;
        TestCase thiz = (TestCase) TransformedClassJunitRetriever.getTransformedClass(getClass()).newInstance();
        thiz.setName(fName);
        try {
            // use getMethod to get all public inherited
            // methods. getDeclaredMethods returns all
            // methods of this class but excludes the
            // inherited ones.
            runMethod = thiz.getClass().getMethod(fName, (Class[]) null);
        } catch (NoSuchMethodException e) {
            TestCase.fail("Method \"" + fName + "\" not found");
        }
        if (!Modifier.isPublic(runMethod.getModifiers())) {
            TestCase.fail("Method \"" + fName + "\" should be public");
        }

        try {
            runMethod.invoke(thiz);
        } catch (InvocationTargetException e) {
            e.fillInStackTrace();
            throw e.getTargetException();
        } catch (IllegalAccessException e) {
            e.fillInStackTrace();
            throw e;
        }
    }

    @Override
    public void setName(String name) {
        super.setName(name);
        this.fName = name;
    }

    @Override
    protected final void tearDown() throws Exception {
        SmartTestContext.get().save();
        doTearDown();
    }

    protected void doTearDown() {

    }
}
