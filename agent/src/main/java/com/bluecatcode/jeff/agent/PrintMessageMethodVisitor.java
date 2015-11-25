package com.bluecatcode.jeff.agent;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrintMessageMethodVisitor extends MethodVisitor implements Opcodes {

    private static final Logger logger = LoggerFactory.getLogger(PrintMessageMethodVisitor.class);

    private final String methodName;
    private final String className;
    private boolean isAnnotationPresent;

    public PrintMessageMethodVisitor(MethodVisitor mv, String methodName, String className) {
        super(ASM5, mv);
        this.methodName = methodName;
        this.className = className;
    }

    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        logger.info("visit annotation: {}, method: {}, class: {}", desc, methodName, className);
        return super.visitAnnotation(desc, visible);
    }

    @Override
    public void visitCode() {
        logger.info("visit code, method: {}, class: {}", methodName, className);

        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitLdcInsn("Call: " + className + "#" + methodName);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
    }
}