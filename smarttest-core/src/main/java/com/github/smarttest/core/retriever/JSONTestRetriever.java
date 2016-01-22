package com.github.smarttest.core.retriever;

import com.github.smarttest.core.persist.JSONTestPersister;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author rodrigo.miguele
 * @since 22/01/16
 */
public class JSONTestRetriever implements SmartTestRetriever {

    public Collection<String> retrieveTestsByClass(Class<?> clazz) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            File jsonFile = new File(JSONTestPersister.JSON_PATH);
            Map<String, Collection<String>> classesAndTheirTests = mapper.readValue(jsonFile, HashMap.class);
            return classesAndTheirTests.get(clazz.getName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
