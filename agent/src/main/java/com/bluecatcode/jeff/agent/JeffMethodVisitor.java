package com.bluecatcode.jeff.agent;

import org.objectweb.asm.MethodVisitor;

public class JeffMethodVisitor extends MethodVisitor {

    public JeffMethodVisitor(int api) {
        super(api);
    }

    public JeffMethodVisitor(int api, MethodVisitor mv) {
        super(api, mv);
    }

}
