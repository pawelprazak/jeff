package com.bluecatcode.jeff.agent;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;

public class JeffClassVisitor extends ClassVisitor {

    private static final Logger logger = LoggerFactory.getLogger(JeffClassVisitor.class);

    /**
     * We assign 'unique event identifiers' to every asm instruction or directive found in the file. Using the identifiers
     * we are able to distinguish if the instruction is the same as found in the other pass of instrumentation.
     * <p/>
     * We will use this 'generator' to provide this identifiers. Remember to acquire identifiers using {@link AtomicInteger#incrementAndGet()} (not {@link AtomicInteger#getAndIncrement()}!!!)
     */
    private AtomicInteger eventIdGenerator = new AtomicInteger(0);

    private Map<String, String> classGraph;

    private Set<String> methods;

    public JeffClassVisitor(int api) {
        super(api);
        this.classGraph = newHashMap();
        this.methods = newHashSet();
    }

    public JeffClassVisitor(int api, ClassVisitor cv) {
        super(api, cv);
        this.classGraph = newHashMap();
        this.methods = newHashSet();
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        methods.add(name);
        return super.visitMethod(access, name, desc, signature, exceptions);
    }

    public Set<String> getMethods() {
        return methods;
    }

}
