package com.github.smarttest.junit;

import com.github.smarttest.core.SmartTestContext;
import junit.framework.TestListener;
import org.junit.internal.runners.SuiteMethod;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;

/**
 * @author rodrigo.miguele
 * @since 20/01/16
 */
public class WarmupSuiteRunner extends SuiteMethod {

    public WarmupSuiteRunner(Class<?> klass) throws Throwable {
        super(TransformedClassJunitRetriever.getTransformedClass(klass));
    }

    @Override
    public TestListener createAdaptingListener(RunNotifier notifier) {
        notifier.addListener(new RunListener() {
            @Override
            public void testStarted(Description description) throws Exception {
                Class<?> testClass = description.getTestClass();
                if (testClass != null) {
                    SmartTestContext.get().setRunningTest(testClass.getName());
                }
                super.testStarted(description);
            }

            @Override
            public void testRunFinished(Result result) throws Exception {
                super.testRunFinished(result);
                SmartTestContext.get().save();
            }
        });
        return super.createAdaptingListener(notifier);
    }

}
