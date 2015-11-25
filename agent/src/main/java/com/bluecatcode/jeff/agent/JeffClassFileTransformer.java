package com.bluecatcode.jeff.agent;

import com.google.common.collect.ImmutableSet;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Set;

public class JeffClassFileTransformer implements ClassFileTransformer {

    private static final Logger logger = LoggerFactory.getLogger(JeffClassFileTransformer.class);

    private Set<String> classNameBlacklist = ImmutableSet.of(
            "java/lang/ClassCircularityError",
            "java/lang/UnsupportedClassVersionError",
            "java/lang/ClassFormatError",
            "java/lang/VerifyError"
    );

    public byte[] transform(ClassLoader loader,
                            String className,
                            Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain,
                            byte[] originalBytecode) throws IllegalClassFormatException {
        if (!classNameBlacklist.contains(className)) {
            logger.info("visit class: {}, loader: {}", className, loader);

            ClassReader reader = new ClassReader(originalBytecode);
            ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_FRAMES);
            ClassVisitor visitor = new LogMethodClassVisitor(writer, className);
            reader.accept(visitor, 0);
            return writer.toByteArray();
        } else {
            logger.debug("ignore class: {}", className);
        }
        return originalBytecode;
    }
}
