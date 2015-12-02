package com.bluecatcode.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

/**
 * Sample jetty server.
 * To run in IntelliJ IDEA go to Run->Configuration and add VM Options:
 *
 * -Xbootclasspath/p:path_to_agent.jar -javaagent:path_to_agent.jar
 */
public class JettyServer {

    public static void main(String[] args) throws Exception {
        Server server = new Server(7070);
        ServletContextHandler handler = new ServletContextHandler(server, "/jetty");
        handler.addServlet(JettyServlet.class, "/");
        server.start();
        server.stop();
    }
}
