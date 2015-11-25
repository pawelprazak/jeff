package com.bluecatcode.jeff.agent;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrintMessageMethodVisitor extends MethodVisitor {

    private static final Logger logger = LoggerFactory.getLogger(PrintMessageMethodVisitor.class);

    private final String methodName;
    private final String className;
    private boolean isAnnotationPresent;

    public PrintMessageMethodVisitor(MethodVisitor mv, String methodName, String className) {
        super(Opcodes.ASM5, mv);
        this.methodName = methodName;
        this.className = className;
    }

    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        logger.info("visit annotation: {}, method: {}, class: {}", desc, methodName, className);

//        if ("Lcom/example/spring2gx/mains/ImportantLog;".equals(desc)) {
//            isAnnotationPresent = true;
//            return new AnnotationVisitor(Opcodes.ASM5, super.visitAnnotation(desc, visible)) {
//                public AnnotationVisitor visitArray(String name, Object value) {
//                    if ("fields".equals(name)) {
//                        return new AnnotationVisitor(Opcodes.ASM5, super.visitArray(name)) {
//                            public void visit(String name, Object value) {
//                                parameterIndexes.add((String) value);
//                                super.visit(name, value);
//                            }
//                        };
//                    } else {
//                        return super.visitArray(name);
//                    }
//                }
//            };
//        }
        return super.visitAnnotation(desc, visible);
    }

    @Override
    public void visitCode() {
        logger.info("visit code, method: {}, class: {}", methodName, className);


//        if (isAnnotationPresent) {
            // create string builder
            mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitTypeInsn(Opcodes.NEW, "java/lang/StringBuilder");
            mv.visitInsn(Opcodes.DUP);
            // add everything to the string builder
            mv.visitLdcInsn("A call was made to method \"");
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/StringBuilder", "", "(Ljava/lang/String;)V", false);
            mv.visitLdcInsn(methodName);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
                    "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
//            mv.visitMethodInsn();
//        }
    }
}