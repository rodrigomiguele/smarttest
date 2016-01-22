package com.github.smarttest.agent;

import com.github.smarttest.core.WarmupClassTransformer;

import java.lang.instrument.Instrumentation;

/**
 * @author rodrigo.miguele
 * @since 21/01/16
 */
public class WarmupAgent {

    public static void premain(String agentArgs, Instrumentation inst) {
        inst.addTransformer(new JunitClassFileTransformer());
        inst.addTransformer(new WarmupClassTransformer());
    }

}
