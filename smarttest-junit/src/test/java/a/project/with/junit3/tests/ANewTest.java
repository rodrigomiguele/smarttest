package a.project.with.junit3.tests;

import a.project.with.classes.AClass;
import a.project.with.classes.BClass;
import com.github.smarttest.junit.TestClasspaths;
import com.github.smarttest.junit.WarmupTestCase;

/**
 * @author rodrigo.miguele
 * @since 21/01/16
 */
@TestClasspaths("a.project")
public class ANewTest extends WarmupTestCase {

    public void test(){
        new AClass();
    }

    public void testAnother(){
        new BClass();
    }
}
