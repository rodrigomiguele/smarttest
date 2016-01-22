package a.project.with.testsuite;

import a.project.with.junit3.tests.ANewTest;
import a.project.with.junit4.tests.AnOldTest;
import com.github.smarttest.junit.TestClasspaths;
import com.github.smarttest.junit.WarmupSuiteRunner;
import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.junit.runner.RunWith;

/**
 * @author rodrigo.miguele
 * @since 21/01/16
 */
@TestClasspaths("a.project")
@RunWith(WarmupSuiteRunner.class)
public class ATestSuite {

    public static Test suite() {
        TestSuite testSuite = new TestSuite();
        testSuite.addTestSuite(ANewTest.class);
        testSuite.addTest(new JUnit4TestAdapter(AnOldTest.class));
        return testSuite;
    }
}
