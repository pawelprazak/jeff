package com.bluecatcode.jeff.agent;

import org.eclipse.jetty.server.Server;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class JeffAgentTest {

    @BeforeClass
    public static void setUp() throws Exception {
        JeffAgent.initialize();
    }

    @Test
    public void testInitialize() throws Exception {
        Server server = new Server(8080);
        server.start();
        server.stop();
    }

}
