package com.bluecatcode.jeff.agent;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogMethodClassVisitor extends ClassVisitor {

    private static final Logger logger = LoggerFactory.getLogger(LogMethodClassVisitor.class);

    private String className;

    public LogMethodClassVisitor(ClassVisitor cv, String pClassName) {
        super(Opcodes.ASM5, cv);
        className = pClassName;
    }

    @Override
    public MethodVisitor visitMethod(int access, String methodName, String desc,
                                     String signature, String[] exceptions) {
        logger.info("visit method: {}, class: {}", methodName, className);

        MethodVisitor mv = super.visitMethod(access, methodName, desc, signature, exceptions);
        return new PrintMessageMethodVisitor(mv, methodName, className);
    }
}