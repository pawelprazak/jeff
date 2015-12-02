package com.bluecatcode.jeff.transformer;

import com.bluecatcode.jeff.visitor.ClassVisitor;
import com.bluecatcode.jeff.visitor.ExceptionVisitor;
import com.google.common.collect.ImmutableSet;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class JeffClassFileTransformer implements ClassFileTransformer {

    private static final Logger log = LoggerFactory.getLogger(JeffClassFileTransformer.class);

    private static final String JEFF_PACKAGE_PREFIX = "com/bluecatcode/jeff/";

    private Set<String> classNameBlacklist = ImmutableSet.of(
            JEFF_PACKAGE_PREFIX + ".*",
            "ch/qos/logback/.*",
            "org/apache/maven/.*",
            "org/junit/runners/.*",
            "com/intellij/*",
            "sun/.*",
            "sun/.*",
            "java/.*"
    );

    private List<String> transformedClasses = new ArrayList<String>();

    public byte[] transform(ClassLoader loader,
                            String className,
                            Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain,
                            byte[] originalBytecode) throws IllegalClassFormatException {

        // prevent multiple transformations
        if (transformedClasses.contains(className)) {
            return originalBytecode;
        } else {
            transformedClasses.add(className);
        }

        // no instrument agent classes
        if (className.startsWith(JEFF_PACKAGE_PREFIX)) {
            return originalBytecode;
        }

        // instrument all exceptions
        if (className.endsWith("Exception")) {
            ClassReader reader = new ClassReader(originalBytecode);
            ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_FRAMES);
            org.objectweb.asm.ClassVisitor visitor = new ExceptionVisitor(writer, className);
            reader.accept(visitor, 0);
            return writer.toByteArray();
        } else if (!isBlacklisted(className)) {
            // instrument all classes which are not blacklisted
            ClassReader reader = new ClassReader(originalBytecode);
            ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_FRAMES);
            org.objectweb.asm.ClassVisitor visitor = new ClassVisitor(writer, className);
            reader.accept(visitor, 0);
            return writer.toByteArray();
        }

        return originalBytecode;
    }

    private boolean isBlacklisted(String className) {
        for (String pattern : classNameBlacklist) {
            if (className.matches(pattern)) {
                return true;
            }
        }
        return false;
    }
}