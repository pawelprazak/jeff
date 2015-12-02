package com.bluecatcode.jeff.notifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

/**
 * This class notify external fuzzer process about
 * flow execution and exceptions appearance of exceptions.
 */
public class SocketJeffEventNotifier implements JeffEventNotifier {

    private static final Logger logger = LoggerFactory.getLogger(SocketJeffEventNotifier.class);

    public SocketJeffEventNotifier() {
        /* EMPTY */
    }

    @Override
    public void notifyEvent(Event event) {
        try {
            String message = event.getEventType() + "#" + event.getTargetClassName() + "#" + event.getTargetMethodName();
            String hostname = "localhost"; // TODO move to external configuration
            Socket socket = new Socket(hostname, 7071);  // TODO move to external configuration
            OutputStream inputStream = socket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(inputStream);
            dataOutputStream.writeBytes(message);
            dataOutputStream.flush();
            dataOutputStream.close();
            socket.close();
        } catch (Exception e) {
            // handle soft and try again later
            logger.error("Cannot send event notification", e);
        }
    }
}
