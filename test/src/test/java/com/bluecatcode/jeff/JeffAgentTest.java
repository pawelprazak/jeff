package com.bluecatcode.jeff;

import com.bluecatcode.jeff.agent.JeffAgent;
import org.eclipse.jetty.server.Server;
//import org.eclipse.jetty.servlet.ServletContextHandler;
//import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.BeforeClass;
import org.junit.Test;

//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;

public class JeffAgentTest {

    @BeforeClass
    public static void setUp() throws Exception {
        JeffAgent.initialize();
    }

    @Test
    public void testInitialize() throws Exception {
//        Server server = new Server(8080);
//        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
//        context.setContextPath("/");
//        server.setHandler(context);
//        context.addServlet(new ServletHolder(
//                        new HttpServlet() {
//                            @Override
//                            protected void doGet(HttpServletRequest req, HttpServletResponse resp)
//                                    throws ServletException, IOException {
//                                String message = System.getenv("POWERED_BY");
//                                if (message == null) {
//                                    message = "Deis";
//                                }
//                                resp.getWriter().print("Powered by " + message);
//                            }
//                        }), "/*");
//        server.start();
//        server.stop();
    }

}
