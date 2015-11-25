package com.bluecatcode.jeff.agent;

import com.google.common.collect.ImmutableList;
import org.objectweb.asm.util.ASMifier;

public class CodeProviderTest {

    public static void main(String[] args) throws Exception {
        ImmutableList<Class<?>> classes = ImmutableList.of(
                CodeProvider.class
        );
        for (Class<?> aClass : classes) {
            System.out.println("----------------------------------------------------------------------------------------------------------------------------------");
            ASMifier.main(new String[] {
                    aClass.getCanonicalName()
            });
            System.out.println("----------------------------------------------------------------------------------------------------------------------------------");
        }
    }

}