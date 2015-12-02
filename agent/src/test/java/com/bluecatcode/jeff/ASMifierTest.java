package com.bluecatcode.jeff;

import org.junit.Test;
import org.objectweb.asm.util.ASMifier;

public class ASMifierTest {

    @Test
    public void generateASMCode() throws Exception {
        ASMifier.main(new String[] {
                HelloWorld.class.getName()
        });
    }

    static class HelloWorld {
        public void sayHello() {
            System.out.printf("Hello world");
        }
    }

}
