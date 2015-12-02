package com.bluecatcode.jeff.visitor;

import org.objectweb.asm.Opcodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassVisitor extends org.objectweb.asm.ClassVisitor {

    private static final Logger logger = LoggerFactory.getLogger(ClassVisitor.class);

    private String className;

    public ClassVisitor(org.objectweb.asm.ClassVisitor classVisitor, String className) {
        super(Opcodes.ASM5, classVisitor);
        this.className = className;
    }

    @Override
    public org.objectweb.asm.MethodVisitor visitMethod(int access, String methodName, String descriptor,
                                     String signature, String[] exceptions) {

        org.objectweb.asm.MethodVisitor mv = cv.visitMethod(access, methodName, descriptor, signature, exceptions);
        return new MethodVisitor(mv, methodName, className);
    }
}
