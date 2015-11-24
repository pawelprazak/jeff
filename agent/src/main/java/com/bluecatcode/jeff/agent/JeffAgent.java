package com.bluecatcode.jeff.agent;

import com.bluecatcode.common.io.CloseableReference;
import com.bluecatcode.common.io.Closeables;
import com.google.common.base.Joiner;
import com.sun.tools.attach.VirtualMachine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;
import java.lang.management.ManagementFactory;

import static com.bluecatcode.common.io.Closeables.closeableFrom;

public class JeffAgent {

    private static final Logger logger = LoggerFactory.getLogger(JeffAgent.class);

    public static boolean firstTime = true;

    private static Instrumentation instrumentation;

    /**
     * JVM hook to statically load the javaagent at startup.
     * 
     * After the Java Virtual Machine (JVM) has initialized, the premain method
     * will be called. Then the real application main method will be called.
     * 
     * @param args
     * @param inst
     * @throws Exception
     */
    public static void premain(String args, Instrumentation inst) throws Exception {
        // guard against the agent being loaded twice
        synchronized (JeffAgent.class) {
            if (firstTime) {
                firstTime = false;
            } else {
                throw new Exception("Main : attempting to load Byteman agent more than once");
            }
        }

        if (args != null) {
            // TODO
        }

        logger.info("premain method invoked with args: '{}' and inst: '{}'", args, inst);
        instrumentation = inst;
        instrumentation.addTransformer(new JeffClassFileTransformer(), true);
        for (Class aClass : instrumentation.getAllLoadedClasses()) {
//            logger.info("{}", aClass);
        }
    }

    /**
     * JVM hook to dynamically load javaagent at runtime.
     * 
     * The agent class may have an agentmain method for use when the agent is
     * started after VM startup.
     * 
     * @param args
     * @param inst
     * @throws Exception
     */
    public static void agentmain(String args, Instrumentation inst) throws Exception {
        premain(args, inst);
    }

    /**
     * Programmatic hook to dynamically load javaagent at runtime.
     */
    public static void initialize() {
        if (instrumentation == null) {
            loadAgent();
        } else {
            throw new IllegalStateException("Instrumentation is not null");
        }
    }

    private static void loadAgent() {
        String nameOfRunningVM = ManagementFactory.getRuntimeMXBean().getName();
        int p = nameOfRunningVM.indexOf('@');
        String pid = nameOfRunningVM.substring(0, p);

        logger.info("dynamically loading javaagent to JVM process: {}", nameOfRunningVM);
        try (CloseableReference<VirtualMachine> vm = closeableFrom(VirtualMachine.attach(pid), VirtualMachine::detach)) {
            String jarFilePath = locateAgent();
            vm.get().loadAgent(jarFilePath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String locateAgent() {
        return "/home/pawel/.m2/repository/com/bluecatcode/jeff-agent/1.0-SNAPSHOT/jeff-agent-1.0-SNAPSHOT-jar-with-dependencies.jar";
    }

}