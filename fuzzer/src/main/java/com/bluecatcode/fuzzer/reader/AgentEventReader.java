package com.bluecatcode.fuzzer.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingDeque;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Read data from java agent and add to global queue.
 */
public class AgentEventReader extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(AgentEventReader.class);

    private Integer port = 7071;
    private final BlockingDeque<String> queue;

    public AgentEventReader(BlockingDeque<String> queue, Integer port) {
        this.queue = checkNotNull(queue);
        this.port = checkNotNull(port);
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            logger.debug("Waiting for events from java agent...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                String message = in.readLine();
                if (message != null) {
                    queue.put(message);
                }
                in.close();
                clientSocket.close();
            }
        } catch (Exception e) {
            // handle soft and try again later
            logger.error("Cannot read agent event", e);
        }
    }
}
