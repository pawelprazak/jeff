package com.bluecatcode.server;

import com.bluecatcode.jeff.agent.JeffAgent;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.BeforeClass;
import org.junit.Test;

public class JeffAgentTest {

    @BeforeClass
    public static void setUp() throws Exception {
        JeffAgent.initialize();
        Thread.sleep(10);
    }

    @Test
    public void testInitialize() throws Exception {
        Server server = new Server(8080);
//        ServletContextHandler handler = new ServletContextHandler(server, "/jetty");
//        handler.addServlet(JettyServlet.class, "/");
        server.start();
        server.stop();
    }

}
