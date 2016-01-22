package a.project.with.junit4.tests;

import a.project.with.classes.AClass;
import a.project.with.classes.BClass;
import com.github.smarttest.junit.TestClasspaths;
import com.github.smarttest.junit.WarmupJUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author rodrigo.miguele
 * @since 21/01/16
 */
@TestClasspaths("a.project")
@RunWith(WarmupJUnitRunner.class)
public class AnOldTest {

    @Test
    public void test(){
        new AClass();
    }

    @Test
    public void anotherTest(){
        new BClass();
    }

}
