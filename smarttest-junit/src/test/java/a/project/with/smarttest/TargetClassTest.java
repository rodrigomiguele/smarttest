package a.project.with.smarttest;

import a.project.with.classes.AClass;
import a.project.with.classes.BClass;
import com.github.smarttest.junit.SmartTestRunner;
import com.github.smarttest.junit.SmartTestRunner.TargetClass;
import org.junit.runner.RunWith;

/**
 * @author rodrigo.miguele
 * @since 22/01/16
 */
@TargetClass({AClass.class, BClass.class})
@RunWith(SmartTestRunner.class)
public class TargetClassTest {

}
