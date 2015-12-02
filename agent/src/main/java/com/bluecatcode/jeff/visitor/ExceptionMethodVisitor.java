package com.bluecatcode.jeff.visitor;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionMethodVisitor extends MethodVisitor implements Opcodes {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionMethodVisitor.class);

    private final String methodName;
    private final String className;

    public ExceptionMethodVisitor(MethodVisitor mv, String methodName, String className) {
        super(ASM5, mv);
        this.methodName = methodName;
        this.className = className;
    }

    @Override
    public void visitCode() {
        if (logger.isDebugEnabled()) {
            logger.debug("visit code, method: {}, class: {}", methodName, className);
        }

        // inject exception touch point
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Thread", "currentThread", "()Ljava/lang/Thread;", false);
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "identityHashCode", "(Ljava/lang/Object;)I", false);
        mv.visitInsn(I2L);
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;", false);
        mv.visitLdcInsn(className);
        mv.visitLdcInsn(methodName);
        mv.visitMethodInsn(INVOKESTATIC, "com/bluecatcode/jeff/touch/CodeProvider", "dumpException", "(Ljava/lang/Long;Ljava/lang/String;Ljava/lang/String;)V", false);
    }
}
