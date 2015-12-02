package com.bluecatcode.jeff.visitor;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionVisitor extends org.objectweb.asm.ClassVisitor {

    private static final Logger log = LoggerFactory.getLogger(ExceptionVisitor.class);

    private String className;

    public ExceptionVisitor(org.objectweb.asm.ClassVisitor classVisitor, String className) {
        super(Opcodes.ASM5, classVisitor);
        this.className = className;
    }

    @Override
    public MethodVisitor visitMethod(int access, String methodName, String desc,
                                     String signature, String[] exceptions) {

        MethodVisitor mv = cv.visitMethod(access, methodName, desc, signature, exceptions);
        return new ExceptionMethodVisitor(mv, methodName, className);
    }

}
