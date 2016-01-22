package com.github.smarttest.core.persist;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * @author rodrigo.miguele
 * @since 22/01/16
 */
public class JSONTestPersister implements SmartTestPersister {

    public static final String JSON_PATH = System.getProperty("java.io.tmpdir") + "/smart-test.json";

    public void save(Map<String, Set<String>> classesAndTheirTests) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(new File(JSON_PATH), classesAndTheirTests);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
