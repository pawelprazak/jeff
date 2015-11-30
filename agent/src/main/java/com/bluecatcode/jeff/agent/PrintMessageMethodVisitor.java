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

        systemout("Call: " + className + "#" + methodName);
        sysoutcount();
    }

    private void systemout(String msg) {
        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitLdcInsn(msg);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
    }

    private void sysoutcount() {
        mv.visitMethodInsn(INVOKESTATIC, "com/bluecatcode/jeff/agent/Counter", "newNotifier", "()Lcom/bluecatcode/jeff/agent/Counter$Notifier;", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "com/bluecatcode/jeff/agent/Counter$Notifier", "send", "()V", false);
        mv.visitFieldInsn(GETSTATIC, "com/bluecatcode/jeff/agent/Counter", "INSTANCE", "Lcom/bluecatcode/jeff/agent/Counter;");
        mv.visitMethodInsn(INVOKEVIRTUAL, "com/bluecatcode/jeff/agent/Counter", "count", "()I", false);
        mv.visitVarInsn(ISTORE, 1);
        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
        mv.visitInsn(DUP);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
        mv.visitLdcInsn("Call: ");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
        mv.visitVarInsn(ILOAD, 1);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(I)Ljava/lang/StringBuilder;", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

    }
}