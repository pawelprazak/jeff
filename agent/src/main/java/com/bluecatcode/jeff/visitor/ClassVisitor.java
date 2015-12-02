package com.bluecatcode.jeff.visitor;

import org.objectweb.asm.Opcodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassVisitor extends org.objectweb.asm.ClassVisitor {

    private static final Logger logger = LoggerFactory.getLogger(ClassVisitor.class);

    private String className;

    public ClassVisitor(org.objectweb.asm.ClassVisitor cv, String pClassName) {
        super(Opcodes.ASM5, cv);
        className = pClassName;
    }

    @Override
    public org.objectweb.asm.MethodVisitor visitMethod(int access, String methodName, String desc,
                                     String signature, String[] exceptions) {

        org.objectweb.asm.MethodVisitor mv = cv.visitMethod(access, methodName, desc, signature, exceptions);
        return new MethodVisitor(mv, methodName, className);
    }
}
