package com.github.smarttest.core.persist;

import java.util.Map;
import java.util.Set;

/**
 * @author rodrigo.miguele
 * @since 21/01/16
 */
public class PrintOnConsoleSmartTestPersister implements SmartTestPersister {

    public void save(Map<String, Set<String>> classesAndTheirTests) {
        for (Map.Entry<String, Set<String>> classAndTheirTests : classesAndTheirTests.entrySet()) {
            for (String test : classAndTheirTests.getValue()) {
                System.err.println(classAndTheirTests.getKey() + " -> " + test);
            }
        }
    }
}
