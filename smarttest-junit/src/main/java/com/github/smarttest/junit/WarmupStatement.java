package com.github.smarttest.junit;

import com.github.smarttest.core.SmartTestContext;
import org.junit.runners.model.Statement;

/**
 * @author rodrigo.miguele
 * @since 20/01/16
 */
public class WarmupStatement extends Statement {

    private Statement parentStatement;

    public WarmupStatement(Statement parentStatement) {
        this.parentStatement = parentStatement;
    }

    @Override
    public void evaluate() throws Throwable {
        parentStatement.evaluate();
        SmartTestContext.get().save();
    }
}
