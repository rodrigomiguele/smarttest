package com.github.smarttest.core.persist;

import java.util.Map;
import java.util.Set;

/**
 * @author rodrigo.miguele
 * @since 20/01/16
 */
public interface SmartTestPersister {

    void save(Map<String, Set<String>> classesAndTheirTests);

}
