package com.bluecatcode.jeff.agent;

import com.google.common.collect.ImmutableSet;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Set;

public class JeffClassFileTransformer implements ClassFileTransformer {

    private static final Logger logger = LoggerFactory.getLogger(JeffClassFileTransformer.class);

    private Set<String> classNameBlacklist = ImmutableSet.of(
            "com/bluecatcode/jeff/.*",
            "org/apache/maven/.*",
            "org/junit/runners/.*",
            "com/intellij/*",
            "sun/.*",
            "sun/.*",
            "java/.*"
    );

    public byte[] transform(ClassLoader loader,
                            String className,
                            Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain,
                            byte[] originalBytecode) throws IllegalClassFormatException {
        if (isBlacklisted(className)) {
//            logger.debug("ignore class: {}", className);
            return originalBytecode;
        }

//        logger.info("visit class: {}, loader: {}", className, loader);
        ClassReader reader = new ClassReader(originalBytecode);
        ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_FRAMES);
        ClassVisitor visitor = new LogMethodClassVisitor(writer, className);
        reader.accept(visitor, 0);
        return writer.toByteArray();
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
