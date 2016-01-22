package com.github.smarttest.junit;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

/**
 * @author rodrigo.miguele
 * @since 19/01/16
 */
public class WarmupJUnitRunner extends BlockJUnit4ClassRunner {

    /**
     * Creates a BlockJUnit4ClassRunner to run {@code klass}
     *
     * @param klass
     * @throws InitializationError if the test class is malformed.
     */
    public WarmupJUnitRunner(Class<?> klass) throws InitializationError, ClassNotFoundException {
        super(TransformedClassJunitRetriever.getTransformedClass(klass));
    }

    @Override
    protected Statement withAfterClasses(Statement statement) {
        return new WarmupStatement(super.withAfterClasses(statement));
    }
}
