package com.github.smarttest.agent;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.NotFoundException;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * @author rodrigo.miguele
 * @since 22/01/16
 */
public class JunitClassFileTransformer implements ClassFileTransformer {

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        byte[] byteCode = classfileBuffer;
        try {
            String usedClassName = className.replace("/", ".");
            if (usedClassName.equals("org.junit.runner.Description")) {
                byteCode = transformDescription(usedClassName);
            } else if (usedClassName.equals("junit.framework.TestCase")) {
                byteCode = transformTestCase(usedClassName);
            } else if (usedClassName.equals("org.junit.internal.runners.statements.RunAfters")) {
                byteCode = transformRunAfters(usedClassName);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return byteCode;
    }

    private byte[] transformRunAfters(String usedClassName) throws NotFoundException, CannotCompileException, IOException {
        byte[] byteCode;
        ClassPool cp = ClassPool.getDefault();
        CtClass cc = cp.get(usedClassName);
        CtMethod method = cc.getDeclaredMethod("evaluate");
        method.insertAfter("com.github.smarttest.core.SmartTestContext.get().save();");
        byteCode = cc.toBytecode();
        cc.detach();
        return byteCode;
    }

    private byte[] transformTestCase(String usedClassName) throws NotFoundException, CannotCompileException, IOException {
        byte[] byteCode;
        ClassPool cp = ClassPool.getDefault();
        CtClass cc = cp.get(usedClassName);
        CtConstructor[] constructors = cc.getConstructors();
        for (CtConstructor constructor : constructors) {
            constructor.insertAfter("com.github.smarttest.core.SmartTestContext.get().setRunningTest(this.getClass().getName());");
        }

        CtMethod method = cc.getDeclaredMethod("tearDown");
        method.insertAfter("com.github.smarttest.core.SmartTestContext.get().save();");

        byteCode = cc.toBytecode();
        cc.detach();
        return byteCode;
    }

    private byte[] transformDescription(String usedClassName) throws NotFoundException, CannotCompileException, IOException {
        byte[] byteCode;
        ClassPool cp = ClassPool.getDefault();
        CtClass cc = cp.get(usedClassName);
        CtConstructor[] constructors = cc.getConstructors();
        for (CtConstructor constructor : constructors) {
            constructor.insertAfter(
                    "if(clazz != null){" +
                            "   com.github.smarttest.core.SmartTestContext.get().setRunningTest(clazz.getName());" +
                            "} else {" +
                            "   com.github.smarttest.core.SmartTestContext.get().setRunningTest(displayName);" +
                            "}");
        }
        byteCode = cc.toBytecode();
        cc.detach();
        return byteCode;
    }
}
