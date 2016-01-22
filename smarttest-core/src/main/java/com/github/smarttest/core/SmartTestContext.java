package com.github.smarttest.core;

import com.github.smarttest.core.persist.JSONTestPersister;
import com.github.smarttest.core.persist.SmartTestPersister;
import com.github.smarttest.core.retriever.JSONTestRetriever;
import com.github.smarttest.core.retriever.SmartTestRetriever;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author rodrigo.miguele
 * @since 19/01/16
 */
public class SmartTestContext {

    public static final String SMART_TEST_CLASSPATHS = "smart.test.classpaths";

    private Map<String, Set<String>> classes = new HashMap<String, Set<String>>();

    private String runningTest;

    private SmartTestPersister smartTestPersister = new JSONTestPersister();

    private SmartTestRetriever smartTestRetriever = new JSONTestRetriever();

    private static SmartTestContext INSTANCE = new SmartTestContext();

    private SmartTestContext() {
    }

    public static SmartTestContext get() {
        return INSTANCE;
    }

    public void register(String usedClass) {
        if (needToRegister(usedClass)) {
            String runningTestClass = runningTest;
            if (runningTestClass != null) {
                if (!classes.containsKey(usedClass)) {
                    classes.put(usedClass, new HashSet<String>());
                }
                classes.get(usedClass).add(runningTestClass);
            }
        }
    }

    private boolean needToRegister(String usedClass) {
        return !(usedClass.endsWith("Test") || usedClass.endsWith("TestCase") || (runningTest != null && usedClass.equals(runningTest)));
    }

    String getRunningTest() {
        return runningTest;
    }

    public SmartTestContext setRunningTest(String testClass) {
        runningTest = testClass;
        return this;
    }

    public void save() {
        this.smartTestPersister.save(classes);
    }

    public List<String> retrieve(Class<?> targetClass) {
        return new ArrayList<String>(this.smartTestRetriever.retrieveTestsByClass(targetClass));
    }

}
