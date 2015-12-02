package com.bluecatcode.jeff.visitor;

import org.objectweb.asm.Opcodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MethodVisitor extends org.objectweb.asm.MethodVisitor implements Opcodes {

    private static final Logger logger = LoggerFactory.getLogger(MethodVisitor.class);

    private final String methodName;
    private final String className;

    public MethodVisitor(org.objectweb.asm.MethodVisitor mv, String methodName, String className) {
        super(ASM5, mv);
        this.methodName = methodName;
        this.className = className;
    }

    @Override
    public void visitCode() {
        if (logger.isDebugEnabled()) {
            logger.debug("visit code, method: {}, class: {}", methodName, className);
        }

        // inject method touch point
        mv.visitLdcInsn(className);
        mv.visitLdcInsn(methodName);
        mv.visitMethodInsn(INVOKESTATIC, "com/bluecatcode/jeff/touch/CodeProvider", "dumpTouchPoint", "(Ljava/lang/String;Ljava/lang/String;)V", false);
    }
}