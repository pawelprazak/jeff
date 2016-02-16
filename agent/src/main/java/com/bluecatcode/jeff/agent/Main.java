package com.bluecatcode.jeff.agent;

import com.bluecatcode.common.io.CloseableReference;
import com.bluecatcode.common.io.Resources;
import com.bluecatcode.core.Architecture;
import com.bluecatcode.core.Platform;
import com.google.common.base.Joiner;
import com.simontuffs.onejar.Boot;
import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.VirtualMachine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.Instrumentation;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

import static com.bluecatcode.common.io.Closeables.closeableFrom;
import static java.lang.String.format;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static boolean firstTime = true;

    public static String JEFF_AGENT_LIBRARY_NAME = "jeff-native-agent";

    private static Instrumentation instrumentation;

    public static void main(String[] args) {
        log.debug("main was invoked");

        checkPlatform();
        checkArchitecture();
        loadNativeAgent();
    }

    /**
     * JVM hook to statically load the javaagent at startup.
     * <p>
     * After the Java Virtual Machine (JVM) has initialized, the premain method
     * will be called. Then the real application main method will be called.
     *
     * @param args command line, coma-separated arguments
     * @param inst the instrumentation instance
     * @throws Exception
     */
    public static void premain(String args, Instrumentation inst) throws Exception {
        log.debug(format("agent main method invoked with args: %s and inst: %s", args, inst));

        // guard against the agent being loaded twice
        synchronized (JeffAgent.class) {
            if (firstTime) {
                firstTime = false;
            } else {
                throw new Exception("Main : attempting to load agent more than once");
            }
        }

        // intercept SIGTERM signal
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                log.debug("Handling Shutdown jvm...");
            }
        });

        // Set the main class for the Boot to invoke
        System.setProperty(Boot.P_MAIN_CLASS, Main.class.getCanonicalName());
        Boot.run(new String[0]);

        instrumentation = inst;
    }

    /**
     * JVM hook to dynamically load javaagent at runtime.
     * <p>
     * The agent class may have an agentmain method for use when the agent is
     * started after VM startup.
     *
     * @param args command line, coma-separated arguments
     * @param inst the instrumentation instance
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
            loadJavaAgent();
        } else {
            throw new IllegalStateException("Instrumentation is not null");
        }
    }

    private static void checkPlatform() {
        Platform platform = Platform.CURRENT;
        log.debug(format("Current platform: %s", platform));
        switch (platform) {
            case LINUX:
                break;
            case UNIX:
            case OSX:
            case WINDOWS:
                throw new UnsupportedOperationException(format("Platform '%s' not supported", platform.toString()));
        }
    }

    private static void checkArchitecture() {
        Architecture architecture = Architecture.CURRENT;
        log.debug(format("Current architecture: %s", architecture));
        switch (architecture) {
            case X64:
                break;
            case X32:
            case IA64:
                throw new UnsupportedOperationException(format("Architecture '%s' not supported", architecture.toString()));
        }

    }

    private static void loadJavaAgent() {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        String pid = name.substring(0, name.indexOf('@'));

        log.debug(format("dynamically loading java agent to JVM process: %s", name));
        try (CloseableReference<VirtualMachine> vm = closeableFrom(VirtualMachine.attach(pid), VirtualMachine::detach)) {
            String jarFilePath = locateJavaAgent();
            VirtualMachine virtualMachine = vm.get();
            virtualMachine.loadAgent(jarFilePath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String locateJavaAgent() {
        String property = getProperties().getProperty("project.highest-basedir");
        String path = property + "/agent/target/jeff-agent-1.0-SNAPSHOT-jar-with-dependencies.jar";
        log.debug(format("Loading agent from: %s", path));
        return path;
    }

    public static void loadNativeAgent() {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        String pid = name.substring(0, name.indexOf('@'));

        log.debug(format("dynamically loading native agent '%s' to JVM process: '%s'", JEFF_AGENT_LIBRARY_NAME, name));

        String libraryName = System.mapLibraryName(JEFF_AGENT_LIBRARY_NAME);
        String tmpdir = System.getProperty("java.io.tmpdir");

        InputStream resource = Resources.getResourceAsStream(Boot.class, "/binlib/" + libraryName);
        Path target = Paths.get(tmpdir, libraryName);
        try {
            long coppied = Files.copy(resource, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        String[] list = new File("/tmp").list();
        System.out.printf("/tmp: \n\t%s%n%n", Joiner.on("\n\t").join(list));

        System.load(target.toString());

        // binlib/libjeff-native-agent.so
        /**
         * https://docs.oracle.com/javase/8/docs/jdk/api/attach/spec/com/sun/tools/attach/VirtualMachine.html
         */
        try (CloseableReference<VirtualMachine> vm = closeableFrom(VirtualMachine.attach(pid), VirtualMachine::detach)) {
            String options = null;
            vm.get().loadAgentPath(target.toString(), options);
        } catch (AgentLoadException | AgentInitializationException | IOException e) {
            throw new LinkageError(format("Cannot load '%s' library", target), e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        log.debug(format("Loaded '%s' as agent", JEFF_AGENT_LIBRARY_NAME));

    }

    private static Properties getProperties() {
        return Resources.getResourceAsProperties(JeffAgent.class, "/maven.properties");
    }
}