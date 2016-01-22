package com.github.smarttest.core;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * @author rodrigo.miguele
 * @since 19/01/16
 */
public class WarmupClassTransformer implements ClassFileTransformer {

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer)
            throws IllegalClassFormatException {
        byte[] byteCode = classfileBuffer;
        String usedClassName = className.replace("/", ".");
        if (ClassTransformationChecker.shouldTransformClass(usedClassName)) {
            try {
                ClassPool cp = ClassPool.getDefault();
                CtClass cc = cp.get(usedClassName);
                CtConstructor[] constructors = cc.getConstructors();
                for (CtConstructor constructor : constructors) {
                    String runningTest = SmartTestContext.get().getRunningTest();
                    if (runningTest == null || !className.equals(runningTest)) {
                        constructor.insertAfter("com.github.smarttest.core.SmartTestContext.get().register(\"" + usedClassName + "\");");
                    }
                }
                byteCode = cc.toBytecode();
                cc.detach();
                return byteCode;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return byteCode;
    }
}
