package com.github.smarttest.core.retriever;

import java.util.Collection;

/**
 * @author rodrigo.miguele
 * @since 22/01/16
 */
public interface SmartTestRetriever {

    Collection<String> retrieveTestsByClass(Class<?> clazz);

}
