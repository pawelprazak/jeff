package com.bluecatcode.jeff.agent;

import com.bluecatcode.common.io.CloseableReference;
import com.bluecatcode.jeff.notifier.*;
import com.bluecatcode.jeff.transformer.JeffClassFileTransformer;
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
    private static JeffEventNotifier notifier;

    /**
     * JVM hook to dynamically load javaagent at runtime.
     * <p>
     * The agent class may have an agentmain method for use when the agent is
     * started after VM startup.
     *
     * @param args command line, coma-separated arguments
     * @param inst the instrumentation
     * @throws Exception
     */
    public static void agentmain(String args, Instrumentation inst) throws Exception {
        premain(args, inst);
    }

    /**
     * JVM hook to statically load the javaagent at startup.
     * <p>
     * After the Java Virtual Machine (JVM) has initialized, the premain method
     * will be called. Then the real application main method will be called.
     *
     * @param args command line, coma-separated arguments
     * @param inst the instrumentation
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


        if (logger.isDebugEnabled()) {
            logger.debug("premain method invoked with args: '{}' and inst: '{}'", args, inst);
        }

        // intercept SIGTERM signal
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                if (logger.isDebugEnabled()) {
                    logger.debug("Handling Shutdown jvm...");
                }
                Event event = new Event("JeffAgent", "premain", EventType.STOP);
                notifier.notifyEvent(event);
            }
        });

        instrumentation = inst;
        instrumentation.addTransformer(new JeffClassFileTransformer(), true);
        // trigger instrumentation all standard java exceptions
        instrumentation.retransformClasses(exceptions());

        // create notifier and notify fuzzer that agent is ready
        notifier = new SystemOutJeffEventNotifier();
        Event event = new Event("JeffAgent", "premain", EventType.START);
        notifier.notifyEvent(event);
    }

    private static Class[] exceptions() {
        return new Class[]{
                /* Unchecked RuntimeException */
                ArithmeticException.class,
                ArrayIndexOutOfBoundsException.class,
                ArrayStoreException.class,
                ClassCastException.class,
                IllegalArgumentException.class,
                IllegalMonitorStateException.class,
                IllegalStateException.class,
                IllegalThreadStateException.class,
                IndexOutOfBoundsException.class,
                NegativeArraySizeException.class,
                NullPointerException.class,
                NumberFormatException.class,
                SecurityException.class,
                StringIndexOutOfBoundsException.class,
                UnsupportedOperationException.class,

                /* Checked Exceptions */
//                ClassNotFoundException.class, // FIXME don't know why multiple occurrences on jetty..
                CloneNotSupportedException.class,
                IllegalAccessException.class,
                InstantiationException.class,
                InterruptedException.class,
                NoSuchFieldException.class,
                NoSuchMethodException.class
        };
    }

    public static JeffEventNotifier getNotifier() {
        return notifier;
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