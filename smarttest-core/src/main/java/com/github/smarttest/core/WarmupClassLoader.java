package com.github.smarttest.core;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author rodrigo.miguele
 * @since 19/01/16
 */
public class WarmupClassLoader extends ClassLoader {

    private static final WarmupClassLoader INSTANCE = new WarmupClassLoader();

    private WarmupClassLoader() {
        super(Thread.currentThread().getContextClassLoader());
    }

    public static WarmupClassLoader get() {
        return INSTANCE;
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        ClassLoader parentClassLoader = getParent();
        WarmupClassTransformer classTransformer = new WarmupClassTransformer();

        Class c = findLoadedClass(name);
        if (c != null) {
            return c;
        }

        InputStream is = this.getResourceAsStream(name.replace('.', '/') + ".class");
        if (is == null) {
            throw new ClassNotFoundException(name + " not found");
        }

        try {
            byte[] originalBytecode = readByteCode(is);
            byte[] transformedBytecode = classTransformer.transform(parentClassLoader, name, null, null, originalBytecode);
            if (originalBytecode == transformedBytecode) {
                return parentClassLoader.loadClass(name);
            }
            return defineClass(name, transformedBytecode, 0, transformedBytecode.length);
        } catch (Throwable t) {
            throw new ClassNotFoundException(name + " not found", t);
        }
    }

    private static byte[] readByteCode(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            throw new IOException("null input stream");
        }

        byte[] buffer = new byte[409600];
        byte[] classBytes = new byte[0];
        int r;
        try {
            r = inputStream.read(buffer);
            while (r >= buffer.length) {
                byte[] temp = new byte[classBytes.length + buffer.length];
                System.arraycopy(classBytes, 0, temp, 0, classBytes.length);
                System.arraycopy(buffer, 0, temp, classBytes.length, buffer.length);
                classBytes = temp;
            }
            if (r != -1) {
                byte[] temp = new byte[classBytes.length + r];
                System.arraycopy(classBytes, 0, temp, 0, classBytes.length);
                System.arraycopy(buffer, 0, temp, classBytes.length, r);
                classBytes = temp;
            }
        } finally {
            try {
                inputStream.close();
            } catch (IOException ignore) {
                // intentionally empty
            }
        }
        return classBytes;
    }
}
