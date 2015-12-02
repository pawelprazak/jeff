package com.bluecatcode.jeff;

import com.google.common.collect.ImmutableList;
import org.objectweb.asm.util.ASMifier;

public class ASMifierTest {

    static class HelloWorld {
        public void sayHello() {
            System.out.printf("Hello world");
        }
    }

    public static void main(String[] args) throws Exception {
        ImmutableList<Class<?>> classes = ImmutableList.of(
                HelloWorld.class
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
